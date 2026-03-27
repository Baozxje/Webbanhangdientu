// dto/cart/CartItemResponse.java
package TTDH.Webbanhangdientu.dto.cart;

import lombok.Data;

@Data
public class CartItemResponse {
    private String productId;
    private String productName;
    private double price;
    private int quantity;
    private double subtotal;
}