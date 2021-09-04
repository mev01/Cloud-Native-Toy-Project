package com.web.curation.feed.comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.web.curation.member.Member;

public interface CommentDao extends JpaRepository<Comment, Integer> {
    @Query("select c from Comment c where c.feed.feedId = :feedId")
    List<Comment> findAllByFeed_Id(int feedId);

    int countByMember(Member member);
}
