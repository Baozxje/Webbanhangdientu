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
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("Username đã tồn tại");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setAddress(request.getAddress() != null ? request.getAddress() : "");
        user.setPhone(request.getPhone() != null ? request.getPhone() : "");
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

    // Convert Entity -> Profile Response
    private UserProfileResponse convertToProfileResponse(User user) {
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setEmail(user.getEmail());
        response.setAddress(user.getAddress());
        response.setPhone(user.getPhone());
        response.setRole(user.getRole());
        return response;
    }

    // Security
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Collection<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + user.getRole())
        );

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}