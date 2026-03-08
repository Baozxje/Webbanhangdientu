// src/main/java/TTDH/Webbanhangdientu/controllers/CartController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.Cart;
import TTDH.Webbanhangdientu.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('USER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable String userId) {
        return cartService.getCartByUserId(userId);
    }

    @PostMapping("/add")
    public Cart addToCart(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String productId = (String) request.get("productId");
        int quantity = (int) request.get("quantity");
        return cartService.addItemToCart(userId, productId, quantity);
    }

    @PostMapping("/remove")
    public Cart removeFromCart(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String productId = (String) request.get("productId");
        return cartService.removeItemFromCart(userId, productId);
    }
}