// src/main/java/TTDH/Webbanhangdientu/controllers/CouponController.java
package TTDH.Webbanhangdientu.controllers;

import TTDH.Webbanhangdientu.models.Coupon;
import TTDH.Webbanhangdientu.services.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coupons")
@PreAuthorize("hasRole('ADMIN')")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @GetMapping
    public List<Coupon> getAllCoupons() {
        return couponService.findAll();
    }

    @PostMapping
    public Coupon createCoupon(@RequestBody Coupon coupon) {
        return couponService.save(coupon);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCoupon(@PathVariable String id) {
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }
}