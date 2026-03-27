// controllers/OrderController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.Order;
import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.services.OrderService;
import TTDH.Webbanhangdientu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    private String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin người dùng từ token"));
        return user.getId();
    }

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Order> placeOrder(@RequestBody Map<String, Object> request) {
        String userId = getCurrentUserId();
        String address = (String) request.get("address");
        String couponCode = (String) request.get("couponCode");

        if (address == null || address.trim().isEmpty()) {
            throw new RuntimeException("Thiếu thông tin địa chỉ giao hàng");
        }

        Order order = orderService.placeOrder(userId, address, couponCode);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<Order>> getMyOrders() {
        String userId = getCurrentUserId();
        List<Order> orders = orderService.getOrdersByUser(userId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        Order updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
}