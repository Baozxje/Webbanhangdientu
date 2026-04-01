// dto/user/UserProfileResponse.java
package TTDH.Webbanhangdientu.dto.user;

import lombok.Data;

@Data
public class UserProfileResponse {
    private String id;
    private String fullName;
    private String email;
    private String address;
    private String phone;
    private String role;
}