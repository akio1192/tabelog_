package com.example.tabelog.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.tabelog.entity.Shop;
import com.example.tabelog.repository.ShopRepository;

@Controller
public class HomeController {
    private final ShopRepository shopRepository;        
    
    public HomeController(ShopRepository shopRepository) {
        this.shopRepository = shopRepository;            
    }    
    
    @GetMapping("/")
    public String index(Model model) {
        List<Shop> newShops = shopRepository.findTop10ByOrderByCreatedAtDesc();
        model.addAttribute("newShops", newShops);        
        
        return "index";
    }   
}