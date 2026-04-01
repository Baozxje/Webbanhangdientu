package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String imageUrl;

    private List<String> colors = new ArrayList<>();
    private Map<String, String> specs = new HashMap<>();
    private List<ProductVariant> variants = new ArrayList<>();
}