// controllers/UserController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.dto.user.UserProfileResponse;
import TTDH.Webbanhangdientu.dto.user.UserProfileUpdateRequest;
import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy ID người dùng an toàn từ Token
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName(); // Token giờ chứa SĐT

        User user = userService.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng từ token"));

        return user.getId();
    }
    // User lấy thông tin profile của chính mình (Đã bỏ @RequestParam)
    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserProfileResponse> getProfile() {
        String userId = getCurrentUserId();
        UserProfileResponse profile = userService.getProfile(userId);
        return ResponseEntity.ok(profile);
    }

    // User cập nhật profile (Đã bỏ @RequestParam)
    @PutMapping("/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<UserProfileResponse> updateProfile(
            @Valid @RequestBody UserProfileUpdateRequest request) {
        String userId = getCurrentUserId();
        UserProfileResponse updated = userService.updateProfile(userId, request);
        return ResponseEntity.ok(updated);
    }

    // Admin: Lấy tất cả user
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}