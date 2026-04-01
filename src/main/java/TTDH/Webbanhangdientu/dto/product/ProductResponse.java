// dto/product/ProductResponse.java
package TTDH.Webbanhangdientu.dto.product;
import TTDH.Webbanhangdientu.models.ProductVariant;
import java.util.List;
import java.util.Map;
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

    private List<String> colors;
    private Map<String, String> specs;
    private List<ProductVariant> variants;
}