// dto/auth/RegisterRequest.java
package TTDH.Webbanhangdientu.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Ho ten hog dc de trong ")
    private String fullName;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @NotBlank(message = "Password không được để trống")
    private String password;

    @Email(message = "Email không hợp lệ")
    private String email;

    private String address;
}