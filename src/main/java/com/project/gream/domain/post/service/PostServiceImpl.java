package com.project.gream.domain.post.service;

import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.entity.QItem;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.post.dto.LikesDto;
import com.project.gream.domain.post.dto.LikesResponseDto;
import com.project.gream.domain.post.dto.ReviewDto;
import com.project.gream.domain.post.entity.Likes;
import com.project.gream.domain.post.entity.Review;
import com.project.gream.domain.post.repository.LikesRepository;
import com.project.gream.domain.post.repository.ReviewRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.gream.domain.post.entity.QLikes.likes;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{

    private final ImgRepository imgRepository;
    private final ReviewRepository reviewRepository;
    private final ItemRepository itemRepository;
    private final LikesRepository likesRepository;
    private final EntityManager em;

    @Transactional
    @Override
    public void saveReview(ReviewDto reviewDto, List<String> imgPaths) {

        log.info("---------------------------- 리뷰 및 리뷰 이미지 DB 저장");

        if(!imgPaths.isEmpty()) {
            reviewDto.setThumbnail(imgPaths.get(0));
        }

        Review review = reviewRepository.save(reviewDto.toEntity());
        for(String imgPath : imgPaths) {
            imgRepository.save(Img.builder()
                    .url(imgPath)
                    .review(review)
                    .build());
        }
    }

    @Override
    public Map<Integer, Integer> getReviewScoreByItemId(Long itemId) {

        log.info("----------------------- 상품 상세페이지 출력용 리뷰 별점수 데이터 가공");

        List<Integer> reviewScoreList = reviewRepository.getReviewScore(itemId);
        Map<Integer, Integer> starMap = new HashMap<>();

        int length = 0;
        for (int i = 1; i <= 5; i++) {
            int count = 0;
            for (int value : reviewScoreList) {
                if(value == i) {
                    count++;
                    length++;
                }
            }
            starMap.put(i, count);
            starMap.put(0, length);
        }
        return starMap;
    }

    @Override
    public List<ReviewDto> getReviewDtoById(Long itemId) {
        List<Review> reviews = reviewRepository.findAllById(itemId);

        if (reviews == null) {
            return Collections.emptyList();
        }

        return reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());
    }

//    @Override
//    public void saveReviewsForTest(@LoginMember MemberDto memberDto) {
//
//        Item item = itemRepository.findById(1L).orElseThrow();
//        for (int i = 0; i < 4; i++) {
//            Review review = Review.builder()
//                    .starValue(4)
//                    .priceScore(2)
//                    .qualityScore(2)
//                    .deliveryScore(2)
//                    .repurchaseScore(2)
//                    .content("content")
//                    .thumbnail("none")
//                    .member(memberDto.toEntity())
//                    .item(item)
//                    .build();
//            reviewRepository.save(review);
//        }
//        for (int i = 0; i < 4; i++) {
//            Review review = Review.builder()
//                    .starValue(5)
//                    .priceScore(2)
//                    .qualityScore(2)
//                    .deliveryScore(2)
//                    .repurchaseScore(2)
//                    .content("content")
//                    .thumbnail("none")
//                    .member(memberDto.toEntity())
//                    .item(item)
//                    .build();
//            reviewRepository.save(review);
//        }
//        for (int i = 0; i < 10; i++) {
//            int starValue = (int) (Math.random() * 5) + 1;
//
//            Review review = Review.builder()
//                    .starValue(starValue)
//                    .priceScore(2)
//                    .qualityScore(2)
//                    .deliveryScore(2)
//                    .repurchaseScore(2)
//                    .content("content")
//                    .thumbnail("none")
//                    .member(memberDto.toEntity())
//                    .item(item)
//                    .build();
//            reviewRepository.save(review);
//        }
//    }

    @Override
    public String validateLike(LikesDto likesDto) {
        Optional<Likes> existingLikes = likesRepository.getByTargetIdAndLikeTargetType(
                likesDto.getTargetId(), likesDto.getLikeTargetType());

        existingLikes.ifPresent(likes -> likesRepository.deleteById(likes.getId()));
        existingLikes.orElseGet(() -> likesRepository.save(new Likes()));
        return "성공";
    }

    @Override
    public Likes isAlreadyLiked(LikesDto likesDto) {

        log.info("-------------------------- 회원이 이미 좋아한 게시물인지 확인");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .selectFrom(likes)
                .where(likes.member.id.eq(likesDto.getMemberDto().getId()),
                        likes.targetId.eq(likesDto.getTargetId()),
                        likes.likeTargetType.eq(likesDto.getLikeTargetType()))
                .fetchOne();
    }

    @Transactional
    @Override
    public LikesResponseDto saveOrDeleteLike(LikesDto likesDto) {

        log.info("-------------------------- 좋아요 db 저장 or 삭제");

        Likes likes = isAlreadyLiked(likesDto);
        String backgroundColor;

        if (likes == null) {
            likesRepository.save(likesDto.toEntity());
            backgroundColor = "lightcoral";
        } else {
            likesRepository.delete(likes);
            backgroundColor = "white";
        }

        return new LikesResponseDto(likesRepository.count(), backgroundColor);
    }

}
