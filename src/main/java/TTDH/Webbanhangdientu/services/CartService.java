// src/main/java/TTDH/Webbanhangdientu/services/CartService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.Cart;
import TTDH.Webbanhangdientu.models.CartItem;
import TTDH.Webbanhangdientu.models.Product;
import TTDH.Webbanhangdientu.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductService productService;

    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setUserId(userId);
            return cartRepository.save(newCart);
        });
    }

    public Cart addItemToCart(String userId, String productId, int quantity) {
        Cart cart = getCartByUserId(userId);
        Product product = productService.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseGet(() -> {
                    CartItem newItem = new CartItem();
                    newItem.setProductId(productId);
                    cart.getItems().add(newItem);
                    return newItem;
                });

        item.setQuantity(item.getQuantity() + quantity);
        return cartRepository.save(cart);
    }

    public Cart removeItemFromCart(String userId, String productId) {
        Cart cart = getCartByUserId(userId);
        cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        return cartRepository.save(cart);
    }

    // Additional methods like update quantity, clear cart
}