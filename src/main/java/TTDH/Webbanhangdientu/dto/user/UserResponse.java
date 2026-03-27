// dto/user/UserResponse.java
package TTDH.Webbanhangdientu.dto.user;

import lombok.Data;

@Data
public class UserResponse {
    private String id;
    private String username;
    private String email;
    private String address;
    private String phone;
    private String role;
}