package com.example.tabelog.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.tabelog.entity.Category;
import com.example.tabelog.entity.Shop;
import com.example.tabelog.form.ShopEditForm;
import com.example.tabelog.form.ShopRegisterForm;
import com.example.tabelog.repository.CategoryRepository;
import com.example.tabelog.repository.ShopRepository;
import com.example.tabelog.service.ShopService;

@Controller
@RequestMapping("/admin/shops")
public class AdminShopController {
     private final ShopRepository shopRepository;
     private final ShopService shopService;
     private final CategoryRepository categoryRepository;
     
     public AdminShopController(ShopRepository shopRepository, ShopService shopService, CategoryRepository categoryRepository) {
         this.shopRepository = shopRepository;  
         this.shopService = shopService;
         this.categoryRepository = categoryRepository; // 修正
     }	
     
     @GetMapping
     public String index(Model model, @PageableDefault(page = 0, size = 10, sort = "shopId", direction = Direction.ASC) Pageable pageable, @RequestParam(name = "keyword", required = false) String keyword) {
         Page<Shop> shopPage;
         
         if (keyword != null && !keyword.isEmpty()) {
             shopPage = shopRepository.findByNameLike("%" + keyword + "%", pageable);                
         } else {
             shopPage = shopRepository.findAll(pageable);
         }  
         
         model.addAttribute("shopPage", shopPage); 
         model.addAttribute("keyword", keyword);
         
         return "admin/shops/index";
     }  
     
     @GetMapping("/{id}")
     public String show(@PathVariable(name = "id") Integer id, Model model) {
         Shop shop = shopRepository.getReferenceById(id);
         
         model.addAttribute("shop", shop);
         
         return "admin/shops/show";
     }    
     
     @GetMapping("/register")
     public String register(Model model) {
         model.addAttribute("shopRegisterForm", new ShopRegisterForm());
         List<Category> categories = categoryRepository.findAll();
         model.addAttribute("categories", categories); 
         
         return "admin/shops/register";
     }   
     
     @PostMapping("/create")
     public String create(@ModelAttribute @Validated ShopRegisterForm shopRegisterForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/shops/register";
         }
         
         shopService.create(shopRegisterForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗を登録しました。");    
         
         return "redirect:/admin/shops";
     }    
     
     @GetMapping("/{id}/edit")
     public String edit(@PathVariable(name = "id") Integer id, Model model) {
         Shop shop = shopRepository.getReferenceById(id);
         String imageName = shop.getImageName();
         ShopEditForm shopEditForm = new ShopEditForm(
             shop.getShopId(), 
             shop.getName(), 
             null, 
             shop.getDescription(), 
             shop.getPrice(), 
             shop.getPostalCode(), 
             shop.getAddress(),
             shop.getHoliday(),  
             shop.getOpenTime(),  
             shop.getCategory().getCategoryId()  
         );

         List<Category> categories = categoryRepository.findAll(); // 追加
         model.addAttribute("categories", categories); // 追加
         
         model.addAttribute("imageName", imageName);
         model.addAttribute("shopEditForm", shopEditForm);
         
         return "admin/shops/edit";
     }
     
     @PostMapping("/{id}/update")
     public String update(@ModelAttribute @Validated ShopEditForm shopEditForm, BindingResult bindingResult, RedirectAttributes redirectAttributes) {        
         if (bindingResult.hasErrors()) {
             return "admin/shops/edit";
         }
         
         shopService.update(shopEditForm);
         redirectAttributes.addFlashAttribute("successMessage", "店舗情報を編集しました。");
         
         return "redirect:/admin/shops";
     }
     
     @PostMapping("/{id}/delete")
     public String delete(@PathVariable(name = "id") Integer id, RedirectAttributes redirectAttributes) {        
         shopRepository.deleteById(id);
         
         redirectAttributes.addFlashAttribute("successMessage", "店舗を削除しました。");
         
         return "redirect:/admin/shops";
     }
}