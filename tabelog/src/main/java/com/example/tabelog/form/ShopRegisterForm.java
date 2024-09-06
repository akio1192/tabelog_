package com.example.tabelog.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShopRegisterForm {
    @NotBlank(message = "店舗名を入力してください。")
    private String name;

    private MultipartFile imageFile;

    @NotBlank(message = "説明を入力してください。")
    private String description;

    @NotNull(message = "料金を入力してください。")
    @Min(value = 1, message = "料金は1円以上に設定してください。")
    private Integer price;

    @NotBlank(message = "郵便番号を入力してください。")
    private String postalCode;

    @NotBlank(message = "住所を入力してください。")
    private String address;

    @NotBlank(message = "営業時間を入力してください。")
    private String openTime;

    @NotBlank(message = "定休日を入力してください。")
    private String holiday;

    @NotBlank(message = "電話番号を入力してください。")
    private String phoneNumber;

    @NotBlank(message = "カテゴリを入力してください。")
    private String category; // これは、別のエンティティや選択肢が用意されている場合に変更が必要です
}
