package com.web.curation.feed.like;

import com.web.curation.feed.Feed;
import com.web.curation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FeedlikeDao extends JpaRepository<Feedlike, Integer> {
    Optional<Feedlike> findFeedlikeByMemberAndFeed(Member member, Feed feed);

    List<Feedlike> findFeedlikeByFeed(Feed feed);

    List<Feedlike> findFeedlikeByMember(Member member);
}