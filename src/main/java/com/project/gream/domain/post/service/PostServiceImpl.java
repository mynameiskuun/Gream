package com.project.gream.domain.post.service;

import com.project.gream.common.annotation.LoginMember;
import com.project.gream.common.auth.dto.CustomUserDetails;
import com.project.gream.common.config.S3Config;
import com.project.gream.common.enumlist.*;
import com.project.gream.common.util.StringToEnumUtil;
import com.project.gream.domain.item.entity.Img;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.item.repository.ImgRepository;
import com.project.gream.domain.item.repository.ItemRepository;
import com.project.gream.domain.member.dto.MemberDto;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.order.entity.OrderItem;
import com.project.gream.domain.post.dto.*;
import com.project.gream.domain.post.entity.Comment;
import com.project.gream.domain.post.entity.Likes;
import com.project.gream.domain.post.entity.Post;
import com.project.gream.domain.post.entity.Review;
import com.project.gream.domain.post.repository.CommentRepository;
import com.project.gream.domain.post.repository.LikesRepository;
import com.project.gream.domain.post.repository.PostRepository;
import com.project.gream.domain.post.repository.ReviewRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.project.gream.domain.post.entity.QLikes.likes;
import static com.project.gream.domain.post.entity.QPost.post;
import static com.project.gream.domain.post.entity.QComment.comment;
import static com.project.gream.domain.order.entity.QOrderItem.orderItem;



@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImpl implements PostService{

    private final ImgRepository imgRepository;
    private final ReviewRepository reviewRepository;
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final JPAQueryFactory queryFactory;
    private final CommentRepository commentRepository;
    private final S3Config s3Config;

    @Transactional
    @Override
    public void saveReview(ReviewDto reviewDto, List<String> imgPaths) {

        log.info("---------------------------- 리뷰 및 리뷰 이미지 DB 저장");

        Item reviewTargetItem = reviewDto.getItemDto().toEntity();
        Member member = reviewDto.toEntity().getMember();

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

        OrderItem orderedItem = queryFactory.selectFrom(orderItem)
                .where(orderItem.item.eq(reviewTargetItem),
                        orderItem.orderHistory.member.eq(member))
                .fetchOne();
        orderedItem.updateState(OrderState.REVIEWED);
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
    public List<ReviewDto> getReviewListByItemId(Long itemId) {
        List<Review> reviews = Optional.ofNullable(reviewRepository.findAllByItem_Id(itemId))
                .orElseThrow(() -> new NoSuchElementException(String.format("itemId [%s]의 리뷰는 존재하지 않습니다", itemId)));

        if (reviews == null) {
            return Collections.emptyList();
        }

        // 리뷰 id로 commentRepository를 탐색한다.
        // targetId를 해당 리뷰 id로 갖는 모든 코멘트를 가져와서 reviewDto에 담는다.


        List<ReviewDto> reviewDtoList = reviews.stream()
                .map(ReviewDto::fromEntity)
                .collect(Collectors.toList());

        reviewDtoList.forEach(this::addCommentListToReview);
        return reviewDtoList;
    }

    private ReviewDto addCommentListToReview(ReviewDto reviewDto) {
        List<CommentDto.Response> responseList = commentRepository.getReviewComments(reviewDto.getId()).stream()
                .map(CommentDto::fromEntity)
                .map(CommentDto.Response::new)
                .collect(Collectors.toList());
        for (CommentDto.Response response : responseList) {
            long likeCount = likesRepository.countByComment_Id(response.getId());
            response.setLikeCount(likeCount);
        }
        reviewDto.setCommentList(responseList);

        return reviewDto;
    }
    @Override
    public Likes isAlreadyLiked(LikesVO likesVO) {

        // 어떤 fk가 들어있는지 확인하고, 해당 fk에 따라 다른 repository에서 탐색

        log.info("-------------------------- 회원이 이미 좋아한 게시물인지 확인");

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

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

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
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
                        .postType(PostType.NOTICE)
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

        Long size = (long) noticeImgs.size();
        log.info("size : " + size);

        log.info(String.valueOf(noticeImgs.isEmpty()));


        if (noticeImgs.size() != 0) {
            List<String> imgUrlList = s3Config.imgUpload(PostRequestDto.class, noticeImgs);
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

    @Override
    public String deleteNotice(final Long noticeId, CustomUserDetails user) {

        String result = "삭제 완료";

        if (!isRealAdmin(user)) {
            result = "권한이 없습니다";
            return result;
        }
        postRepository.deleteById(noticeId);
        return result;
    }

    private boolean isRealAdmin(CustomUserDetails user) {
        log.info("authorities : " + user.getAuthorities().toString());
        return user.getRole().equals(Role.ADMIN);
    }

    @Transactional
    @Override
    public String saveQna(PostRequestDto.QnaRequestDto postQnaDto,
                          List<MultipartFile> qnaImgs,
                          @LoginMember MemberDto memberDto) throws Exception {

        log.info("------------------------- QNA save");

        if (postQnaDto == null) {
            return "문의사항 접수에 실패했습니다.";
        }

        Member member = memberRepository.findById(memberDto.getId()).orElseThrow();
        QnaType qnaType = StringToEnumUtil.getEnumFromValue(QnaType.class, postQnaDto.getQnaType());

        Post post = Post.builder()
                .title(postQnaDto.getQnaTitle())
                .content(postQnaDto.getQnaContent())
                .qnaType(qnaType)
                .postType(PostType.QNA)
                .item(postQnaDto.getItemDto().toEntity())
                .member(member)
                .build();

        Post qna = postRepository.save(post);
        List<String> imgUrlList = this.saveQnaImagesToS3Bucket(PostRequestDto.QnaRequestDto.class, qnaImgs);
        this.saveQnaImgUrlToDB(qna, imgUrlList, postQnaDto.getItemDto().getId());

        return "문의사항 접수가 완료되었습니다.";
    }

    private <T> List<String> saveQnaImagesToS3Bucket(Class<T> dto, List<MultipartFile> imgList) throws Exception {

        log.info("------------------------- QnA img upload to S3");

        if (imgList.size() == 0) {
            return Collections.emptyList();
        } else {
            return s3Config.imgUpload(dto, imgList);
        }
    }

    private void saveQnaImgUrlToDB(Post post, List<String> imgUrlList, Long itemId) {

        log.info("------------------------- QNA img url save to DB");

        Item item = itemRepository.getReferenceById(itemId);
        if (imgUrlList.isEmpty()) {
            return;
        }

        for (String url : imgUrlList) {
            Img img = Img.builder()
                    .item(item)
                    .url(url)
                    .post(post)
                    .build();
            imgRepository.save(img);
        }
    }

    @Override
    public Page<Post> getQnaListByItemId(Long itemId, Pageable pageable) {

        log.info("------------------------- Get All Qna List");

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        QueryResults<Post> results = queryFactory.selectFrom(post)
                .where(
                        post.item.id.eq(itemId),
                        post.postType.eq(PostType.QNA)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Post> content = results.getResults();
        Long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public PostResponseDto getQnaDetail(Long qnaId) {
//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Post qna = queryFactory.selectFrom(post)
                .where(post.id.eq(qnaId))
                .fetchOne();

        return PostResponseDto.builder()
                .postDto(PostDto.fromEntity(qna))
                .build();
    }

    @Override
    public Page<Post> getQnaListByMemberId(String memberId, PostType postType, Pageable pageable) {

        return postRepository.findAllByMember_IdAndPostTypeOrderByCreatedTimeDesc(memberId, PostType.QNA, pageable);
    }

    @Override
    public List<PostDto> getQnaListForMyPage(String memberId) {

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        return queryFactory.selectFrom(post)
                .where(post.member.id.eq(memberId),
                        post.postType.eq(PostType.QNA))
                .limit(4)
                .fetch()
                .stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PostDto> searchNoticeByCondition(PostRequestDto requestDto, Pageable pageable) {

        String searchPeriod = requestDto.getSearchPeriod();
        String searchTarget = requestDto.getSearchTarget();
        String searchKeyWords = requestDto.getSearchKeyWords();

        log.info("searchPeriod : " + searchPeriod);
        log.info("searchTarget : " + searchTarget);
        log.info("searchKeyWords : " + searchKeyWords);

//        QueryResults<Post> searchResults = queryFactory.selectFrom(post)
//                .where(eqPeriod(searchPeriod),
//                        eqTarget(searchTarget, searchKeyWords))
//                .orderBy(post.createdTime.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetchResults();

        List<PostDto> postList =  queryFactory.selectFrom(post)
                .where(eqPeriod(searchPeriod),
                        eqTarget(searchTarget, searchKeyWords),
                        post.postType.eq(PostType.NOTICE))
                .orderBy(post.createdTime.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch().stream()
                .map(PostDto::fromEntity)
                .collect(Collectors.toList());

        return new PageImpl<>(postList, pageable, postList.size());
    }

    private BooleanExpression eqPeriod(String searchPeriod) {
        if (StringUtils.isEmpty(searchPeriod)) {
            return null;
        }
        if (StringUtils.equals(searchPeriod, "ALL")) {
            return null;
        }
        if (StringUtils.equals(searchPeriod, "LAST_SIX_MONTH")) {
            return post.createdTime.after(LocalDateTime.now().minusMonths(6));
        }
        if (StringUtils.equals(searchPeriod, "LAST_ONE_YEAR")) {
            return post.createdTime.after(LocalDateTime.now().minusYears(1));
        }
        return null;
    }

    private BooleanExpression eqTarget(String searchTarget, String searchKeyWord) {
        if (StringUtils.isEmpty(searchTarget)) {
            return null;
        }
        if (StringUtils.equals(searchTarget, "TITLE_AND_CONTENT")) {
            return post.content.like(searchKeyWord).or(post.title.like(searchKeyWord));
        }
        if (StringUtils.equals(searchTarget, "WRITER")) {
            return post.member.id.like(searchKeyWord);
        }
        return null;
    }

    @Override
    public CommentDto.Response saveReviewComment(CommentDto.Request request) {
        Review review = reviewRepository.findById(request.getReviewId()).orElseThrow();
        Comment comment = Comment.builder()
                .content(request.getContent())
                .depth(0)
                .review(review)
                .member(request.getMemberDto().toEntity())
                .build();

        commentRepository.save(comment);
        return CommentDto.Response.builder()
                .message("댓글 등록에 성공했습니다.")
                .build();
    }
}
