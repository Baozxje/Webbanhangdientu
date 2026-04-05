// src/main/java/TTDH/Webbanhangdientu/controllers/AdminController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.User;
import TTDH.Webbanhangdientu.services.CategoryService;
import TTDH.Webbanhangdientu.services.OrderService;
import TTDH.Webbanhangdientu.services.ProductService;
import TTDH.Webbanhangdientu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import TTDH.Webbanhangdientu.models.Order;


@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderService orderService;

    // API lấy ds user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // API xoa user
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalProducts", productService.findAll().size());
        stats.put("totalCategories", categoryService.findAll().size());
        stats.put("totalUsers", userService.getAllUsers().size());

        List<Order> orders = orderService.getAllOrders();
        stats.put("totalOrders", orders.size());

        double revenue = orders.stream()
                .filter(o -> "DELIVERED".equals(o.getStatus()))
                .mapToDouble(Order::getTotalAmount)
                .sum();
        stats.put("totalRevenue", revenue);

        return stats;
    }

}