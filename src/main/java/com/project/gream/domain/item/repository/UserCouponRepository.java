package com.project.gream.domain.item.repository;

import com.project.gream.domain.item.entity.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

    boolean existsByMember_IdAndCouponId(String memberId, Long CouponId);
    List<UserCoupon> getByMember_Id(String memberId);
    List<UserCoupon> getTop4ByMember_IdOrderByCreatedTimeAsc(String memberId);
}
