package com.project.gream.domain.member.repository;

import com.project.gream.domain.member.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    List<CartItem> findAllById(Long cartId);

    Optional<CartItem> findByIdAndCartId(Long itemId, Long cartId);

    void deleteAllById(Long cartId);
}
