package TTDH.Webbanhangdientu.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "coupons")
@Data
public class Coupon {
    @Id
    private String id;
    private String code;
    private double discountPercentage;
    private Date expiryDate;
    private boolean active = true;
}