package com.example.tabelog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.tabelog.entity.Category; // 追加
import com.example.tabelog.entity.Shop;
import com.example.tabelog.form.ShopEditForm;
import com.example.tabelog.form.ShopRegisterForm;
import com.example.tabelog.repository.CategoryRepository; // 追加
import com.example.tabelog.repository.ShopRepository;

@Service
public class ShopService {
    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository; // 追加

    public ShopService(ShopRepository shopRepository, CategoryRepository categoryRepository) { // コンストラクタに追加
        this.shopRepository = shopRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void create(ShopRegisterForm shopRegisterForm) {
        Shop shop = new Shop();
        MultipartFile imageFile = shopRegisterForm.getImageFile();

        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            shop.setImageName(hashedImageName);
        }

        shop.setName(shopRegisterForm.getName());
        shop.setDescription(shopRegisterForm.getDescription());
        shop.setPrice(shopRegisterForm.getPrice());
        shop.setPostalCode(shopRegisterForm.getPostalCode());
        shop.setAddress(shopRegisterForm.getAddress());
        shop.setOpenTime(shopRegisterForm.getOpenTime());
        shop.setHoliday(shopRegisterForm.getHoliday());

        // CategoryId から Category を取得し、セット
        Integer categoryId = Integer.parseInt(shopRegisterForm.getCategory());  // String型のCategoryIdをIntegerに変換
        Category category = categoryRepository.getReferenceById(categoryId); // Categoryエンティティを取得
        shop.setCategory(category);  // Categoryをセット

        shopRepository.save(shop);
    }
    // UUIDを使って生成したファイル名を返す
    public String generateNewFileName(String fileName) {
        String[] fileNames = fileName.split("\\.");
        for (int i = 0; i < fileNames.length - 1; i++) {
            fileNames[i] = UUID.randomUUID().toString();
        }
        String hashedFileName = String.join(".", fileNames);
        return hashedFileName;
    }
    
    @Transactional
    public void update(ShopEditForm shopEditForm) {
        Shop shop = shopRepository.getReferenceById(shopEditForm.getId());
        MultipartFile imageFile = shopEditForm.getImageFile();
        
        if (!imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename(); 
            String hashedImageName = generateNewFileName(imageName);
            Path filePath = Paths.get("src/main/resources/static/storage/" + hashedImageName);
            copyImageFile(imageFile, filePath);
            shop.setImageName(hashedImageName);
        }
        
        shop.setName(shopEditForm.getName());                
        shop.setDescription(shopEditForm.getDescription());
        shop.setPrice(shopEditForm.getPrice());
        shop.setPostalCode(shopEditForm.getPostalCode());
        shop.setAddress(shopEditForm.getAddress());
        shop.setHoliday(shopEditForm.getHoliday());
        shop.setOpenTime(shopEditForm.getOpenTime());

        // CategoryId から Category を取得し、セット
        Category category = categoryRepository.getReferenceById(shopEditForm.getCategoryId());
        shop.setCategory(category);
        
        shopRepository.save(shop);
    }

    // 画像ファイルを指定したファイルにコピーする
    public void copyImageFile(MultipartFile imageFile, Path filePath) {
        try {
            Files.copy(imageFile.getInputStream(), filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}