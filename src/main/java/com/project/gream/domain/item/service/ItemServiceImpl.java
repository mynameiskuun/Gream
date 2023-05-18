package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.domain.item.dto.ImgDto;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.member.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService{

    private final S3Config s3Config;
    private final ItemRepository itemRepository;
    private final ImgRepository imgRepository;
    private final CartItemRepository cartItemRepository;

    @Override
    public List<ItemVO> selectAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemVO::fromEntity).collect(Collectors.toList());
    }
    @Override
    public List<ItemVO> selectMenItems() {
        return itemRepository.findByGender(Gender.MAN).stream()
                .map(ItemVO::fromEntity).collect(Collectors.toList());
    }
    @Override
    public List<ItemVO> selectWomenItems() {
        return itemRepository.findByGender(Gender.WOMAN).stream()
                .map(ItemVO::fromEntity).collect(Collectors.toList());
    }

    @Override
    public ItemVO selectItemById(Long itemId) {
        return ItemVO.fromEntity(itemRepository.findById(itemId).orElseThrow());
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional
    @Override
    public void registerItemAndImgs(ItemRequestDto itemRequestDto) throws Exception {
        Item item = itemRepository.save(new ItemVO(itemRequestDto).toEntity());
        List<String> imgPaths = s3Config.imgUpload(item, itemRequestDto.getImgFiles()); // S3 업로드
        saveEntities(item, imgPaths); // DB 업로드
    }

    @Override
    public void saveEntities(Item item, List<String> imgPaths) {
//        item.getCreatedTime().
        String thumbnailImg = "";
        if (imgPaths.size() == 0) {
            thumbnailImg = "https://mynameiskuun-img-bucket.s3.ap-northeast-2.amazonaws.com/static/images/default_item_img.jpg";
        }
        thumbnailImg = imgPaths.get(0);
        item.updateThumbnailUrl(thumbnailImg);
        for(String imgPath : imgPaths) {
            imgRepository.save(Img.builder()
                        .url(imgPath)
                        .item(item)
                        .build());
        }
    }

    @Override
    public List<ImgDto> getImgsByItemId(Long itemId) {
        return imgRepository.findItemImgs(itemId).stream()
                .map(ImgDto::fromEntityForItem).collect(Collectors.toList());
    }

    public ItemVO getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return ItemVO.fromEntity(item);
    }

    @Override
    public void deleteImg(Long imgId) {

    }

    @Transactional
    @Override
    public String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberVO memberVo) {

        Optional<CartItem> oldCartItem = getOldCartItem(memberVo, cartItemDto);

        int oldItemQty = oldCartItem.map(CartItem::getQuantity).orElse(0);
        int newItemQty = cartItemDto.getQuantity();
        int itemStock = cartItemDto.getItemVo().getItemStock();

        if (itemStock <= 0) {
            return "품절된 상품입니다.";
        }
        if (oldCartItem.isEmpty()) {
            cartItemDto.setCartDto(memberVo.getCartDto());
            cartItemRepository.save(cartItemDto.toEntity());
            return "장바구니에 저장 되었습니다";
        } else if (oldItemQty + newItemQty > itemStock) {
            oldCartItem.get().updateCartItemQty(itemStock);
        } else {
            oldCartItem.get().updateCartItemQty(oldItemQty + newItemQty);
        }
            cartItemRepository.save(oldCartItem.get());
            return "장바구니에 저장 되었습니다";
    }

    public Optional<CartItem> getOldCartItem(MemberVO memberVo, CartItemDto cartItemDto) {
        return cartItemRepository.findByCart_IdAndItem_Id(memberVo.getCartDto().getId(), cartItemDto.getItemVo().getId());
    }

    @Override
    public String deleteCartItem(List<Long> cartItemIds) {

        if (cartItemIds.isEmpty()) {
            return "삭제할 상품이 없습니다";
        }
        for (Long itemId : cartItemIds) {
            cartItemRepository.deleteById(itemId);
        }
        return "삭제 완료";
    }

}
