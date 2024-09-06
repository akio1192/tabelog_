package com.example.tabelog.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRegisterForm {    
    private Integer shopId;  // 店舗ID
    private Integer userId;  // ユーザーID    
    private String reservationDate;  // 予約日
    private Integer numberOfPeople;  // 予約人数
    private Integer amount;  // 料金
}