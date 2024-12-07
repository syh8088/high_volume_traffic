package com.wallet_service.domain.wallet.service;

import com.wallet_service.PaymentEventMessage;
import com.wallet_service.domain.payment.model.response.PaymentOrderWithSellerOutPut;
import com.wallet_service.domain.payment.service.PaymentOrderQueryService;
import com.wallet_service.domain.wallet.enums.WalletEventMessageType;
import com.wallet_service.domain.wallet.model.WalletEventMessage;
import com.wallet_service.domain.wallet.model.request.WalletOutPut;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public WalletEventMessage settlementProcess(PaymentEventMessage paymentEventMessage) {

        /**
         * 정산이 이미 처리되었는지 여부를 판단 합니다.
         * 이미 처리된 결제 이벤트 메시지라면 월렛 이벤트 메시지를 반환하도록 한다.
         */
        String orderId = paymentEventMessage.orderId();

        if (walletQueryService.isAlreadyProcessWallet(orderId)) {
            // 이미 처리된 결제 이벤트인데 왜 월렛 이벤트 메시지를 반환 하는 이유는
            // 정산 처리를 완료한 이후에 갑작스럽게 어플리케이션이 크래시 나서 월렛 이벤트 메시지 발행에 실패 할 가능성이 있기 떄문
            // 그래서 발행 실패할 경우를 대비해서 이미 처리한 이벤트라도 월렛 이벤트 메시지를 한 번 더 발행
            // 이전에도 말했지만 이렇게 처리하는 건 중복 메시지가 발행할 수 있지만 큰 문제는 없습니다.
            return this.createWalletEventMessage(orderId);
        }

        List<PaymentOrderWithSellerOutPut> paymentOrders
                = paymentOrderQueryService.selectPaymentOrderListWithSellerByOrderId(paymentEventMessage.orderId());

        // 결제 주문 데이터에 저장되어 있는 판매자 ID 를 바탕으로 판매자 지갑대를 조회하는 로직
        Map<Long, List<PaymentOrderWithSellerOutPut>> paymentOrderWithSellerGroupingSellerNoMap
                = paymentOrders.stream()
                .collect(Collectors.groupingBy(PaymentOrderWithSellerOutPut::getSellerNo));

        List<WalletOutPut> updatedWallets = this.getUpdatedWallets(paymentOrderWithSellerGroupingSellerNoMap);
        walletCommandService.updateWalletList(updatedWallets);
        walletTransactionCommandService.insertWalletTransactionList(updatedWallets);

        return this.createWalletEventMessage(paymentEventMessage.orderId());
    }

    private WalletEventMessage createWalletEventMessage(String orderId) {
        return WalletEventMessage.of(
                WalletEventMessageType.SUCCESS,
                Map.of("orderId", orderId)
        );
    }

    private List<WalletOutPut> getUpdatedWallets(Map<Long, List<PaymentOrderWithSellerOutPut>> paymentOrdersBySellerId) {

        Set<Long> sellerNoList = paymentOrdersBySellerId.keySet();
        List<WalletOutPut> wallets = walletQueryService.selectWalletListBySellerNoList(sellerNoList.stream().toList());

        return wallets.stream()
                .peek(wallet -> {
                    List<PaymentOrderWithSellerOutPut> paymentOrders = paymentOrdersBySellerId.get(wallet.getWalletNo());
                    wallet.calculateBalance(paymentOrders);
                })
                .collect(Collectors.toList());
    }
}
