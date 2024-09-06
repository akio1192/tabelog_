package com.example.tabelog.service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tabelog.entity.Reservation;
import com.example.tabelog.entity.Shop;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationRegisterForm;
import com.example.tabelog.repository.ReservationRepository;
import com.example.tabelog.repository.ShopRepository;
import com.example.tabelog.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.model.checkout.Session;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ReservationService {
	
	private final ReservationRepository reservationRepository;  
	private final ShopRepository shopRepository;  
	private final UserRepository userRepository;  

	public ReservationService(ReservationRepository reservationRepository, ShopRepository shopRepository, UserRepository userRepository) {
	    this.reservationRepository = reservationRepository;  
	    this.shopRepository = shopRepository;  
	    this.userRepository = userRepository;  
	}    

	@Transactional
	public void create(ReservationRegisterForm reservationRegisterForm) { 
	    Reservation reservation = new Reservation();
	    Shop shop = shopRepository.getReferenceById(reservationRegisterForm.getShopId());
	    User user = userRepository.getReferenceById(reservationRegisterForm.getUserId());
	    LocalDate reservationDate = LocalDate.parse(reservationRegisterForm.getReservationDate());
	             
	    reservation.setShop(shop);
	    reservation.setUser(user);
	    reservation.setReservationDatetime(reservationDate.atStartOfDay());
	    reservation.setNumberOfPeople(reservationRegisterForm.getNumberOfPeople());
	    reservation.setAmount(reservationRegisterForm.getAmount());
	    
	    reservationRepository.save(reservation);
	}

	@Transactional
    public void cancelReservation(Integer reservationId) {
        reservationRepository.deleteById(reservationId);
    }
	
    // 人数に基づいて料金を計算する
    public Integer calculateAmount(LocalDate reservationDate, Integer pricePerPerson, Integer numberOfPeople) {
        // 人数に基づいて料金を計算
        int amount = pricePerPerson * numberOfPeople;
        return amount;
    }
    
    @Transactional
    public String createStripeSession(String shopName, ReservationRegisterForm reservationRegisterForm, HttpServletRequest request) {
        // StripeのAPIキーを設定
        Stripe.apiKey = "your_stripe_secret_key";  // シークレットキーを指定

        // 商品情報（例: 店舗名、金額）を設定
        Map<String, Object> priceData = new HashMap<>();
        priceData.put("name", shopName);
        priceData.put("amount", reservationRegisterForm.getAmount() * 100);  // 金額はセント単位
        priceData.put("currency", "jpy");
        priceData.put("quantity", 1);

        // 決済セッションのパラメータを設定
        Map<String, Object> sessionParams = new HashMap<>();
        sessionParams.put("payment_method_types", Arrays.asList("card"));
        sessionParams.put("line_items", Arrays.asList(Collections.singletonMap("price_data", priceData)));
        sessionParams.put("mode", "payment");
        sessionParams.put("success_url", request.getRequestURL().toString() + "?success=true");  // 成功時のURL
        sessionParams.put("cancel_url", request.getRequestURL().toString() + "?canceled=true");  // キャンセル時のURL

        try {
            // Stripeセッションを作成
            Session session = Session.create(sessionParams);
            return session.getId();  // セッションIDを返す
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
