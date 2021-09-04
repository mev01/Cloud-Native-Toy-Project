package com.web.curation.feed.like;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.curation.feed.Feed;
import com.web.curation.member.Member;

import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Feedlike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEEDLIKE_ID")
    private int feedlikeId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "FEED_ID")
    private Feed feed;

    @Column(name = "feed_like")
    private int feedLike;
}
