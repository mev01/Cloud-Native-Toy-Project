package com.web.curation.feed.comment;

import com.web.curation.error.CustomException;
import com.web.curation.error.NotFoundDataException;
import com.web.curation.feed.FeedDao;
import com.web.curation.member.MemberDao;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.web.curation.error.ErrorCode.FEED_NOT_FOUND;
import static com.web.curation.error.ErrorCode.MEMBER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentDao commentDao;
    private final FeedDao feedDao;
    private final MemberDao memberDao;

    public List<CommentDto> getCommentOfFeed(int feedId) {
        List<Comment> commentList = commentDao.findAllByFeed_Id(feedId);
        List<CommentDto> parentComment = new ArrayList<>();
        List<CommentDto> childComment = new ArrayList<>();

        for (Comment comment : commentList) {
            if (comment.getParent() == null) {
                parentComment.add(CommentAndDtoAdapter.entityToDto(comment));
            } else {
                childComment.add(CommentAndDtoAdapter.entityToDto(comment));
            }
        }
        List<CommentDto> commentDtoList = new ArrayList<>();

        for (int i = 0; i < parentComment.size(); i++) {
            for (int j = 0; j < childComment.size(); j++) {
                if (childComment.get(j).getParentId() == parentComment.get(i).getCommentId()) {
                    parentComment.get(i).getCoComments().add(childComment.get(j));
                }
            }
            commentDtoList.add(parentComment.get(i));
        }
        return commentDtoList;
    }

    public CommentDto addCommentOfFeed(CommentDto commentDto) {
        feedDao.findById(commentDto.getFeedId()).orElseThrow(NotFoundDataException::new);

        commentDto.setFeed(feedDao.findById(commentDto.getFeedId()).orElseThrow(() -> new CustomException(FEED_NOT_FOUND)));
        commentDto.setMember(memberDao.findById(commentDto.getMemberId()).orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND)));

        if (commentDto.getParentId() != 0) {
            Comment comment = commentDao.findById(commentDto.getParentId()).orElseThrow(NotFoundDataException::new);
            commentDto.setParent(comment);
            return CommentAndDtoAdapter.entityToDto(
                    commentDao.save(CommentAndDtoAdapter.dtoToEntityCoComment(commentDto)));
        }

        log.info(CommentAndDtoAdapter.dtoToEntityComment(commentDto).getMember().toString());
        return CommentAndDtoAdapter.entityToDto(
                commentDao.save(CommentAndDtoAdapter.dtoToEntityCoComment(commentDto)));
    }

    public boolean removeComment(int commentId) {
        commentDao.findById(commentId).orElseThrow(NotFoundDataException::new);
        commentDao.deleteById(commentId);
        return true;
    }

}
