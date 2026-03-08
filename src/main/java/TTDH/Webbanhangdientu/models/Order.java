package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {
    @Id
    private String id;
    private String userId;
    private List<OrderItem> items = new ArrayList<>();
    private double total;
    private String status; // e.g., "PENDING", "SHIPPED", "DELIVERED"
    private String address;
    private Date orderDate = new Date();
    private String couponCode;
}