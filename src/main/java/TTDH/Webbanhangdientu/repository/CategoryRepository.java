package TTDH.Webbanhangdientu.repository;

import TTDH.Webbanhangdientu.models.Category;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String> {
}