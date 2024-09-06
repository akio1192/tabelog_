package com.example.tabelog.controller;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Reservation;
import com.example.tabelog.entity.Shop;
import com.example.tabelog.entity.User;
import com.example.tabelog.form.ReservationInputForm;
import com.example.tabelog.form.ReservationRegisterForm;
import com.example.tabelog.repository.ReservationRepository;
import com.example.tabelog.repository.ShopRepository;
import com.example.tabelog.security.UserDetailsImpl;
import com.example.tabelog.service.ReservationService;
import com.example.tabelog.service.StripeService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ReservationController {
    private final ReservationRepository reservationRepository;      
    private final ShopRepository shopRepository;
    private final ReservationService reservationService;
    private final StripeService stripeService; 
    
    public ReservationController(ReservationRepository reservationRepository, ShopRepository shopRepository, ReservationService reservationService, StripeService stripeService) {        
        this.reservationRepository = reservationRepository;  
        this.shopRepository = shopRepository;
        this.reservationService = reservationService;
        this.stripeService = stripeService;
        
    }    

    @GetMapping("/reservations")
    public String index(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl, 
                        @PageableDefault(page = 0, size = 10, sort = "reservationId", direction = Direction.ASC) Pageable pageable, 
                        Model model) {
        User user = userDetailsImpl.getUser();
        Page<Reservation> reservationPage = reservationRepository.findByUserOrderByCreatedAtDesc(user, pageable);
        
        model.addAttribute("reservationPage", reservationPage);         
        
        return "reservations/index";
    }
    
    @GetMapping("/shops/{id}/reservations/input")
    public String input(@PathVariable(name = "id") Integer id,
                        @ModelAttribute @Validated ReservationInputForm reservationInputForm,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes,
                        Model model) 
    {
        // 店舗情報を取得
        Shop shop = shopRepository.getReferenceById(id);
        
        // バリデーションエラーがある場合
        if (bindingResult.hasErrors()) {            
            model.addAttribute("shop", shop);            
            model.addAttribute("errorMessage", "予約内容に不備があります。"); 
            return "shops/show";
        }
        
        // バリデーションが通った場合
        redirectAttributes.addFlashAttribute("reservationInputForm", reservationInputForm);           
        
        return "redirect:/shops/{id}/reservations/confirm";
    }
    
    @GetMapping("/shops/{id}/reservations/confirm")
    public String confirm(@PathVariable(name = "id") Integer id,
                          @ModelAttribute ReservationInputForm reservationInputForm,
                          @AuthenticationPrincipal UserDetailsImpl userDetailsImpl,   
                          HttpServletRequest httpServletRequest,
                          Model model) 
    {        
        // 店舗情報を取得
        Shop shop = shopRepository.getReferenceById(id);
        User user = userDetailsImpl.getUser(); 

        // 予約日を取得する
        LocalDate reservationDate = reservationInputForm.getReservationDate();
      
        // 料金を計算する（例えば人数に基づいて計算）
        Integer price = shop.getPrice();        
        Integer amount = reservationService.calculateAmount(reservationDate, price, reservationInputForm.getNumberOfPeople());
             
        // ReservationRegisterFormに必要なデータをセット
        ReservationRegisterForm reservationRegisterForm = new ReservationRegisterForm(shop.getShopId(), user.getUserId(), reservationDate.toString(), reservationInputForm.getNumberOfPeople(), amount);
        
        String sessionId = stripeService.createStripeSession(shop.getName(), reservationRegisterForm, httpServletRequest);
        
        model.addAttribute("shop", shop);  
        model.addAttribute("reservationRegisterForm", reservationRegisterForm);       
        model.addAttribute("sessionId", sessionId);
        
        return "reservations/confirm";
    }
    
    @PostMapping("/reservations/cancel/{reservationId}")
    public String cancelReservation(@PathVariable("reservationId") Integer reservationId, RedirectAttributes redirectAttributes) {
        reservationService.cancelReservation(reservationId);  // サービスでキャンセル処理を実行
        redirectAttributes.addFlashAttribute("message", "予約をキャンセルしました。");  // フラッシュメッセージを設定
        return "redirect:/reservations"; 
    }
    /*
    @PostMapping("/shops/{id}/reservations/create")
    public String create(@ModelAttribute ReservationRegisterForm reservationRegisterForm) {                
        reservationService.create(reservationRegisterForm);        
        
        return "redirect:/reservations?reserved";
    }
    */
}