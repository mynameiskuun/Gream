package com.project.gream.domain.item.repository;

import com.project.gream.domain.item.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponBoxRepository extends JpaRepository<UserCoupon, Long> {
}
