// services/ProductService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.dto.product.ProductPageResponse;
import TTDH.Webbanhangdientu.dto.product.ProductRequest;
import TTDH.Webbanhangdientu.dto.product.ProductResponse;
import TTDH.Webbanhangdientu.models.Product;
import TTDH.Webbanhangdientu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // Lấy tất cả sản phẩm có phân trang
    public ProductPageResponse getAllProducts(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Product> productPage = productRepository.findAll(pageable);

        return convertToPageResponse(productPage);
    }

    // Tìm kiếm sản phẩm theo tên
    public ProductPageResponse searchProducts(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<Product> productPage = productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        return convertToPageResponse(productPage);
    }

    // Lấy sản phẩm theo category có phân trang
    // Lấy sản phẩm theo category có phân trang (tạm thời dùng list)
    public ProductPageResponse getProductsByCategory(String categoryId, int page, int size) {
        List<Product> products = productRepository.findByCategoryId(categoryId);

        // Tạm thời chuyển thành page đơn giản
        ProductPageResponse response = new ProductPageResponse();
        response.setContent(products.stream().map(this::convertToResponse).collect(Collectors.toList()));
        response.setPageNumber(page);
        response.setPageSize(size);
        response.setTotalElements(products.size());
        response.setTotalPages(1);
        response.setLast(true);
        response.setFirst(true);
        return response;
    }

    public ProductResponse getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
        return convertToResponse(product);
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = convertToEntity(request);
        Product saved = productRepository.save(product);
        return convertToResponse(saved);
    }

    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategoryId(request.getCategoryId());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());

        product.setColors(request.getColors());
        product.setSpecs(request.getSpecs());
        product.setVariants(request.getVariants());

        Product updated = productRepository.save(product);
        return convertToResponse(updated);
    }

    public void deleteProduct(String id) {
        productRepository.deleteById(id);
    }

    // ==================== Mapping ====================
    private Product convertToEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setDescription(request.getDescription());
        product.setCategoryId(request.getCategoryId());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());

        if (request.getColors() != null) product.setColors(request.getColors());
        if (request.getSpecs() != null) product.setSpecs(request.getSpecs());
        if (request.getVariants() != null) product.setVariants(request.getVariants());

        return product;
    }

    private ProductResponse convertToResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setPrice(product.getPrice());
        response.setDescription(product.getDescription());
        response.setCategoryId(product.getCategoryId());
        response.setStock(product.getStock());
        response.setImageUrl(product.getImageUrl());

        response.setColors(product.getColors());
        response.setSpecs(product.getSpecs());
        response.setVariants(product.getVariants());

        return response;
    }

    private ProductPageResponse convertToPageResponse(Page<Product> page) {
        ProductPageResponse response = new ProductPageResponse();
        response.setContent(page.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList()));
        response.setPageNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalElements(page.getTotalElements());
        response.setTotalPages(page.getTotalPages());
        response.setLast(page.isLast());
        response.setFirst(page.isFirst());
        return response;
    }
}