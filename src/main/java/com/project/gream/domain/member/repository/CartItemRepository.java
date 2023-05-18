package com.project.gream.domain.member.repository;

import com.project.gream.domain.member.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    Optional<CartItem> findByCart_IdAndItem_Id(Long cartId, Long itemId);
//    @Query(value = "select i from CartItem i left join fetch i.cart where i.id = :cartId") // left join fetch 를 통해 N+1 문제 해결.
//    List<CartItem> findAllWithFetch(@Param("cartId") Long cartId);
    List<CartItem> findAllByCart_Id(Long cartId);
    Optional<CartItem> findByIdAndCartId(Long itemId, Long cartId);
    void deleteAllById(Long cartId);

}
