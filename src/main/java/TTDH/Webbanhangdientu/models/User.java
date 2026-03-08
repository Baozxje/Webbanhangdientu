package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "users")
@Data
public class User {
    @Id

    private String id;

    @Indexed(unique = true)
    @NotBlank(message = "Username không được để trống")
    private String username;

    private String password;

    @Indexed(unique = true, sparse = true)
    @Email(message = "Email không đúng định dạng")
    private String email;

    private String role; // "ADMIN" or "USER"
    private String address;
    private String phone;
}