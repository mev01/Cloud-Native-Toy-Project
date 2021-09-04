package com.web.curation.feed;

public class FeedAdaptor {
    public static FeedDto entityToDto(Feed feed) {
        return FeedDto.builder()
                .feedId(feed.getFeedId())
                .contents(feed.getContents())
                .teamName(feed.getTeamName())
                .writer(feed.getWriter())
                .memberId(feed.getMember().getMemberId())
                .teamId(feed.getTeam().getTeamId())
                .build();
    }

    public static Feed dtoToEntity(FeedDto feedDto) {
        return Feed.builder()
                .feedId(feedDto.getFeedId())
                .contents(feedDto.getContents())
                .teamName(feedDto.getTeamName())
                .writer(feedDto.getWriter())
                .member(feedDto.getMember())
                .team(feedDto.getTeam())
                .build();
    }
}
