package com.project.gream.domain.order.repository;

import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {

    List<OrderHistory> findAllByMember_Id(String memberId);

}
