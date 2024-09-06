package com.example.tabelog.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.tabelog.entity.Shop;

public interface ShopRepository extends JpaRepository<Shop, Integer> {
    public Page<Shop> findByNameLike(String keyword, Pageable pageable);
    
    public Page<Shop> findByNameLikeOrAddressLikeOrderByCreatedAtDesc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Shop> findByNameLikeOrAddressLikeOrderByPriceAsc(String nameKeyword, String addressKeyword, Pageable pageable);  
    public Page<Shop> findByAddressLikeOrderByCreatedAtDesc(String area, Pageable pageable);
    public Page<Shop> findByAddressLikeOrderByPriceAsc(String area, Pageable pageable);
    public Page<Shop> findByPriceLessThanEqualOrderByCreatedAtDesc(Integer price, Pageable pageable);
    public Page<Shop> findByPriceLessThanEqualOrderByPriceAsc(Integer price, Pageable pageable); 
    public Page<Shop> findAllByOrderByCreatedAtDesc(Pageable pageable);
    public Page<Shop> findAllByOrderByPriceAsc(Pageable pageable);
    public List<Shop> findTop10ByOrderByCreatedAtDesc();

    // 東区と名東区を区別するためのメソッド
    public Page<Shop> findByAddressLikeAndAddressNotLike(String area, String excludeArea, Pageable pageable);
}