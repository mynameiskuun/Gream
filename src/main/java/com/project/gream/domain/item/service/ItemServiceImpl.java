package com.project.gream.domain.item.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.Gender;
import com.project.gream.common.util.EnumValueUtil;
import com.project.gream.domain.item.dto.*;
import com.project.gream.domain.item.entity.*;
import com.project.gream.domain.item.repository.CouponRepository;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.item.repository.UserCouponRepository;
import com.project.gream.domain.member.dto.CartItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.CartItem;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.order.dto.KakaoPayDto;
import com.project.gream.domain.order.dto.OrderRequestDto;
import com.project.gream.domain.order.entity.OrderHistory;
import com.project.gream.domain.post.entity.QLikes;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class ItemServiceImpl implements ItemService{

    private final S3Config s3Config;
    private final ItemRepository itemRepository;
    private final ImgRepository imgRepository;
    private final CartItemRepository cartItemRepository;
    private final CouponRepository couponRepository;
    private final MemberRepository memberRepository;
    private final UserCouponRepository userCouponRepository;
    private final EntityManager em;

    @Override
    public List<ItemDto> selectAllItems() {
        return itemRepository.findAll().stream()
                .map(ItemDto::fromEntity).collect(Collectors.toList());
    }
    @Override
    public List<ItemDto> selectMenItems() {
        return itemRepository.findByGender(Gender.MAN).stream()
                .map(ItemDto::fromEntity).collect(Collectors.toList());
    }
    @Override
    public List<ItemDto> selectWomenItems() {
        return itemRepository.findByGender(Gender.WOMAN).stream()
                .map(ItemDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> sortItemByCategory(String sortBy) {

        log.info("------------------------------ 카테고리 별 상품 DB 탐색");

//        Category category = EnumSet.allOf(Category.class).stream()
//                .filter(v -> v.name().equals(sortBy))
//                .findAny()
//                .get();

        if (sortBy.equals("ALL")) {
            return itemRepository.findByGender(Gender.MAN).stream()
                    .map(ItemDto::fromEntity)
                    .collect(Collectors.toList());
        }

        Category category = EnumValueUtil.toEntityCode(Category.class, sortBy);

        return itemRepository.findAllByCategoryOrderByCreatedTime(category).stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public ItemDto selectItemById(Long itemId) {
        return ItemDto.fromEntity(itemRepository.findById(itemId).orElseThrow());
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }

    @Transactional
    @Override
    public void registerItemAndImgs(ItemRequestDto itemRequestDto) throws Exception {
        Item item = itemRepository.save(new ItemDto(itemRequestDto).toEntity());
        List<String> imgPaths = s3Config.imgUpload(itemRequestDto.getClass(), itemRequestDto.getImgFiles()); // S3 업로드
        this.saveEntities(item, imgPaths); // DB 업로드
    }

    @Override
    public void saveEntities(Item item, List<String> imgPaths) {

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

    public ItemDto getItemById(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return ItemDto.fromEntity(item);
    }

    @Override
    public void deleteImg(Long imgId) {

    }

    @Transactional
    @Override
    public String addItemToCart(CartItemDto cartItemDto, @LoginMember MemberDto memberDto) {

        Optional<CartItem> oldCartItem = getOldCartItem(memberDto, cartItemDto);

        int oldItemQty = oldCartItem.map(CartItem::getQuantity).orElse(0);
        int newItemQty = cartItemDto.getQuantity();
        int itemStock = cartItemDto.getItemDto().getItemStock();

        if (itemStock <= 0) {
            return "품절된 상품입니다.";
        }
        if (oldCartItem.isEmpty()) {
            cartItemDto.setCartDto(memberDto.getCartDto());
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

    public Optional<CartItem> getOldCartItem(MemberDto memberDto, CartItemDto cartItemDto) {
        return cartItemRepository.findByCart_IdAndItem_Id(memberDto.getCartDto().getId(), cartItemDto.getItemDto().getId());
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

    @Override
    public String updateCartItemQuantity(CartItemRequestDto requestDto) {

        log.info("changedQuantity : " + String.valueOf(requestDto.getChangedQuantity()));
        log.info("cartItemId : " + String.valueOf(requestDto.getCartItemId()));

        if (requestDto.getChangedQuantity() == 0 || requestDto.getCartItemId() == null) {
            return "변경 가능한 수량이 아닙니다";
        }

        Optional<CartItem> cartItem = cartItemRepository.findById(requestDto.getCartItemId());
        cartItem.get().updateCartItemQty(requestDto.getChangedQuantity());
        cartItemRepository.save(cartItem.get());
          return "수량 변경 완료";
    }

    @Override
    public List<CartItemDto> getCartItems(Long cartId) {
        return cartItemRepository.findAllByCart_Id(cartId).stream()
                .map(CartItemDto::fromEntity)
                .collect(Collectors.toList());
    }
    @Override
    public List<CartItemDto> getOrderItemsFromCart(String cartItemIds) {

        log.info("------------------------- 장바구니에서 가져온 주문 상품 출력");

        String[] itemIdArray = cartItemIds.split(",");
        List<Long> idArray = Arrays.stream(itemIdArray)
                .map(Long::parseLong)
                .collect(Collectors.toList());

         return idArray.stream()
                 .map(cartItemRepository::findById)
                 .map(Optional::orElseThrow)
                 .map(CartItemDto::fromEntity)
                 .collect(Collectors.toList());
    }

    @Override
    public void updateItemStock(KakaoPayDto kakaoPayDto, OrderHistory orderHistory) {

        log.info("--------------------------- 주문 완료 후 상품 재고 변경");

//        String[] itemArray = kakaoPayDto.getItemIds().split("/");
//        String[] qtyArray = kakaoPayDto.getItemQtys().split("/");

        List<Long> itemArray = kakaoPayDto.getItemIds();
        List<Integer> qtyArray = kakaoPayDto.getItemQtys();

//        for (int i = 0; i < itemArray.length; i++) {
//            Item item = itemRepository.findById(Long.valueOf(itemArray[i]))
//                    .orElseThrow(() -> new NoSuchElementException("상품 재고 갱신 중 오류가 발생 했습니다."));
//            item.updateItemStock(item.getItemStock() - Integer.parseInt(qtyArray[i]));
//            itemRepository.save(item); // 주문 수량만큼 재고 -
//        }

        for (int i = 0; i < itemArray.size(); i++) {
            Item item = itemRepository.findById(itemArray.get(i))
                    .orElseThrow(() -> new NoSuchElementException("상품 재고 갱신 중 오류가 발생 했습니다."));
            item.updateItemStock(item.getItemStock() - qtyArray.get(i));
            itemRepository.save(item); // 주문 수량만큼 재고 -
        }
    }

    @Override
    public List<Long> getLikedItemIds(String memberId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QLikes likes = QLikes.likes;

        return queryFactory
                .select(likes.item.id)
                .from(likes)
                .where(likes.member.id.eq(memberId))
                .fetch();
    }

    @Override
    public List<ItemDto> getLikedItemListForMypage(List<Long> itemIds) {
        return itemIds.stream()
                .map(itemRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(ItemDto::fromEntity)
                .limit(6)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getLikedItemListByMemberId(String memberId) {

        return itemRepository.findAllById(this.getLikedItemIds(memberId)).stream()
                .map(ItemDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean itemStockCheck(OrderRequestDto requestDto) {
        Item item = itemRepository.findById(requestDto.getItemDto().getId()).orElseThrow();
        return item.getItemStock() > 0;
    }

    @Override
    public String createCoupon(CouponRequestDto requestDto) {
        log.info("-------------------------- 쿠폰 생성");

        String discountFor = String.join(",", requestDto.getDiscountFor());
        try {
            Coupon coupon = Coupon.builder()
                    .type(requestDto.getType())
                    .name(requestDto.getName())
                    .discountRate(requestDto.getDiscountRate())
                    .expireDate(requestDto.getExpireDate())
                    .stock(requestDto.getStock())
                    .minOrderPrice(requestDto.getMinOrderPrice())
                    .discountFor(discountFor)
                    .build();
            couponRepository.save(coupon);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @Override
    public Page<CouponDto> getCouponList(Pageable pageable) {
        return couponRepository.findAll(pageable).map(Coupon -> CouponDto.builder()
                .id(Coupon.getId())
                .type(Coupon.getType())
                .name(Coupon.getName())
                .discountRate(Coupon.getDiscountRate())
                .expireDate(Coupon.getExpireDate())
                .stock(Coupon.getStock())
                .minOrderPrice(Coupon.getMinOrderPrice())
                .discountFor(Coupon.getDiscountFor())
                .createdTime(Coupon.getCreatedTime())
                .modifiedTime(Coupon.getModifiedTime())
                .build());
    }

    @Override
    public String deleteCoupon(Long couponId) {
        try {
            couponRepository.deleteById(couponId);
        } catch(Exception e) {
            e.printStackTrace();
            return "오류 발생";
        }
        return "쿠폰 삭제 완료.";
    }

    @Override
    public List<CouponDto> getUsableCouponList(Long itemId) {
        Item item = itemRepository.findById(itemId).orElseThrow();
        return couponRepository.findCouponsByDiscountForContaining(String.valueOf(item.getCategory())).stream()
                .map(CouponDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public String saveUsableCoupon(CouponRequestDto requestDto) {

        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow();
        String memberId = member.getId();
        List<Long> couponIdList = requestDto.getCouponIdList();

        if (memberId == null || couponIdList == null) {
            return "쿠폰 저장에 실패했습니다.";
        }

        List<Coupon> couponList = requestDto.getCouponIdList().stream()
                .filter(couponId -> !userCouponRepository.existsByMember_IdAndCouponId(memberId,couponId))
                .map(couponRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (Coupon coupon : couponList) {
            UserCoupon userCoupon = UserCoupon.builder()
                    .coupon(coupon)
                    .member(member)
                    .build();
            log.info(String.valueOf(userCoupon.getCoupon().getId()));
            userCouponRepository.save(userCoupon);
        }

        return "쿠폰 저장 완료(사용완료, 수령완료 쿠폰 제외)";
    }

    @Override
    public List<UserCouponResponseDto> getMemberCoupon(Member member) {

        List<UserCoupon> couponList = userCouponRepository.getByMember_Id(member.getId());
        return couponList.stream()
                .map(UserCouponVO::fromEntity)
                .map(UserCouponResponseDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserCouponResponseDto> getMemberCouponForMypage(String memberId) {

        return userCouponRepository.getTop4ByMember_IdOrderByCreatedTimeAsc(memberId).stream()
                .map(UserCouponVO::fromEntity)
                .map(UserCouponResponseDto::new)
                .collect(Collectors.toList());
    }


}
