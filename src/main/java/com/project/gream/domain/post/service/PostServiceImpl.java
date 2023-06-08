package com.project.gream.domain.post.service;

import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.post.dto.LikesVO;
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
                    .item(review.getItem())
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

    @Override
    public Likes isAlreadyLiked(LikesVO likesVO) {

        log.info("-------------------------- 회원이 이미 좋아한 게시물인지 확인");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory
                .selectFrom(likes)
                .where(likes.member.id.eq(likesVO.getMemberDto().getId()),
                        likes.targetId.eq(likesVO.getTargetId()),
                        likes.likeTargetType.eq(likesVO.getLikeTargetType()))
                .fetchOne();
    }

    @Transactional
    @Override
    public LikesResponseDto saveOrDeleteLike(LikesVO likesVO) {

        log.info("-------------------------- 좋아요 db 저장 or 삭제");

        Likes likes = this.isAlreadyLiked(likesVO);
        String backgroundColor;

        if (likes == null) {
            likesRepository.save(likesVO.toEntity());
            backgroundColor = "lightcoral";
        } else {
            likesRepository.delete(likes);
            backgroundColor = "white";
        }

        int likeCount = likesRepository.countByTargetIdAndLikeTargetType(
                likesVO.getTargetId(), likesVO.getLikeTargetType());

        return new LikesResponseDto(likeCount, backgroundColor);
    }

//    @Override
//    public LikesResponseDto checkLike(LikesRequestDto req, @LoginMember MemberDto memberDto) {
//
//        // 로그인 안했다면 -> 해당 아이템, 리뷰, 댓글의 좋아요 count 수 만 검색해서 반환.
//        if (memberDto == null) {
//
//        }
//        // 로그인 했다면 -> 아이디 & itemId, reviewId, commentId 검색해서 결과 조회하고, count수 & bgcolor 반환.
//
//        likesRepository.
//    }

}
