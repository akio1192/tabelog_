package com.example.tabelog.form;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShopEditForm {
    @NotNull
    private Integer id;    

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

    @NotBlank(message = "定休日を入力してください。")
    private String holiday;  // 追加

    @NotBlank(message = "営業時間を入力してください。")
    private String openTime;  // 追加

    @NotNull(message = "カテゴリーを選択してください。")
    private Integer categoryId;  // 追加
}