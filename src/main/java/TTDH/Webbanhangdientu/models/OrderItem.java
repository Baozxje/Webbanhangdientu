package TTDH.Webbanhangdientu.models;

import lombok.Data;

@Data
public class OrderItem {
    private String productId;
    private int quantity;
    private double priceAtPurchase;
}