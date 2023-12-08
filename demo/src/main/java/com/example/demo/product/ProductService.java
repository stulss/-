package com.example.demo.product;

import com.example.demo.core.error.exception.Exception404;
import com.example.demo.file.FileDTO;
import com.example.demo.option.Option;
import com.example.demo.option.OptionRepository;
import com.example.demo.file.FileRepository;
import com.example.demo.file.ProductFile;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final OptionRepository optionRepository;
    private final FileRepository fileRepository;

    private final String filePath = "C:/Users/G/Desktop/green/Board Files/";

    @Transactional
    public void createProduct(ProductResponse.create createDto, MultipartFile[] files) throws IOException {
        // 게시글 저장
        Product product = productRepository.save(createDto.toEntity());
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                // 파일 경로 생성
                Path uploadPath = Paths.get(filePath);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 파일명 추출
                String originalFileName = file.getOriginalFilename();

                // 파일 고유 식별자 생성
                String uuid = UUID.randomUUID().toString();

                // 파일 저장 경로 지정
                String path = filePath + uuid + originalFileName;

                // 파일 저장
                file.transferTo(new java.io.File(path));

                // 파일 엔티티 생성 및 저장
                ProductFile boardFile = ProductFile.builder()
                        .filePath(filePath)
                        .fileName(originalFileName)
                        .uuid(uuid)
                        .fileType(file.getContentType())
                        .fileSize(file.getSize())
                        .product(product)
                        .build();
                fileRepository.save(boardFile);
            }
        }
    }

    // 전체 상품 검색
    public List<ProductResponse.FindAllDTO> findAll(int page) {
        Pageable pageable = PageRequest.of(page,2);
        Page<Product> productPage = productRepository.findAll(pageable);

        List<ProductResponse.FindAllDTO> productDTOS =
                productPage.getContent().stream().map(ProductResponse.FindAllDTO::new)
                .collect(Collectors.toList());
        return productDTOS;
    }

    // 개별 상품 검색
    public ProductResponse.FindByIdDTO findByProductId(Long id) {
        // List<Option> -> ProductResponse.FindByIdDTO
        //dto안에 리스트가 포함되어 있기때문에 바로 dto를 반환
        Product product = productRepository.findById(id).orElseThrow(
                () -> new Exception404("해당 상품("+id+")은 등록되어 있지 않습니다.") );

        // product.getId() 로  Option 상품 검색
        List<Option> optionList = optionRepository.findByProductId(product.getId());

        // 검색이 완료된 제품 반환.
        return new ProductResponse.FindByIdDTO(product,optionList);
    }

    @Transactional
    public void updateProduct(ProductResponse.FindByIdDTO productDTO) {
        Optional<Product> productOptional = productRepository.findById(productDTO.getId());

        if(productOptional.isPresent()){
            Product product = productOptional.get();
            product.update(productDTO.toEntity());
            productRepository.save(product);
        }


    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public List<ProductResponse.FindAllDTO> findAllProducts() {
        List<Product> products = productRepository.findAll(); // ProductRepository를 통해 모든 상품을 가져옴
        return products.stream()
                .map(ProductResponse.FindAllDTO::new) // Product를 FindAllDTO로 변환
                .collect(Collectors.toList());
    }

    private void uploadFile(MultipartFile file, Product product) throws IOException {
        if (!file.isEmpty()) {
            Path uploadPath = Paths.get(filePath);

            // ** 만약 경로가 없다면... 경로 생성.
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // ** 파일명 추출
            String originalFileName = file.getOriginalFilename();

            // ** 확장자 추출
            String formatType = originalFileName.substring(
                    originalFileName.lastIndexOf("."));

            // ** UUID 생성
            String uuid = UUID.randomUUID().toString();

            // 경로 지정
            String path = filePath + uuid + originalFileName;

            // 경로에 파일을 저장.
            file.transferTo(new java.io.File(path));

            ProductFile productFile = ProductFile.builder()
                    .filePath(filePath)
                    .fileName(originalFileName)
                    .uuid(uuid)
                    .fileType(formatType)
                    .fileSize(file.getSize())
                    .product(product)
                    .build();

            fileRepository.save(productFile);
        }
    }

    public Page<ProductResponse.PagingDto> paging(Pageable pageable) {

        int page = pageable.getPageNumber() - 1;

        int size = 5;

        Page<Product> products = productRepository.findAll(PageRequest.of(page, size));
        FileDTO fileDTO = new FileDTO();

        return products.map(product -> {
            String imagePath = null;

            if (!product.getFiles().isEmpty()) {
                ProductFile file = product.getFiles().get(0);
                imagePath = "/image/" + file.getUuid() + "/" + file.getFileName();
            }

            return new ProductResponse.PagingDto(
                    product.getId(),
                    product.getProductName(),
                    product.getPrice(),
                    imagePath  // imagePath 전달
            );
        });
    }
}