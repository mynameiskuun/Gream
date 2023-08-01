package com.project.gream.domain.post.entity;

import com.project.gream.common.enumlist.PostType;
import com.project.gream.common.enumlist.QnaType;
import com.project.gream.common.enumlist.converter.PostTypeConverter;
import com.project.gream.common.enumlist.converter.QnaTypeConverter;
import com.project.gream.common.util.BaseTimeEntity;
import com.project.gream.domain.item.entity.Item;
import com.project.gream.domain.member.entity.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post", indexes = {
        @Index(name = "idx_title_content_createdTime", columnList = "title, content, createdTime"),
        @Index(name = "idx_writer", columnList = "member_id")
})
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private String title;
    private String content;
    private int hits;
    @Convert(converter = QnaTypeConverter.class)
    private QnaType qnaType;
    @Convert(converter = PostTypeConverter.class)
    private PostType postType;
    private String thumbnailUrl;
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Post(Long id, String title, String content, int hits, QnaType qnaType, PostType postType, String thumbnailUrl, Item item, Member member) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.hits = hits;
        this.qnaType = qnaType;
        this.postType = postType;
        this.thumbnailUrl = thumbnailUrl;
        this.item = item;
        this.member = member;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    public void updateHits() {
        this.hits += 1;
    }
}
