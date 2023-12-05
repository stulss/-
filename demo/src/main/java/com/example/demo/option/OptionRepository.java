package com.example.demo.option;

import com.example.demo.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByProductId(Long id);
    List<Option> findByProduct(Product product);
}
