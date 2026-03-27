// dto/product/ProductPageResponse.java
package TTDH.Webbanhangdientu.dto.product;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class ProductPageResponse {
    private List<ProductResponse> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
    private boolean first;
}