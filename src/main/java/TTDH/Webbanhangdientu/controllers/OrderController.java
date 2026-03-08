// src/main/java/TTDH/Webbanhangdientu/controllers/OrderController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.Order;
import TTDH.Webbanhangdientu.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public Order placeOrder(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String address = (String) request.get("address");
        String couponCode = (String) request.get("couponCode");
        return orderService.placeOrder(userId, address, couponCode);
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public List<Order> getUserOrders(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Order> getOrder(@PathVariable String id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(@PathVariable String id, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        return orderService.updateOrderStatus(id, status);
    }
}