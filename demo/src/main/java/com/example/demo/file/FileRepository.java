package com.example.demo.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<ProductFile, Long> {
    List<ProductFile> findByProductId(Long productId);

    void deleteByProductId(Long productId);
}
