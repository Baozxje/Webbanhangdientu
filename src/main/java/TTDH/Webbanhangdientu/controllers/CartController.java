// controllers/CartController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.dto.cart.CartResponse;
import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.services.CartService;
import TTDH.Webbanhangdientu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    // Hàm helper: Trích xuất username từ JWT Token và tìm userId tương ứng trong DB
    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phone = authentication.getName();

        User user = userService.findByPhone(phone)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng từ token"));

        return user.getId(); // Trả về ID thật trong MongoDB
    }

    @GetMapping
    public ResponseEntity<CartResponse> getMyCart() {
        String userId = getCurrentUserId();
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(@RequestBody Map<String, Object> request) {
        String userId = getCurrentUserId();
        String productId = (String) request.get("productId");
        int quantity = (int) request.get("quantity");

        CartResponse response = cartService.addItemToCart(userId, productId, quantity);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateQuantity(@RequestBody Map<String, Object> request) {
        String userId = getCurrentUserId();
        String productId = (String) request.get("productId");
        int quantity = (int) request.get("quantity");

        CartResponse response = cartService.updateCartItemQuantity(userId, productId, quantity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<CartResponse> removeFromCart(@RequestBody Map<String, Object> request) {
        String userId = getCurrentUserId();
        String productId = (String) request.get("productId");

        CartResponse response = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearCart() {
        String userId = getCurrentUserId();
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}