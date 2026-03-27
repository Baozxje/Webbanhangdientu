// dto/product/ProductRequest.java
package TTDH.Webbanhangdientu.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotNull(message = "Giá không được để trống")
    @Min(value = 0, message = "Giá phải lớn hơn hoặc bằng 0")
    private double price;

    private String description;
    private String categoryId;

    @Min(value = 0, message = "Số lượng tồn kho phải >= 0")
    private int stock;

    private String imageUrl;
}