// src/main/java/TTDH/Webbanhangdientu/services/CouponService.java
package TTDH.Webbanhangdientu.services;

import TTDH.Webbanhangdientu.models.Coupon;
import TTDH.Webbanhangdientu.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CouponService {
    @Autowired
    private CouponRepository couponRepository;

    public List<Coupon> findAll() {
        return couponRepository.findAll();
    }

    public Coupon save(Coupon coupon) {
        return couponRepository.save(coupon);
    }

    public void delete(String id) {
        couponRepository.deleteById(id);
    }

    public Coupon validateCoupon(String code) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Invalid coupon"));
        if (!coupon.isActive() || coupon.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Coupon expired or inactive");
        }
        return coupon;
    }
}