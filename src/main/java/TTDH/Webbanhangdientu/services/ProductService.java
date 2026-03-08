// src/main/java/TTDH/Webbanhangdientu/services/ProductService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.Product;
import TTDH.Webbanhangdientu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(String id) {
        productRepository.deleteById(id);
    }

    public List<Product> findByCategory(String categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}