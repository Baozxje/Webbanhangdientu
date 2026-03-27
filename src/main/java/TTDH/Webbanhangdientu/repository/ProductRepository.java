// repository/ProductRepository.java
package TTDH.Webbanhangdientu.repository;

import TTDH.Webbanhangdientu.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {

    List<Product> findByCategoryId(String categoryId);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    @Query("{'name': {$regex: ?0, $options: 'i'}, 'categoryId': ?1}")
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String name, String categoryId, Pageable pageable);

    Page<Product> findByPriceBetween(Double minPrice, Double maxPrice, Pageable pageable);
}