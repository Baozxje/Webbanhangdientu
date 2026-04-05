package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.Order;
import TTDH.Webbanhangdientu.models.Product;
import TTDH.Webbanhangdientu.repository.OrderRepository;
import TTDH.Webbanhangdientu.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional // Đảm bảo nếu trừ kho lỗi thì không tạo đơn hàng
    public Order createOrder(Order order) {
        // 1. Kiểm tra và Trừ kho sản phẩm
        for (Order.OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại: " + item.getName()));

            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Sản phẩm " + item.getName() + " đã hết hàng hoặc không đủ số lượng!");
            }

            // Thực hiện trừ kho
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        // 2. Thiết lập thông tin đơn hàng
        order.setStatus("PENDING"); // Chờ xác nhận
        order.setCreatedAt(LocalDateTime.now());

        return orderRepository.save(order);
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    public Order updateStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng"));
        order.setStatus(status);
        return orderRepository.save(order);
    }
}