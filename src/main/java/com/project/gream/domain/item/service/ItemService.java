package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.domain.item.dto.ImgDto;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.entity.CartItem;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    List<ItemVO> selectAllItems();
    List<ItemVO> selectMenItems();
    List<ItemVO> selectWomenItems();
    ItemVO selectItemById(Long itemId);
    void deleteItem(Long itemId);
//    void updateItemStock(KakaoPaymentDto kakaoPaymentDto, OrderHistory orderHistory, String itemQtys);
//    void registerItemAndImgs(ItemVO itemVO, List<MultipartFile> imgFiles) throws Exception;
    void registerItemAndImgs(ItemRequestDto itemRequestDto) throws Exception;
    void saveEntities(Item item, List<String> imgPaths) throws Exception;
    List<ImgDto> getImgsByItemId(Long itemId);
    ItemVO getItemById(Long itemId);
    void deleteImg(Long imgId);
    String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberVO memberVo);
    Optional<CartItem> getOldCartItem(MemberVO memberVo, CartItemDto cartItemDto);
    String deleteCartItem(List<Long> cartItemIds);
}
