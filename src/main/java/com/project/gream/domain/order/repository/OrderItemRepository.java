package com.project.gream.domain.order.repository;

import com.project.gream.common.enumlist.OrderState;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.order.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findTop4ByOrderHistory_Member_IdOrderByCreatedTimeDesc(String memberId);
    Page<OrderItem> findAllByOrderHistory_Member_Id(String memberId, Pageable pageable);
    List<OrderItem> findAllByStateAndOrderHistory_Member(OrderState state, Member member);

}

