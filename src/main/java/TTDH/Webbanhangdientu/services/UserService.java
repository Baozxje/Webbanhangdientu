// services/UserService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.dto.user.UserProfileResponse;
import TTDH.Webbanhangdientu.dto.user.UserProfileUpdateRequest;
import TTDH.Webbanhangdientu.dto.auth.RegisterRequest;
import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register
    public UserProfileResponse register(RegisterRequest request) {
        if (userRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new RuntimeException("SDT đã tồn tại");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress() != null ? request.getAddress() : "");
        user.setRole("USER");

        User savedUser = userRepository.save(user);
        return convertToProfileResponse(savedUser);
    }

    // Lấy thông tin profile
    public UserProfileResponse getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
        return convertToProfileResponse(user);
    }

    // Cập nhật profile
    public UserProfileResponse updateProfile(String userId, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getAddress() != null) user.setAddress(request.getAddress());
        if (request.getPhone() != null) user.setPhone(request.getPhone());

        User updatedUser = userRepository.save(user);
        return convertToProfileResponse(updatedUser);
    }

    // Admin: Lấy tất cả user
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Admin: Xóa user
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    // Convert Entity -> Profile Response
    private UserProfileResponse convertToProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        return response;
    }
    // Security
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + phone));

        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getPhone(),
                user.getPassword(),
                authorities
        );
    }
}