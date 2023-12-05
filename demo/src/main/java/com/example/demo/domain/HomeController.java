package com.example.demo.domain;

import com.example.demo.product.ProductResponse;
import com.example.demo.product.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    @GetMapping("/product/productList")
    public String productList(Model model) {
        List<ProductResponse.FindAllDTO> productDTOS = productService.findAll(0);
        model.addAttribute("products", productDTOS);
        return "paging";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        ProductResponse.FindByIdDTO productDTO = productService.findByProductId(id);
        model.addAttribute("product", productDTO);
        return "detail";
    }

}
