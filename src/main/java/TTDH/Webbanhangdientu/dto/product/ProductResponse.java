// dto/product/ProductResponse.java
package TTDH.Webbanhangdientu.dto.product;

import lombok.Data;

@Data
public class ProductResponse {
    private String id;
    private String name;
    private double price;
    private String description;
    private String categoryId;
    private int stock;
    private String imageUrl;
}