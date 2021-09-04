package com.web.curation.feed.comment;

import com.web.curation.feed.Feed;
import com.web.curation.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
public class CommentDto {

    private int commentId;

    private int parentId;

    @NotNull
    private int feedId;

    @NotNull
    private int memberId;

    @NotNull
    private String contents;

    private int className;

    private int order;

    private LocalDateTime createDate;

    private List<CommentDto> coComments;
    private Member member;
    private Comment parent;
    private Feed feed;

}
