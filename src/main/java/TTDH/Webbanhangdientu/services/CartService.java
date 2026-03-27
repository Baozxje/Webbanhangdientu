// services/CartService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.dto.cart.CartItemResponse;
import TTDH.Webbanhangdientu.dto.cart.CartResponse;
import TTDH.Webbanhangdientu.models.Cart;
import TTDH.Webbanhangdientu.models.CartItem;
import TTDH.Webbanhangdientu.models.Product;
import TTDH.Webbanhangdientu.repository.CartRepository;
import TTDH.Webbanhangdientu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        return convertToCartResponse(cart);
    }

    public CartResponse addItemToCart(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Số lượng sản phẩm không đủ");
        }

        // Kiểm tra item đã tồn tại chưa
        CartItem existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            CartItem newItem = new CartItem();
            newItem.setProductId(productId);
            newItem.setQuantity(quantity);
            cart.getItems().add(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        return convertToCartResponse(savedCart);
    }

    public CartResponse updateCartItemQuantity(String userId, String productId, int quantity) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Sản phẩm không có trong giỏ hàng"));

        if (quantity <= 0) {
            cart.getItems().remove(item);
        } else {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm"));
            if (product.getStock() < quantity) {
                throw new RuntimeException("Số lượng sản phẩm không đủ");
            }
            item.setQuantity(quantity);
        }

        Cart savedCart = cartRepository.save(cart);
        return convertToCartResponse(savedCart);
    }

    public CartResponse removeItemFromCart(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại"));

        cart.getItems().removeIf(item -> item.getProductId().equals(productId));

        Cart savedCart = cartRepository.save(cart);
        return convertToCartResponse(savedCart);
    }

    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart != null) {
            cart.getItems().clear();
            cartRepository.save(cart);
        }
    }

    // ==================== Convert Entity to DTO ====================
    private CartResponse convertToCartResponse(Cart cart) {
        CartResponse response = new CartResponse();
        response.setId(cart.getId());
        response.setUserId(cart.getUserId());

        List<CartItemResponse> itemResponses = new ArrayList<>();
        double total = 0.0;

        for (CartItem item : cart.getItems()) {
            Product product = productRepository.findById(item.getProductId()).orElse(null);
            if (product != null) {
                CartItemResponse itemResp = new CartItemResponse();
                itemResp.setProductId(item.getProductId());
                itemResp.setProductName(product.getName());
                itemResp.setPrice(product.getPrice());
                itemResp.setQuantity(item.getQuantity());
                itemResp.setSubtotal(product.getPrice() * item.getQuantity());
                itemResponses.add(itemResp);

                total += product.getPrice() * item.getQuantity();
            }
        }

        response.setItems(itemResponses);
        response.setTotalAmount(total);
        return response;
    }
}