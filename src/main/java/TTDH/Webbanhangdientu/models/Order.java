package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {
    @Id
    private String id;
    private String userId;
    private String fullName;
    private String phone;
    private String address;
    private String paymentMethod; // COD, BANKING
    private double totalAmount;
    private String status; // PENDING, SHIPPING, DELIVERED, CANCELLED
    private LocalDateTime createdAt;

    private List<OrderItem> items;

    @Data
    public static class OrderItem {
        private String productId;
        private String name;
        private int quantity;
        private double price; // gia tại thoi diem mua
        private String imageUrl;
    }
}