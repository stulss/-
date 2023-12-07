package com.example.demo.file;

import com.example.demo.product.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ProductFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ** 파일 경로
    private String filePath;

    // ** 파일 이름
    private String fileName;

    // ** uuid (랜덤 키)
    private String uuid;

    // ** 파일 포멧
    private String fileType;

    // ** 파일 크기
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public ProductFile(Long id, String filePath, String fileName , String uuid, String fileType, Long fileSize, Product product) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.uuid = uuid;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.product = product;
    }

    public void fileUpdateFromDTO(FileDTO fileDTO){

        // ** 모든 변경 사항을 셋팅.
        this.fileName = fileDTO.getFileName();
        this.fileSize = fileDTO.getFileSize();
        this.fileType = fileDTO.getFileType();
        this.uuid = fileDTO.getUuid();
    }
}













