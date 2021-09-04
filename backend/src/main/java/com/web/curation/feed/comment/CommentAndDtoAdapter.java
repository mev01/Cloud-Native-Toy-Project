package com.web.curation.feed.comment;


import java.util.ArrayList;

public class CommentAndDtoAdapter {
    public static CommentDto entityToDto(Comment comment) {
        CommentDto commentDto = CommentDto.builder()
                .commentId(comment.getCommentId())
                .feedId(comment.getFeed().getFeedId())
                .member(comment.getMember())
                .contents(comment.getContents())
                .createDate(comment.getCreateDate())
                .coComments(new ArrayList<>())
                .build();
        if (comment.getParent() != null) {
            commentDto.setParentId(comment.getParent().getCommentId());
        }
        return commentDto;
    }

    public static Comment dtoToEntityCoComment(CommentDto commentDto) {
        return Comment.builder()
                .feed(commentDto.getFeed())
                .parent(commentDto.getParent())
                .member(commentDto.getMember())
                .contents(commentDto.getContents())
                .className(commentDto.getClassName())
                .order(commentDto.getOrder())
                .build();
    }

    public static Comment dtoToEntityComment(CommentDto commentDto) {
        return Comment.builder()
                .feed(commentDto.getFeed())
                .member(commentDto.getMember())
                .contents(commentDto.getContents())
                .className(commentDto.getClassName())
                .order(commentDto.getOrder())
                .build();
    }
}
