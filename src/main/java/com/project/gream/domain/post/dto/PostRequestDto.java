package com.project.gream.domain.post.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {

    private Long id;
    private String searchPeriod;
    private String searchTarget;
    private String searchKeyWords;
    private String noticeTitle;
    private String noticeContent;
    private String postType;
    private String thumbnailUrl;

    @Builder
    public PostRequestDto(Long id, String searchPeriod, String searchTarget, String searchKeyWords, String noticeTitle, String noticeContent, String postType, String thumbnailUrl) {
        this.id = id;
        this.searchPeriod = searchPeriod;
        this.searchTarget = searchTarget;
        this.searchKeyWords = searchKeyWords;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.postType = postType;
        this.thumbnailUrl = thumbnailUrl;
    }
}
