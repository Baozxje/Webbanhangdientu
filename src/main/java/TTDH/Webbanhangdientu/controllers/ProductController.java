// controllers/ProductController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.dto.product.ProductPageResponse;
import TTDH.Webbanhangdientu.dto.product.ProductRequest;
import TTDH.Webbanhangdientu.dto.product.ProductResponse;
import TTDH.Webbanhangdientu.services.ProductService;
import TTDH.Webbanhangdientu.services.FileUploadService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    // Lấy danh sách sản phẩm có phân trang
    @GetMapping
    public ResponseEntity<ProductPageResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        ProductPageResponse response = productService.getAllProducts(page, size, sortBy, direction);
        return ResponseEntity.ok(response);
    }

    // Tìm kiếm sản phẩm theo tên
    @GetMapping("/search")
    public ResponseEntity<ProductPageResponse> searchProducts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        ProductPageResponse response = productService.searchProducts(keyword, page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        ProductResponse response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request) {
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable String id,
            @Valid @RequestBody ProductRequest request) {

        ProductResponse response = productService.updateProduct(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> createProduct(
            @RequestPart("product") @Valid ProductRequest request,
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        String imageUrl = fileUploadService.uploadFile(imageFile);

        request.setImageUrl(imageUrl);

        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.ok(response);
    }
}