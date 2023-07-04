package com.project.gream.domain.post.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.PostType;
import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Likes;
import com.project.gream.domain.post.entity.Post;
import com.project.gream.domain.post.entity.Review;
import com.project.gream.domain.post.repository.LikesRepository;
import com.project.gream.domain.post.repository.PostRepository;
import com.project.gream.domain.post.repository.ReviewRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final EntityManager em;
    private final S3Config s3Config;

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

        // 어떤 fk가 들어있는지 확인하고, 해당 fk에 따라 다른 repository에서 탐색

        log.info("-------------------------- 회원이 이미 좋아한 게시물인지 확인");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Likes like = new Likes();

        if (likesVO.getItemDto() != null) {
            like = queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(likesVO.getMemberDto().getId()).and(likes.item.id.eq(likesVO.getItemDto().getId())))
                    .fetchOne();
        } else if (likesVO.getReviewDto() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(likesVO.getMemberDto().getId()).and(likes.review.id.eq(likesVO.getReviewDto().getId())))
                    .fetchOne();
        } else if (likesVO.getCommentDto() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(likesVO.getMemberDto().getId()).and(likes.comment.id.eq(likesVO.getCommentDto().getId())))
                    .fetchOne();
        } else if (likesVO.getPostDto() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(likesVO.getMemberDto().getId()).and(likes.post.id.eq(likesVO.getPostDto().getId())))
                    .fetchOne();
        }
        return like;
    }

    @Transactional
    @Override
    public LikesResponseDto saveOrDeleteItemLike(LikesVO likesVO) {

        log.info("-------------------------- 좋아요 db 저장 or 삭제");
        Likes likes = this.isAlreadyLiked(likesVO);
        String backgroundColor;

        if (likes == null) {
            if (likesVO.getItemDto() != null) {
                likesRepository.save(Likes.builder()
                        .item(likesVO.getItemDto().toEntity())
                        .member(likesVO.getMemberDto().toEntity())
                        .build());
            } else if (likesVO.getReviewDto() != null) {
                likesRepository.save(Likes.builder()
                        .review(likesVO.getReviewDto().toEntity())
                        .member(likesVO.getMemberDto().toEntity())
                        .build());
            } else if (likesVO.getCommentDto() != null) {
                likesRepository.save(Likes.builder()
//                        .comment(likesVO.getCommentDto().toEntity())
                        .member(likesVO.getMemberDto().toEntity())
                        .build());
            } else if (likesVO.getPostDto() != null) {
                likesRepository.save(Likes.builder()
//                        .post(likesVO.getPostDto().toEntity())
                        .member(likesVO.getMemberDto().toEntity())
                        .build());
            }
            backgroundColor = "lightcoral";
        } else {
            likesRepository.delete(likes);
            backgroundColor = "white";
        }

        Long likeCount = this.getLikeCount(likesVO);
        return new LikesResponseDto(likeCount, backgroundColor);
    }

    @Override
    public LikesResponseDto checkLike(Long itemId, @LoginMember MemberDto memberDto) {

        log.info("------------------------- 상품 상세페이지의 좋아요 갯수 반환");

        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Map<String, String> itemLikeBackgroundColorMap = new HashMap<>();
        Map<Long, Long> reviewLikeCountsMap = new HashMap<>();
        Map<String, String> reviewLikeBackgroundColorMap = new HashMap<>();

        // 상품 좋아요 갯수
        Long itemLikesCount = queryFactory.select(likes.count())
                                .from(likes)
                                .where(likes.item.id.eq(itemId))
                                .fetchOne();

        if (memberDto == null) {
            List<Long> reviewIdList = reviewRepository.findAllByItem_Id(itemId).stream()
                    .map(Review::getId)
                    .collect(Collectors.toList());

            Map<Long, Long> reviewLikesMap = new HashMap<>();

            for (Long reviewId : reviewIdList) {
            Long reviewLikeCount = queryFactory.select(likes.count())
                    .from(likes)
                    .where(likes.review.id.eq(reviewId))
                    .fetchOne();

            reviewLikesMap.put(reviewId, reviewLikeCount);
            }

            return LikesResponseDto.builder()
                    .itemLikesCount(itemLikesCount)
                    .reviewLikesMap(reviewLikesMap)
                    .build();
        } else {
            // ------------------ 상품 좋아요 map 구성

            if (likesRepository.existsByItem_IdAndMember_Id(itemId, memberDto.getId())) {
                itemLikeBackgroundColorMap.put(memberDto.getId(), "lightcoral");
            } else {
                itemLikeBackgroundColorMap.put(memberDto.getId(), "white");
            }

            // 리뷰 좋아요 갯수
            // 리뷰 아이디 / 좋아요 갯수 map으로 담아서 반환 후 출력.
            List<Long> reviewIdList = reviewRepository.findAllByItem_Id(itemId).stream()
                    .map(Review::getId)
                    .collect(Collectors.toList());

            for (Long reviewId : reviewIdList) {
//                Map<Long, Map<Long, String>> reviewLikesMap = new HashMap<>();
//                Map<Long, String> reviewDetailMap = new HashMap<>();

                Long reviewLikeCount = queryFactory.select(likes.count())
                        .from(likes)
                        .where(likes.review.id.eq(reviewId))
                        .fetchOne();

                reviewLikeCountsMap.put(reviewId, reviewLikeCount);

                if (likesRepository.existsByReview_IdAndMember_Id(reviewId, memberDto.getId())) {
                    reviewLikeBackgroundColorMap.put(memberDto.getId(), "lightcoral");
                } else {
                    reviewLikeBackgroundColorMap.put(memberDto.getId(), "white");
                }
            }
        }
        return LikesResponseDto.builder()
                .itemLikeBackgroundColorMap(itemLikeBackgroundColorMap)
                .itemLikesCount(itemLikesCount)
                .reviewLikeCountsMap(reviewLikeCountsMap)
                .reviewLikeBackgroundColorMap(reviewLikeBackgroundColorMap)
                .build();
    }

    private Long getLikeCount(LikesVO likesVO) {

        Long likeCount = 0L;

        if (likesVO.getItemDto() != null) {
            likeCount = likesRepository.countByItem_Id(likesVO.getItemDto().getId());
        } else if (likesVO.getReviewDto() != null) {
            likeCount = likesRepository.countByReview_Id(likesVO.getReviewDto().getId());
        } else if (likesVO.getCommentDto() != null) {
            likeCount = likesRepository.countByComment_Id(likesVO.getCommentDto().getId());
        } else if (likesVO.getPostDto() != null) {
            likeCount = likesRepository.countByPost_Id(likesVO.getPostDto().getId());
        }
        return likeCount;
    }

    @Override
    public Page<PostDto> getAllNoticePosts(Pageable pageable) {
        return postRepository.findAllByPostTypeOrderByCreatedTimeDesc(PostType.NOTICE, pageable)
                .map(Post -> PostDto.builder()
                        .id(Post.getId())
                        .title(Post.getTitle())
                        .content(Post.getContent())
                        .hits(Post.getHits())
                        .thumbnailUrl(Post.getThumbnailUrl())
                        .memberDto(MemberDto.fromEntity(Post.getMember()))
                        .build());
    }

    @Override
    public String saveNotice(PostRequestDto requestDto, List<MultipartFile> noticeImgs, MemberDto memberDto) throws Exception {

        if (!memberDto.getId().equals("admin")) {
            return "권한이 없습니다.";
        }
        Member member = memberRepository.findById(memberDto.getId()).orElseThrow();

        Post post = Post.builder()
                .title(requestDto.getNoticeTitle())
                .content(requestDto.getNoticeContent())
                .postType(PostType.NOTICE)
                .thumbnailUrl(requestDto.getThumbnailUrl())
                .member(member)
                .build();

        List<String> imgUrlList = s3Config.imgUpload(PostRequestDto.class, noticeImgs);

        if (!noticeImgs.isEmpty()) {
            post.setThumbnailUrl(imgUrlList.get(0));
        }

        postRepository.save(post);
        return "공지사항 등록 완료";
    }

    @Override
    public PostResponseDto getNoticeDetail(Long noticeId) {

        Post post =  postRepository.findById(noticeId).orElseThrow();
        List<String> imgUrlList = Optional.ofNullable(imgRepository.findAllByPost_Id(noticeId))
                .orElse(Collections.emptyList())
                .stream()
                .map(Img::getUrl)
                .collect(Collectors.toList());

        PostDto postDto = PostDto.fromEntity(post);
        return new PostResponseDto(imgUrlList, postDto);
    }
}
