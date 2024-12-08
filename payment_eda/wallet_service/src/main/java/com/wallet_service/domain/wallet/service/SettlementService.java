package com.wallet_service.domain.wallet.service;

import com.wallet_service.domain.payment.model.response.PaymentOrderWithSellerOutPut;
import com.wallet_service.domain.payment.service.PaymentOrderQueryService;
import com.wallet_service.domain.wallet.enums.WalletEventMessageType;
import com.wallet_service.domain.wallet.model.WalletEventMessage;
import com.wallet_service.domain.wallet.model.response.WalletOutPut;
import com.wallet_service.error.exception.RetryExhaustedWithOptimisticLockingFailureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SettlementService {

    private final WalletQueryService walletQueryService;
    private final WalletCommandService walletCommandService;
    private final WalletTransactionCommandService walletTransactionCommandService;
    private final PaymentOrderQueryService paymentOrderQueryService;

    private final TransactionTemplate transactionTemplate;

    public WalletEventMessage settlementProcess(String orderId) {

        /**
         * 정산이 이미 처리되었는지 여부를 판단 합니다.
         * 이미 처리된 결제 이벤트 메시지라면 월렛 이벤트 메시지를 반환하도록 한다.
         */
        if (walletQueryService.isAlreadyProcessWallet(orderId)) {
            // 이미 처리된 결제 이벤트인데 왜 월렛 이벤트 메시지를 반환 하는 이유는
            // 정산 처리를 완료한 이후에 갑작스럽게 어플리케이션이 크래시 나서 월렛 이벤트 메시지 발행에 실패 할 가능성이 있기 떄문
            // 그래서 발행 실패할 경우를 대비해서 이미 처리한 이벤트라도 월렛 이벤트 메시지를 한 번 더 발행
            // 이전에도 말했지만 이렇게 처리하는 건 중복 메시지가 발행할 수 있지만 큰 문제는 없습니다.
            return this.createWalletEventMessage(orderId);
        }

        List<PaymentOrderWithSellerOutPut> paymentOrders
                = paymentOrderQueryService.selectPaymentOrderListWithSellerByOrderId(orderId);

        // 결제 주문 데이터에 저장되어 있는 판매자 ID 를 바탕으로 판매자 지갑대를 조회하는 로직
        Map<Long, List<PaymentOrderWithSellerOutPut>> paymentOrderWithSellerGroupingSellerNoMap
                = paymentOrders.stream()
                .collect(Collectors.groupingBy(PaymentOrderWithSellerOutPut::getSellerNo));

        List<WalletOutPut> updatedWallets = this.getUpdatedWallets(paymentOrderWithSellerGroupingSellerNoMap);


//        walletCommandService.updateWalletList(updatedWallets);
//        walletTransactionCommandService.insertWalletTransactionList(updatedWallets);
//

        try {
            this.updateWallet(updatedWallets);
        }
        catch (ObjectOptimisticLockingFailureException e) {
            this.retrySaveOperation(updatedWallets);
        }

        return this.createWalletEventMessage(orderId);
    }

    /**
     * 일반적으로 트랜잭션 안에서 예외가 발생했을 때 try-catch 로 예외를 잡으려고 하는 건 잘 안될것이다.
     * 예외가 발생한 트랜잭션은 롤백 마킹을 하기 때문에 해당 트랜잭션은 재사용될 수 없어서 무조건 롤백 될것이고
     * 전파되는 예외도 Object Optimistic Locking Exception 으로 전파되는 게 아니라
     * 롤백 마킹 때문에 Unexpected Rollback Exception 으로 전파될 것이다.
     * 그러니깐 일반적으로 그러니까 Optimistic Locking Exception 을 예외를 잡을 수 없다.
     */
    private void updateWallet(List<WalletOutPut> updatedWallets) {
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {
                walletCommandService.updateWalletList(updatedWallets);
                walletTransactionCommandService.insertWalletTransactionList(updatedWallets);
            }
        });
    }

    private WalletEventMessage createWalletEventMessage(String orderId) {
        return WalletEventMessage.of(
                WalletEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );
    }

    private List<WalletOutPut> getUpdatedWallets(Map<Long, List<PaymentOrderWithSellerOutPut>> paymentOrderWithSellerGroupingSellerNoMap) {

        Set<Long> sellerNoList = paymentOrderWithSellerGroupingSellerNoMap.keySet();
        List<WalletOutPut> wallets = walletQueryService.selectWalletListBySellerNoList(sellerNoList.stream().toList());

        return wallets.stream()
                .peek(wallet -> {
                    List<PaymentOrderWithSellerOutPut> paymentOrders = paymentOrderWithSellerGroupingSellerNoMap.get(wallet.getWalletNo());
                    wallet.calculateBalance(paymentOrders);
                })
                .collect(Collectors.toList());
    }

    /**
     * 재시도 함수는 최대 3번만 제시도하도록 만들고 제시도 사이에 충돌을 방지하기 위해 약간의 지연 시간을 준다.
     *
     */
    private void retrySaveOperation(List<WalletOutPut> wallets) {

        int maxRetries = 3;
        int baseDelay = 100;
        int retryCount = 0;

        while (true) {
            try {
                this.performSaveOperationWithRecent(wallets);
                break;
            }
            catch (ObjectOptimisticLockingFailureException e) {
                // 제시도 할 때도 만약에 충돌이 발생한다면 제시도 횟수를 초과했는지 검사하고 초과했다면
                // 제시도가 모두 소진되었다는 Exception 발생 되도록 한다.
                if (++retryCount > maxRetries) {
                    throw new RetryExhaustedWithOptimisticLockingFailureException(e.getMessage() != null ? e.getMessage() : "exhausted retry count.");
                }

                // 만약에 제시도 횟수를 초과하진 않았더라면 충돌을 방지하기 위해서 베이스 딜레이와 지터 값을 통해 다음 제시도까지 약간 대기하도록 한다.
                waitForNextRetry(baseDelay);
            }
        }
    }

    /**
     * 재시도를 할 때 최신 상태의 지갑을 기반으로 지갑 상태를 업데이트하도록 로직
     */
    private void performSaveOperationWithRecent(List<WalletOutPut> requestWallets) {

        Set<Long> walletNoList = requestWallets.stream()
                .map(WalletOutPut::getWalletNo)
                .collect(Collectors.toSet());

        // 지갑의 상태를 최신 상태로 가져와서 제시도하도록 해야 된다.
        // 먼저 Wallet No 를 기반으로 다시 데이터베이스에 저장되어 있는 최신 상태의 지갑을 조회
        List<WalletOutPut> walletOutPuts = walletQueryService.selectWalletListWithWalletTransactionsByWalletNoList(walletNoList.stream().toList());

        // 다음으로 최신 상태의 지갑을 가지고 기존에 정산된 내역을 더해서 지갑을 업데이트 한다.
        Map<Long, WalletOutPut> recentWalletsGropingWalletNoMap = walletOutPuts.stream()
                .collect(Collectors.toMap(WalletOutPut::getWalletNo, wallet -> wallet));

        List<WalletOutPut> updatedWallets = requestWallets.stream()
                .map(requestWallet -> {
                    WalletOutPut recentWallet = recentWalletsGropingWalletNoMap.get(requestWallet.getWalletNo());
                    recentWallet.addBalance(requestWallet);
                    recentWallet.updateWalletTransactions(requestWallet.getWalletTransactionList());
                    return recentWallet;
                })
                .toList();

        /**
         * 여기서 중요한 포인트는 트랜잭션을 최대한 늦게 시작하도록 만든다.
         * 트랜잭션은 가능한 늦게 시작하고 빨리 끝내는 게 좋다.
         * 데이터베이스에서 트랜잭션을 관리하는데 유지하는 비용이 들어가기 때문!!
         */
        transactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(@NotNull TransactionStatus status) {

                walletCommandService.updateWalletList(updatedWallets);
                walletTransactionCommandService.insertWalletTransactionList(updatedWallets);
            }
        });
    }

    private void waitForNextRetry(int baseDelay) {
        long jitter = (long) (Math.random() * baseDelay);

        try {
            Thread.sleep(jitter);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupted during retry wait", e);
        }
    }
}
