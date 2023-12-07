package com.example.demo.domain;

import com.example.demo.product.ProductResponse;
import com.example.demo.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model){
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(0);
        model.addAttribute("products", productDTOS);
        return "index";
    }

    @GetMapping("/product/create")
    public String create(){
        return "create";
    }

    @GetMapping("/products")
    public String productList(Model model) {
        List<ProductResponse.FindAllDTO> products = productService.findAllProducts();
        model.addAttribute("products", products);
        return "products"; // 상품 목록 페이지의 Thymeleaf 템플릿 이름
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductResponse.FindByIdDTO productDTO = productService.findByProductId(id);
        model.addAttribute("product", productDTO);
        return "detail";
    }

    @GetMapping("/loginPage")
    public String login() {
        return "login"; // 로그인 페이지로 이동하는 뷰 이름
    }

    @GetMapping("/joinPage")
    public String join() {
        return "join"; // 회원가입 페이지로 이동하는 뷰 이름
    }

}
