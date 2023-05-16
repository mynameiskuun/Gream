package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.item.dto.ImgDto;
import com.project.gream.domain.item.dto.ItemRequestDto;
import com.project.gream.domain.item.dto.ItemVO;
import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberVO;
import com.project.gream.domain.member.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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

    @Override
    public String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberVO memberVo) {

        int qty = qtyIsValidForCart(cartItemDto) ? cartItemDto.getQuantity() : cartItemDto.getItemVo().getItemStock();

        cartItemDto.setQuantity(qty);

        isCartItemAlreadyExists(memberVo, cartItemDto);

        boolean isAlreadyExists = cartItemRepository.existsByCart_IdAndItem_Id(memberVo.getCartDto().getId(), cartItemDto.getItemVo().getId());
        Item item = itemRepository.findById(cartItemDto.getItemVo().getId()).orElseThrow();
        boolean qtyIsValidForCart = item.getItemStock() > cartItemDto.getQuantity();

        if(isAlreadyExists && qtyIsValidForCart) {

        }
        // item 테이블 수량 수정, save
        // cartitem 테이블 save
        // 처음 담기는 상품인지, 기존에 담았던 상품인지에 따라 분기처리.
        // 처음 담겼고, 상품 재고보다 적은 수량을 담았다면 그대로 save.
        // 기존에 담았던 상품의 경우
        // 기존 장바구니 상품 수량 + 새로 담는 상품 수량 > 상품 재고 -> 상품 재고 최대치로 저장
        // 기존 장바구니 상품 수량 + 새로 담는 상품 수량 < 상품 재고 -> 합친 수량 저장

        return null;
    }
    private boolean qtyIsValidForCart(CartItemDto cartItemDto) {
        Item item = itemRepository.findById(cartItemDto.getItemVo().getId()).orElseThrow();
        return item.getItemStock() > cartItemDto.getQuantity();
    }

    private boolean isCartItemAlreadyExists(MemberVO memberVo, CartItemDto cartItemDto) {
        return cartItemRepository.existsByCart_IdAndItem_Id(memberVo.getCartDto().getId(), cartItemDto.getItemVo().getId());
    }

}
