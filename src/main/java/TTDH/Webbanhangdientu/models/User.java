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

    private String password;

    @Indexed(unique = true, sparse = true)
    @Email(message = "Email hong dung dinh dang")
    private String email;

    private String role;
    private String address;

    @Indexed(unique = true)
    @NotBlank(message = "SDT hong duoc de trong")
    private String phone;

    private String fullName;
}