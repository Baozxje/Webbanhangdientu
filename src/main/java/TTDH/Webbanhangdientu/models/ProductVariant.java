// src/main/java/TTDH/Webbanhangdientu/models/ProductVariant.java
package TTDH.Webbanhangdientu.models;

import lombok.Data;

@Data
public class ProductVariant {
    private String name;
    private double price;
    private int stock;
}