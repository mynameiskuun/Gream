package com.project.gream.domain.item.service;


import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.enumlist.Category;
import com.project.gream.common.enumlist.CouponStatus;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.item.dto.*;
import com.project.gream.domain.item.entity.Coupon;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.entity.UserCoupon;
import com.project.gream.domain.item.repository.CouponRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.item.repository.UserCouponRepository;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.CartItemRepository;
import com.project.gream.domain.member.repository.MemberRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManagerFactory;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.gream.domain.item.entity.QCoupon.coupon;
import static com.project.gream.domain.item.entity.QUserCoupon.userCoupon;
import static com.project.gream.domain.member.entity.QMember.member;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiscountServiceImpl implements DiscountService {

    private final CouponRepository couponRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final UserCouponRepository userCouponRepository;
    private final CartItemRepository cartItemRepository;
    private final JPAQueryFactory queryFactory;
    private final EntityManagerFactory emf;
    @Override
    public String createCoupon(CouponRequestDto requestDto) {
        log.info("-------------------------- 쿠폰 생성");

        String discountFor = String.join(", ", requestDto.getDiscountFor());
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

    @Transactional
    @Override
    public String saveUsableCoupon(CouponRequestDto requestDto) {

        Member member = memberRepository.findById(requestDto.getMemberId()).orElseThrow();
        String memberId = member.getId();
        List<Long> couponIdList = requestDto.getCouponIdList();

        if (memberId == null || couponIdList == null) {
            return "쿠폰 저장에 실패했습니다.";
        }

        List<Coupon> couponList = requestDto.getCouponIdList().stream()
                .filter(couponId -> !userCouponRepository.existsByMember_IdAndCouponId(memberId, couponId))
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

    @Override
    public Map<String, String> isPointUsable(int point, @LoginMember MemberDto memberDto) {

        // 멤버가 가진 포인트보다 적으면 key = false, message = ''
        // 많으면 (가진 포인트를 초과했으면) key = true, message = '사용 가능한 포인트는 memberDto.getPoint() P 입니다'
        Map<String, String> result = new HashMap<>();

        log.info("point : " + point);
        boolean isPointUsable = memberDto.getPoint() >= point;
        if (isPointUsable) {
            result.put("isPointUsable", "true");
        } else {
            result.put("isPointUsable", "false");
            result.put("message", "보유 포인트를 초과했습니다.");
        }
        return result;
    }

    @Transactional
    @Override
    public List<UserCouponResponseDto> getUserCouponForItem(String category, MemberDto memberDto) {

        boolean isInTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("트랜잭션 상태 : " + String.valueOf(isInTransaction));
        log.info("트랜잭션 이름 : " + TransactionSynchronizationManager.getCurrentTransactionName());

//        Category discountFor = StringToEnumUtil.getEnumFromValue(Category.class, category);
        return queryFactory.selectFrom(userCoupon)
                .join(userCoupon.coupon, coupon)
                .fetchJoin()
                .join(userCoupon.member, member)
                .fetchJoin()
                .where(userCoupon.member.id.eq(memberDto.getId()),
                        userCoupon.status.eq(CouponStatus.VALID),
                        userCoupon.coupon.discountFor.contains(category))
                .distinct()
                .fetch().stream()
                .map(UserCouponVO::fromEntity)
                .map(UserCouponResponseDto::new)
                .collect(Collectors.toList());

    }

    @Transactional
    @Override
    public UserCouponDto.CouponApplyResponse applyCoupon(UserCouponDto.CouponApplyRequest request) {

        boolean isInTransaction = TransactionSynchronizationManager.isActualTransactionActive();
        log.info("트랜잭션 상태 : " + String.valueOf(isInTransaction));
        log.info("트랜잭션 이름 : " + TransactionSynchronizationManager.getCurrentTransactionName());
        // 1. 해당 쿠폰 유효성 검사. (최저 요구 금액 만족하는지, 유효기간 만료되지 않았는지)
        // 2. 통과하면 해당 상품 할인금액 출력, 안되면 alert창에 해당 오류 출력
        // 통과 후 -> 최종 구매 완료되면 해당 usercoupon 아이디 status 바꾸기.

        Long userCouponId = request.getUserCouponId();

        UserCoupon userCoupon = userCouponRepository.findById(userCouponId).orElseThrow();
        Coupon coupon = userCoupon.getCoupon();
        Item item = getItemFromId(request);

        int discountAmount = getDiscountAmount(coupon.getDiscountRate(), item.getPrice());
        log.info("결과 discountAmount : " + discountAmount);

        if (coupon.isExpired()) {
            return UserCouponDto.CouponApplyResponse.builder()
                    .message("사용기한이 만료된 쿠폰입니다.")
                    .build();
        }

        if (isLessThanMinOrderPrice(coupon, item)) {
            return UserCouponDto.CouponApplyResponse.builder()
                    .message("이 쿠폰은 " + coupon.getMinOrderPrice() + "원 이상 구매 시 사용 가능합니다.")
                    .build();
        }

            return UserCouponDto.CouponApplyResponse.builder()
                    .singleItemPrice(item.getPrice())
                    .discountAmount(discountAmount)
                    .afterDiscountPrice(item.getPrice() - discountAmount)
                    .message(discountAmount + "원 할인이 적용 되었습니다.")
                    .build();

    }

    private boolean isLessThanMinOrderPrice(Coupon coupon, Item item) {
        log.info("minOrderPrice : " + coupon.getMinOrderPrice());
        log.info("itemPrice : " + item.getPrice());

        return coupon.getMinOrderPrice() >= item.getPrice();
    }

    private int getDiscountAmount(int discountRate, int itemPrice) {
        log.info("discountRate : " + discountRate);
        log.info("itemPrice : " + itemPrice);

        double discountAmount = discountRate > 100 ? discountRate : itemPrice * (discountRate / 100.0);
        log.info("discountAmount 연산 후 결과 : " + discountAmount);

        return (int) Math.round(discountAmount);
    }

    private Item getItemFromId(UserCouponDto.CouponApplyRequest request) {
        if (request.getCartItemId() == null) {
            return itemRepository.findById(request.getItemId()).orElseThrow();
        }
        return cartItemRepository.findById(request.getCartItemId()).get().getItem();
    }

    @Override
    public void updateUserCouponStatus(List<Long> usedCouponIds) {

        log.info("---------------------- 유저가 사용한 쿠폰 INVALID 처리.");

        if (usedCouponIds.isEmpty()) {
            return;
        }

        List<UserCoupon> userCouponList = Optional.ofNullable(userCouponRepository.findByIdIn(usedCouponIds))
                .orElse(Collections.emptyList());
        userCouponList.forEach(userCoupon -> userCoupon.setStatus(CouponStatus.INVALID));
    }
}
