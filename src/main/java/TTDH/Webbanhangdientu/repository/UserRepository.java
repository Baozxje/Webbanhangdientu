// repository/UserRepository.java
package TTDH.Webbanhangdientu.repository;

import TTDH.Webbanhangdientu.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByPhone(String phone);
    Optional<User> findByEmail(String email);
}