package com.project.gream.domain.member.repository;

import com.project.gream.domain.member.entity.Cart;
import com.project.gream.domain.member.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findAllByCart_Id(Long cartId);
    Optional<CartItem> findByCart_IdAndItem_Id(Long cartId, Long itemId);


}
