// dto/user/UserProfileUpdateRequest.java
package TTDH.Webbanhangdientu.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserProfileUpdateRequest {

    @Email(message = "Email không hợp lệ")
    private String email;

    @Size(max = 200, message = "Địa chỉ tối đa 200 ký tự")
    private String address;

    @Size(max = 15, message = "Số điện thoại tối đa 15 ký tự")
    private String phone;
}