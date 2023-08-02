package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.*;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.order.dto.KakaoPayDto;
import com.project.gream.domain.order.dto.OrderHistoryDto;
import com.project.gream.domain.order.dto.OrderRequestDto;
import com.project.gream.domain.order.entity.OrderHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ItemService {
    List<ItemDto> selectAllItems();
    Page<ItemDto> selectMenItems(Pageable pageable);
    Page<ItemDto> selectWomenItems(Pageable pageable);
    List<ItemDto> sortItemByCategory(String sortBy);
    void registerItemAndImgs(ItemRequestDto itemRequestDto) throws Exception;
    void saveEntities(Item item, List<String> imgPaths) throws Exception;
    ItemDto getItemById(Long itemId);
    String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberDto memberDto);
    Optional<CartItem> getOldCartItem(MemberDto memberDto, CartItemDto cartItemDto);
    String deleteCartItem(List<Long> cartItemIds);
    String updateCartItemQuantity(CartItemRequestDto requestDto);
    List<CartItemDto> getCartItems(Long cartId);
    List<CartItemDto> getOrderItemsFromCart(String cartItemIds);
    void updateItemStock(KakaoPayDto kakaoPayDto, OrderHistory orderHistory);
    List<Long> getLikedItemIds(String memberId);
    List<ItemDto> getLikedItemListForMypage(List<Long> itemIds);
    Page<ItemDto> getLikedItemListByMemberId(String memberId, Pageable pabeable);
    boolean itemStockCheck(OrderRequestDto requestDto);

    List<ItemDto> sortItemByGenderAndCategory(String genderStr, String sortBy);

    //    List<ImgDto> getImgsByItemId(Long itemId);
    //    ItemDto selectItemById(Long itemId);
    //    void deleteImg(Long imgId);
    //    void deleteItem(Long itemId);
}
