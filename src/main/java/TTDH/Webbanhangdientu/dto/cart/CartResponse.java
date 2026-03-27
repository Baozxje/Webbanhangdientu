// dto/cart/CartResponse.java
package TTDH.Webbanhangdientu.dto.cart;

import lombok.Data;
import java.util.List;

@Data
public class CartResponse {
    private String id;
    private String userId;
    private List<CartItemResponse> items;
    private double totalAmount;
}