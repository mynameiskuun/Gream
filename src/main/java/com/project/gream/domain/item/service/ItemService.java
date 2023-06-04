package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.CartItemRequestDto;
import com.project.gream.domain.item.dto.ImgDto;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.order.dto.KakaoPayDto;
import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.post.dto.LikesDto;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemDto> selectAllItems();
    List<ItemDto> selectMenItems();
    List<ItemDto> selectWomenItems();
    ItemDto selectItemById(Long itemId);
    void deleteItem(Long itemId);
    void registerItemAndImgs(ItemRequestDto itemRequestDto) throws Exception;
    void saveEntities(Item item, List<String> imgPaths) throws Exception;
    List<ImgDto> getImgsByItemId(Long itemId);
    ItemDto getItemById(Long itemId);
    void deleteImg(Long imgId);
    String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberDto memberDto);
    Optional<CartItem> getOldCartItem(MemberDto memberDto, CartItemDto cartItemDto);
    String deleteCartItem(List<Long> cartItemIds);
    String updateCartItemQuantity(CartItemRequestDto requestDto);
    List<CartItemDto> getCartItems(Long cartId);
    List<CartItemDto> getOrderItemsFromCart(String cartItemIds);
    void updateItemStock(KakaoPayDto kakaoPayDto, OrderHistory orderHistory);

    List<Long> getLikedItemIds(String memberId);

    List<ItemDto> getLikedItemList(List<Long> itemIds);
}
