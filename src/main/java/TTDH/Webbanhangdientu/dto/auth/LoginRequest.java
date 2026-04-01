// dto/auth/LoginRequest.java
package TTDH.Webbanhangdientu.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Phone không được để trống")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    private String password;
}