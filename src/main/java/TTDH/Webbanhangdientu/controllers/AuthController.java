// controllers/AuthController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.authentication.JwtUtil;
import TTDH.Webbanhangdientu.dto.auth.AuthResponse;
import TTDH.Webbanhangdientu.dto.auth.LoginRequest;
import TTDH.Webbanhangdientu.dto.auth.RefreshTokenRequest;
import TTDH.Webbanhangdientu.dto.auth.RegisterRequest;
import TTDH.Webbanhangdientu.dto.user.UserProfileResponse;   // ← Sửa import này
import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<UserProfileResponse> register(@Valid @RequestBody RegisterRequest request) {   // ← Sửa kiểu trả về
        UserProfileResponse userResponse = userService.register(request);
        return ResponseEntity.ok(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhone(), request.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new RuntimeException("Tên đăng nhập hoặc mật khẩu không đúng");
        }

        UserDetails userDetails = userService.loadUserByUsername(request.getPhone());
        String accessToken = jwtUtil.generateAccessToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);
        String role = userService.findByPhone(request.getPhone())
                .map(User::getRole)
                .orElse("USER");

        AuthResponse response = new AuthResponse(accessToken, refreshToken, request.getPhone(), role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("Refresh token không hợp lệ hoặc đã hết hạn");
        }

        String phone = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userService.loadUserByUsername(phone);

        String newAccessToken = jwtUtil.generateAccessToken(userDetails);

        String role = userService.findByPhone(phone)
                .map(User::getRole)
                .orElse("USER");

        AuthResponse response = new AuthResponse(newAccessToken, refreshToken, phone, role);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Đăng xuất thành công. Vui lòng xóa token ở phía client.");
    }
}