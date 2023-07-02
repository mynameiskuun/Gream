package com.project.gream.domain.item.repository;

import com.project.gream.common.enumlist.Category;
import com.project.gream.domain.item.dto.CouponDto;
import com.project.gream.domain.item.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findCouponsByDiscountForContaining(String category);
}
