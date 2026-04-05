package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.Order;
import TTDH.Webbanhangdientu.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    // Khách hàng đặt hàng
    @PostMapping
    public Order placeOrder(@RequestBody Order order) {
        return orderService.createOrder(order);
    }

    // Khách hàng xem lịch sử đơn hàng của mình
    @GetMapping("/my-orders/{userId}")
    public List<Order> getMyOrders(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    // Admin xem tất cả đơn hàng
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAllOrdersForAdmin() {
        return orderService.getAllOrders();
    }

    // Admin cập nhat trang thai don hang
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public Order updateStatus(@PathVariable String id, @RequestParam String status) {
        return orderService.updateStatus(id, status);
    }
}