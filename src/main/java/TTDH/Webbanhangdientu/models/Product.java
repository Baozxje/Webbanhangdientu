// src/main/java/TTDH/Webbanhangdientu/models/Product.java
package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "products")
@Data
public class Product {
    @Id
    private String id;
    private String name;
    private double price;
    private String description;
    private String categoryId;
    private int stock;
    private String imageUrl; // Optional for product image
}