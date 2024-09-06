package com.example.tabelog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.tabelog.form.ReservationRegisterForm;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {

	@Value("${stripe.api-key}")
	private String stripeApiKey;

	// Stripe Checkout セッションを作成する
	public String createStripeSession(String shopName, ReservationRegisterForm reservationRegisterForm,
			HttpServletRequest httpServletRequest) {
		// StripeのAPIキーを初期化
		Stripe.apiKey = stripeApiKey;
		// 現在のリクエストURLを取得
		String requestUrl = httpServletRequest.getRequestURL().toString();

		// Stripe Checkoutのセッション作成パラメータを設定
		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD) // 支払い方法をカードに設定
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPriceData(
										SessionCreateParams.LineItem.PriceData.builder()
												.setProductData(
														SessionCreateParams.LineItem.PriceData.ProductData.builder()
																.setName(shopName) // 店舗名を設定
																.build())
												.setUnitAmount((long) reservationRegisterForm.getAmount() * 100) // 金額を設定（Stripeではセント単位）
												.setCurrency("jpy") // 通貨を設定
												.build())
								.setQuantity(1L) // 数量を設定
								.build())
				.setMode(SessionCreateParams.Mode.PAYMENT) // 支払いモードを設定
				.setSuccessUrl(
						requestUrl.replaceAll("/shops/[0-9]+/reservations/confirm", "") + "/reservations?reserved") // 成功時のリダイレクトURL
				.setCancelUrl(requestUrl.replace("/reservations/confirm", "")) // キャンセル時のリダイレクトURL
				.setPaymentIntentData(SessionCreateParams.PaymentIntentData.builder()
						.putMetadata("shopId", reservationRegisterForm.getShopId().toString()) // 店舗IDをメタデータに追加
						.putMetadata("userId", reservationRegisterForm.getUserId().toString()) // ユーザーIDをメタデータに追加
						.putMetadata("reservationDate", reservationRegisterForm.getReservationDate().toString()) // 予約日をメタデータに追加
						.putMetadata("numberOfPeople", reservationRegisterForm.getNumberOfPeople().toString()) // 人数をメタデータに追加
						.putMetadata("amount", reservationRegisterForm.getAmount().toString()) // 金額をメタデータに追加
						.build())
				.build();

		try {
			// セッションを作成し、セッションIDを取得
			Session session = Session.create(params);
			return session.getId(); // セッションURLを返す
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}
}
