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
import com.project.gream.domain.order.repository.OrderItemRepository;
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
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import static com.project.gream.domain.order.entity.QOrderItem.orderItem;
import static com.project.gream.domain.item.entity.QItem.item;
import static com.project.gream.domain.post.entity.QReview.review;
import static com.project.gream.domain.post.entity.QComment.comment;




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
    private final OrderItemRepository orderItemRepository;
    private final S3Config s3Config;

    @Transactional
    @Override
    public PostResponseDto saveReview(ReviewDto reviewDto, List<String> imgPaths) {

        log.info("---------------------------- 리뷰 및 리뷰 이미지 DB 저장");

        Member member = reviewDto.toEntity().getMember();
        Review review = reviewRepository.save(reviewDto.toEntity());

        if(!imgPaths.isEmpty()) {
            review.setThumbnail(imgPaths.get(0));
            for(String imgPath : imgPaths) {
                imgRepository.save(Img.builder()
                        .url(imgPath)
                        .item(review.getItem())
                        .review(review)
                        .build());
            }
            member.addPoint(2000);
        }

        OrderItem orderedItem = queryFactory.selectFrom(orderItem)
                .where(orderItem.id.eq(reviewDto.getOrderItemId()),
                        orderItem.orderHistory.member.eq(member))
                .fetchOne();
        orderedItem.updateState(OrderState.REVIEWED);
        orderItemRepository.save(orderedItem);

        return new PostResponseDto().builder()
                .message("리뷰 작성이 완료되었습니다.")
                .itemDto(reviewDto.getItemDto())
                .reviewId(review.getId())
                .build();
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
    public Page<ReviewDto> getReviewListByItemId(Long itemId, Pageable pageable) {
        Page<Review> reviews = Optional.ofNullable(reviewRepository.findAllByItem_Id(itemId, pageable))
                .orElseThrow(() -> new NoSuchElementException(String.format("itemId [%s]의 리뷰는 존재하지 않습니다", itemId)));

        if (reviews == null) {
            return Page.empty();
        }

        // 리뷰 id로 commentRepository를 탐색한다.
        // targetId를 해당 리뷰 id로 갖는 모든 코멘트를 가져와서 reviewDto에 담는다.

//        Page<ReviewDto> reviewDtoList = reviews.stream()
//                .map(ReviewDto::fromEntity)
//                .map(dto -> {
//                    dto.setLikesCount(queryFactory.select(likes.count())
//                            .from(likes)
//                            .where(likes.review.id.eq(dto.getId()))
//                            .fetchOne());
//                    return dto;
//                })
//                .collect(Collectors.toList());
//        reviewDtoList.forEach(this::addCommentListToReview);
//        return reviewDtoList;

        Page<ReviewDto> list = reviews.map(review -> {
            ReviewDto dto = ReviewDto.fromEntity(review);
            dto.setLikesCount(queryFactory.select(likes.count())
                    .from(likes)
                    .where(likes.review.id.eq(dto.getId()))
                    .fetchOne());
            return dto;
        });

        list.forEach(this::addCommentListToReview);
        return list;
    }

    private ReviewDto addCommentListToReview(ReviewDto reviewDto) {
        List<CommentDto.Response> responseList = commentRepository.getReviewComments(reviewDto.getId()).stream()
                .map(CommentDto::fromEntity)
                .map(CommentDto.Response::new)
                .collect(Collectors.toList());
        for (CommentDto.Response response : responseList) {
            long likesCount = likesRepository.countByComment_Id(response.getId());
            response.setLikesCount(likesCount);
        }
        reviewDto.setCommentList(responseList);
        return reviewDto;
    }
    private Likes isAlreadyLiked(LikesDto.Request request) {

        // 어떤 fk가 들어있는지 확인하고, 해당 fk에 따라 다른 repository에서 탐색

        log.info("-------------------------- 회원이 이미 좋아한 게시물인지 확인");

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);

        Likes like = new Likes();

        if (request.getItemDto() != null) {
            like = queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(request.getMemberDto().getId()).and(likes.item.id.eq(request.getItemDto().getId())))
                    .fetchOne();
        } else if (request.getReviewId() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(request.getMemberDto().getId()).and(likes.review.id.eq(request.getReviewId())))
                    .fetchOne();
        } else if (request.getCommentId() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(request.getMemberDto().getId()).and(likes.comment.id.eq(request.getCommentId())))
                    .fetchOne();
        } else if (request.getPostDto() != null) {
            like =  queryFactory
                    .selectFrom(likes)
                    .where(likes.member.id.eq(request.getMemberDto().getId()).and(likes.post.id.eq(request.getPostDto().getId())))
                    .fetchOne();
        }
        return like;
    }

    @Transactional
    @Override
    public LikesResponseDto saveOrDeleteItemLike(LikesDto.Request request) {

        log.info("-------------------------- 좋아요 db 저장 or 삭제");
        Likes likes = this.isAlreadyLiked(request);
        String backgroundColor;

        if (likes == null) {
            if (request.getItemDto() != null) {
                likesRepository.save(Likes.builder()
                        .item(request.getItemDto().toEntity())
                        .member(request.getMemberDto().toEntity())
                        .build());
            } else if (request.getReviewId() != null) {
                Review review = reviewRepository.findById(request.getReviewId()).orElseThrow();
                Likes newLike = likesRepository.save(Likes.builder()
                        .review(review)
                        .member(request.getMemberDto().toEntity())
                        .build());
            } else if (request.getCommentId() != null) {
                Comment comment = commentRepository.findById(request.getCommentId()).orElseThrow();
                likesRepository.save(Likes.builder()
                        .comment(comment)
                        .member(request.getMemberDto().toEntity())
                        .build());
            } else if (request.getPostDto() != null) {
                likesRepository.save(Likes.builder()
//                        .post(likesVO.getPostDto().toEntity())
                        .member(request.getMemberDto().toEntity())
                        .build());
            }
            backgroundColor = "lightcoral";
        } else {
            likesRepository.delete(likes);
            backgroundColor = "white";
        }

        Long likesCount = this.getLikeCount(request);
        return new LikesResponseDto(likesCount, backgroundColor);
    }

    @Override
    public LikesResponseDto checkLike(Long itemId, @LoginMember MemberDto memberDto) {

        log.info("------------------------- 상품 상세페이지의 좋아요 갯수 반환");

//        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        Map<String, String> itemLikeBackgroundColorMap = new HashMap<>();
        Map<Long, Long> reviewLikeCountsMap = new HashMap<>();
//        Map<String, String> reviewLikeBackgroundColorMap = new HashMap<>();
        Map<Long, String> reviewLikeBgColorMapList = new HashMap<>();
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
                    reviewLikeBgColorMapList.put(reviewId, "lightcoral");
                } else {
                    reviewLikeBgColorMapList.put(reviewId, "white");
                }
            }
        }

        Map<Long, String> commentLikesBgColorMap = this.getCommentBgColorMap(itemId, memberDto.getId());
        return LikesResponseDto.builder()
                .itemLikeBackgroundColorMap(itemLikeBackgroundColorMap)
                .itemLikesCount(itemLikesCount)
//                .reviewLikeCountsMap(reviewLikeCountsMap)
                .reviewLikeBgColorMapList(reviewLikeBgColorMapList)
                .commentLikeBgColorMapList(commentLikesBgColorMap)
                .build();
    }

    private Map<Long, String> getCommentBgColorMap(Long itemId, String memberId) {

        Map<Long, String> commentLikeBgColorMap = new HashMap<>();
        List<Review> reviewList = reviewRepository.findAllByItem_Id(itemId);
        List<Long> commentIdList = reviewList.stream()
                .map(Review::getId)
                .map(commentRepository::findAllByReview_Id)
                .flatMap(List::stream)
                .map(Comment::getId)
                .collect(Collectors.toList());

        for (Long commentId : commentIdList) {
            if (likesRepository.existsByComment_IdAndMember_Id(commentId, memberId)) {
                commentLikeBgColorMap.put(commentId, "lightcoral");
            } else {
                commentLikeBgColorMap.put(commentId, "white");
            }
        }
        return commentLikeBgColorMap;
    }
    private Long getLikeCount(LikesDto.Request request) {

        Long likesCount = 0L;

        if (request.getItemDto() != null) {
            likesCount = likesRepository.countByItem_Id(request.getItemDto().getId());
        } else if (request.getReviewId() != null) {
            likesCount = likesRepository.countByReview_Id(request.getReviewId());
        } else if (request.getCommentId() != null) {
            likesCount = likesRepository.countByComment_Id(request.getCommentId());
        } else if (request.getPostDto() != null) {
            likesCount = likesRepository.countByPost_Id(request.getPostDto().getId());
        }
        return likesCount;
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
                        .createdTime(Post.getCreatedTime())
                        .modifiedTime(Post.getModifiedTime())
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

    @Transactional
    @Override
    public PostResponseDto getNoticeDetail(Long noticeId) {

        Post post =  postRepository.findById(noticeId).orElseThrow();
        post.updateHits();
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
    public PostResponseDto saveQna(PostRequestDto.QnaRequestDto postQnaDto,
                          List<MultipartFile> qnaImgs,
                          @LoginMember MemberDto memberDto) throws Exception {

        log.info("------------------------- QNA save");

        if (postQnaDto == null) {
            return new PostResponseDto().builder()
                    .message("문의 저장에 실패했습니다")
                    .build();
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

        return new PostResponseDto().builder()
                .message("문의 저장 완료되었습니다.")
                .build();
    }

    private <T> List<String> saveQnaImagesToS3Bucket(Class<T> dto, List<MultipartFile> imgList) throws Exception {

        log.info("------------------------- QnA img upload to S3");

        if (isImgListEmpty(imgList)) {
            return Collections.emptyList();
        } else {
            return s3Config.imgUpload(dto, imgList);
        }
    }

    private boolean isImgListEmpty(List<MultipartFile> imgList) {
        return imgList.get(0).getOriginalFilename().equals("");
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

        Comment responseComment = commentRepository.save(comment);
        return CommentDto.Response.builder()
                .id(responseComment.getId())
                .content(responseComment.getContent())
                .memberDto(MemberDto.fromEntity(responseComment.getMember()))
                .likesCount(0L)
                .depth(0)
                .message("댓글 등록에 성공했습니다.")
                .createdTime(responseComment.getCreatedTime())
                .modifiedTime(responseComment.getModifiedTime())
                .build();
    }

    @Transactional
    @Override
    public CommentDto.Response updateComment(CommentDto.Request request) {

        Comment comment = commentRepository.findById(request.getId()).orElseThrow();
        comment.updateContent(request.getModifyContent());
        commentRepository.saveAndFlush(comment);

        return new CommentDto.Response().builder()
                .id(comment.getId())
                .content(request.getModifyContent())
                .memberDto(MemberDto.fromEntity(comment.getMember()))
                .depth(comment.getDepth())
                .message("댓글 업데이트 완료")
                .createdTime(comment.getCreatedTime())
                .modifiedTime(LocalDateTime.now())
                .build();
    }

    @Transactional
    @Override
    public CommentDto.Response deleteComment(CommentDto.Request request) {

        Long commentId = request.getId();
        likesRepository.deleteAllByComment_Id(commentId);
        commentRepository.deleteById(request.getId());

        return new CommentDto.Response().builder()
                .id(request.getId())
                .message("댓글이 삭제 되었습니다.")
                .build();
    }
}
