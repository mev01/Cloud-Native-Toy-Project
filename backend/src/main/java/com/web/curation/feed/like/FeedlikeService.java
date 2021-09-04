package com.web.curation.feed.like;

import com.web.curation.error.CustomException;
import com.web.curation.error.NotFoundDataException;
import com.web.curation.feed.Feed;
import com.web.curation.feed.FeedDao;
import com.web.curation.member.Member;
import com.web.curation.member.MemberAdapter;
import com.web.curation.member.MemberDao;
import com.web.curation.member.MemberDto;
import com.web.curation.team.Team;
import com.web.curation.team.challenge.TeamChallenge;
import com.web.curation.team.challenge.TeamChallengeDao;
import com.web.curation.team.challenger.TeamChallenger;
import com.web.curation.team.challenger.TeamChallengerDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.web.curation.error.ErrorCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class FeedlikeService {
    private FeedlikeDao feedLikeDao;
    private FeedDao feedDao;
    private MemberDao memberDao;
    private TeamChallengeDao teamChallengeDao;
    private TeamChallengerDao teamChallengerDao;

    @Transactional
    public void likeFeed(FeedlikeDto feedlikeDto) {
        Member member = memberDao.findById(feedlikeDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Feed feed = feedDao.findById(feedlikeDto.getFeedId())
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));

        feedLikeDao.findFeedlikeByMemberAndFeed(member, feed)
                .ifPresent(feedlike -> {
                    throw new CustomException(FEEDLIKE_DUPLICATE_RESOURCE);
                });

        Feedlike feedlike = Feedlike.builder()
                .member(member)
                .feed(feed)
                .feedLike(feedlikeDto.getFeedLike())
                .build();
        feedLikeDao.saveAndFlush(feedlike);

        TeamChallenge teamChallenge = feed.getTeamchallenge();
        if (teamChallenge != null &&
                teamChallenge.getEndDate().isAfter(LocalDateTime.now()) &&
                checkTeamChallenge(feed)) {

            Member feedMember = feed.getMember();
            MemberDto memberDto = MemberAdapter.entityToDto(feedMember);
            memberDto.setPoint(feedMember.getPoint() + 1);
            memberDto.setCreateDate(feedMember.getCreateDate());
            memberDao.save(MemberAdapter.dtoToEntity(memberDto));

        }
    }

    private boolean checkTeamChallenge(Feed feed) {
        Optional<TeamChallenger> chkTeamChallenger = teamChallengerDao.findTeamChallengerByTeamChallengeAndMember(
                feed.getTeamchallenge(), feed.getMember());
        if (!chkTeamChallenger.isPresent()) {
            return false;
        }

        TeamChallenger teamChallenger = chkTeamChallenger.get();
        if (teamChallenger.isDone()) {
            return false;
        }

        List<Feedlike> feedlikes = feedLikeDao.findFeedlikeByFeed(feed);
        Team team = feed.getTeam();
        int memberCount = team.getMemberCount();
        log.info("멤버수 : " + memberCount + ", 피드 좋아요 수 :" + feedlikes.size());
        if (feedlikes.size() < (float) memberCount / 3)
            return false;
        teamChallenger.setDone(true);
        teamChallengerDao.save(teamChallenger);
        updateGoalCount(feed.getTeamchallenge());
        return true;
    }

    private void updateGoalCount(TeamChallenge teamChallenge) {
        teamChallenge.setGoalCount(teamChallenge.getGoalCount() + 1);
        teamChallengeDao.save(teamChallenge);
    }

    public List<FeedlikeDto> getfeedlikeList(int feedId) {
        Feed feed = feedDao.findById(feedId)
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));
        List<Feedlike> feedlikeList = feedLikeDao.findFeedlikeByFeed(feed);

        List<FeedlikeDto> feedlikeDtos = new ArrayList<>();
        for (Feedlike feedlike : feedlikeList) {
            feedlikeDtos.add(FeedlikeDto.builder()
                    .feedlikeId(feedlike.getFeedlikeId())
                    .memberId(feedlike.getMember().getMemberId())
                    .feedId(feedlike.getFeed().getFeedId())
                    .feedLike(feedlike.getFeedLike())
                    .build()
            );
        }

        return feedlikeDtos;
    }

    public List<FeedlikeDto> getMyFeedlikes(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<Feedlike> feedlikeList = feedLikeDao.findFeedlikeByMember(member);

        List<FeedlikeDto> feedlikeDtos = new ArrayList<>();
        for (Feedlike feedlike : feedlikeList) {
            feedlikeDtos.add(FeedlikeDto.builder()
                    .feedlikeId(feedlike.getFeedlikeId())
                    .memberId(feedlike.getMember().getMemberId())
                    .feedId(feedlike.getFeed().getFeedId())
                    .feedLike(feedlike.getFeedLike())
                    .build()
            );
        }

        return feedlikeDtos;
    }

    @Transactional
    public void updateFeedlike(FeedlikeDto feedlikeDto) {
        Feedlike feedlike = feedLikeDao.findById(feedlikeDto.getFeedlikeId())
                .orElseThrow(() -> new CustomException(FEEDLIKE_NOT_FOUND));

        feedlike.setFeedLike(feedlikeDto.getFeedLike());
        feedLikeDao.save(feedlike);
    }

    @Transactional
    public void deleteFeedlike(int feedlikeId) {
        Feedlike feedlike = feedLikeDao.findById(feedlikeId)
                .orElseThrow(() -> new CustomException(FEEDLIKE_NOT_FOUND));

        feedLikeDao.delete(feedlike);
    }
}