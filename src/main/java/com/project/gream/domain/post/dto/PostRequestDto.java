package com.project.gream.domain.post.dto;

import com.project.gream.common.enumlist.QnaType;
import com.project.gream.domain.item.dto.ItemDto;
import com.project.gream.domain.member.dto.MemberDto;
import lombok.*;

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
    public PostRequestDto(Long id, String searchPeriod, String searchTarget, String searchKeyWords,
                          String noticeTitle, String noticeContent, String postType, String thumbnailUrl) {
        this.id = id;
        this.searchPeriod = searchPeriod;
        this.searchTarget = searchTarget;
        this.searchKeyWords = searchKeyWords;
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.postType = postType;
        this.thumbnailUrl = thumbnailUrl;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class QnaRequestDto {
        private String qnaType;
        private String qnaTitle;
        private String qnaContent;
        private ItemDto itemDto;
        private MemberDto memberDto;

        @Builder
        public QnaRequestDto(String qnaType, String qnaTitle, String qnaContent, ItemDto itemDto, MemberDto memberDto) {
            this.qnaType = qnaType;
            this.qnaTitle = qnaTitle;
            this.qnaContent = qnaContent;
            this.itemDto = itemDto;
            this.memberDto = memberDto;
        }
    }

}
