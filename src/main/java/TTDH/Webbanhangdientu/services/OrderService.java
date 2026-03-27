// services/OrderService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.*;
import TTDH.Webbanhangdientu.repository.CartRepository;
import TTDH.Webbanhangdientu.repository.OrderRepository;
import TTDH.Webbanhangdientu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CouponService couponService;   // Nếu có coupon

    public Order placeOrder(String userId, String address, String couponCode) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng trống"));

        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng đang trống");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setAddress(address);
        order.setStatus("PENDING");
        order.setOrderDate(new Date());

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (CartItem cartItem : cart.getItems()) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + product.getName() + " không đủ hàng");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(product.getPrice());

            orderItems.add(orderItem);
            totalAmount += product.getPrice() * cartItem.getQuantity();

            // Giảm stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);
        }

        // Áp dụng coupon nếu có
        if (couponCode != null && !couponCode.isEmpty()) {
            try {
                Coupon coupon = couponService.validateCoupon(couponCode);
                double discount = totalAmount * (coupon.getDiscountPercentage() / 100);
                totalAmount -= discount;
                order.setCouponCode(couponCode);
            } catch (Exception e) {
                // Coupon không hợp lệ thì bỏ qua, không throw lỗi
                System.out.println("Coupon không hợp lệ: " + e.getMessage());
            }
        }

        order.setItems(orderItems);
        order.setTotal(totalAmount);

        Order savedOrder = orderRepository.save(order);

        // Xóa giỏ hàng sau khi đặt hàng thành công
        cart.getItems().clear();
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Optional<Order> getOrderById(String id) {
        return orderRepository.findById(id);
    }

    // Dành cho Admin
    public Order updateOrderStatus(String id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));

        order.setStatus(status);
        return orderRepository.save(order);
    }
}