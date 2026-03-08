// src/main/java/TTDH/Webbanhangdientu/services/OrderService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.*;
import TTDH.Webbanhangdientu.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CouponService couponService;

    public Order placeOrder(String userId, String address, String couponCode) {
        Cart cart = cartService.getCartByUserId(userId);
        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(address);
        order.setStatus("PENDING");

        double total = 0;
        for (CartItem cartItem : cart.getItems()) {
            Product product = productService.findById(cartItem.getProductId()).orElseThrow();
            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());
            order.getItems().add(orderItem);
            total += product.getPrice() * cartItem.getQuantity();

            // Update stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productService.save(product);
        }

        if (couponCode != null) {
            Coupon coupon = couponService.validateCoupon(couponCode);
            total -= total * (coupon.getDiscountPercentage() / 100);
            order.setCouponCode(couponCode);
        }

        order.setTotal(total);
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cart.getItems().clear();
        cartService.cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    // Admin methods: update status, etc.
    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}