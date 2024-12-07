package com.payment_service.domain.payment.model.response.toss;

import com.payment_service.domain.payment.model.request.PaymentConfirmInPut;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TossPaymentConfirmationWithPspRawDataResponse {

	private TossPaymentConfirmationResponse tossPaymentConfirmationResponse;
	private String pspRawData;
	private TossFailureResponse tossFailureResponse;


	@Builder
	private TossPaymentConfirmationWithPspRawDataResponse(TossPaymentConfirmationResponse tossPaymentConfirmationResponse, String pspRawData, TossFailureResponse tossFailureResponse) {
		this.tossPaymentConfirmationResponse = tossPaymentConfirmationResponse;
		this.pspRawData = pspRawData;
		this.tossFailureResponse = tossFailureResponse;
	}

	public static TossPaymentConfirmationWithPspRawDataResponse of(TossPaymentConfirmationResponse tossPaymentConfirmationResponse, String pspRawData) {

		return TossPaymentConfirmationWithPspRawDataResponse.builder()
				.tossPaymentConfirmationResponse(tossPaymentConfirmationResponse)
				.pspRawData(pspRawData)
				.tossFailureResponse(null)
				.build();
	}

	public static TossPaymentConfirmationWithPspRawDataResponse of(PaymentConfirmInPut request, TossFailureResponse tossFailureResponse, String pspRawData) {

		return TossPaymentConfirmationWithPspRawDataResponse.builder()
				.tossPaymentConfirmationResponse(null)
				.pspRawData(pspRawData)
				.tossFailureResponse(tossFailureResponse)
				.build();
	}
}