package com.web.curation.feed;

import com.web.curation.amazonS3.S3Uploader;
import com.web.curation.error.CustomException;
import com.web.curation.files.Photo;
import com.web.curation.files.PhotoAndDtoAdapter;
import com.web.curation.files.PhotoDao;
import com.web.curation.files.PhotoDto;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;
import com.web.curation.team.challenge.TeamChallengeDao;
import com.web.curation.team.join.JoinTeam;
import com.web.curation.team.join.JoinTeamDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.web.curation.error.ErrorCode.*;

@Slf4j
@Service
@AllArgsConstructor
public class FeedService {
    private FeedDao feedDao;
    private PhotoDao photoDao;
    private S3Uploader s3Uploader;
    private JoinTeamDao joinTeamDao;
    private TeamChallengeDao teamChallengeDao;
    private MemberDao memberDao;
    private TeamDao teamDao;

    @Transactional
    public void registerFeed(FeedDto feedDto) throws IOException {
        Member member = memberDao.findById(feedDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(feedDto.getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        joinTeamDao.findByMemberAndTeam(member, team)
                .orElseThrow(() -> new CustomException(JOIN_TEAM_NOT_FOUND));

        feedDto.setMember(member);
        feedDto.setTeam(team);
        Feed feed = FeedAdaptor.dtoToEntity(feedDto);
        if (feedDto.getTeamchallengeId() != 0) {
            feed.setTeamchallenge(teamChallengeDao.findById(feedDto.getTeamchallengeId())
                    .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND)));
        }
        Feed resultFeed = feedDao.save(feed);

        PhotoDto uploadPhoto = s3Uploader.upload(feedDto.getImage(), "static");
        uploadPhoto.setFeed(resultFeed);
        Photo photo = PhotoAndDtoAdapter.dtoToEntity(uploadPhoto);
        photoDao.save(photo);
    }

    public List<Feed> getFeedList(int memberId, int page) {
        PageRequest pageRequest = PageRequest.of(page, 3, Sort.by("feedId").descending());
        Member chkMember = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<JoinTeam> joinTeamList = joinTeamDao.findJoinTeamByMember(chkMember);
        List<Team> teamForSearch = new ArrayList<>();
        joinTeamList.forEach(joinTeam -> teamForSearch.add(joinTeam.getTeam()));
        if (teamForSearch.isEmpty()) return Collections.emptyList();
        List<Feed> feeds = feedDao.findAllJoinFetch(teamForSearch, pageRequest);
        if (feeds == null) {
            return Collections.emptyList();
        }
        return feeds;
    }

    @Transactional
    public void updateFeedWithImg(FeedDto feedDto) throws IOException {
        Feed oldFeed = feedDao.findById(feedDto.getFeedId())
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));

        for (Photo photo : oldFeed.getPhotos()) {
            s3Uploader.deleteFile(photo.getImageName() + "." + photo.getImageExtension());
            photoDao.delete(photo);
        }

        List<Photo> photoList = new ArrayList<>();

        Member member = memberDao.findById(feedDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(feedDto.getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        feedDto.setMember(member);
        feedDto.setTeam(team);
        Feed feed = FeedAdaptor.dtoToEntity(feedDto);
        if (feedDto.getTeamchallengeId() != 0) {
            feed.setTeamchallenge(teamChallengeDao.findById(feedDto.getTeamchallengeId())
                    .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND)));
        }

        PhotoDto uploadPhoto = s3Uploader.upload(feedDto.getImage(), "static");
        uploadPhoto.setFeed(Feed.builder().feedId(feedDto.getFeedId()).build());
        Photo photo = PhotoAndDtoAdapter.dtoToEntity(uploadPhoto);
        photoList.add(photoDao.save(photo));
        feed.setPhotos(photoList);
        feedDao.save(feed);
    }

    @Transactional
    public void updateFeedWithNoImg(FeedDto feedDto) {
        Feed oldFeed = feedDao.findById(feedDto.getFeedId())
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));
        Member member = memberDao.findById(feedDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(feedDto.getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        feedDto.setMember(member);
        feedDto.setTeam(team);
        Feed feed = FeedAdaptor.dtoToEntity(feedDto);
        feed.setPhotos(oldFeed.getPhotos());

        if (feedDto.getTeamchallengeId() != 0) {
            feed.setTeamchallenge(teamChallengeDao.findById(feedDto.getTeamchallengeId())
                    .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND)));
        }

        feedDao.save(feed);
    }

    @Transactional
    public void deleteFeed(int feedId) {
        Feed feed = feedDao.findById(feedId)
                .orElseThrow(() -> new CustomException(FEED_NOT_FOUND));
        for (Photo photo : feed.getPhotos()) {
            s3Uploader.deleteFile(photo.getImageName() + "." + photo.getImageExtension());
            photoDao.delete(photo);
        }
        feedDao.delete(feed);
    }

    public List<Feed> getTeamFeeds(int teamId) {
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        List<Feed> teamFeeds = feedDao.findByTeam(team);
        if (teamFeeds == null) {
            return Collections.emptyList();
        }
        return teamFeeds;
    }

    public List<Feed> getMemberFeeds(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        List<Feed> teamFeeds = feedDao.findByMember(member);
        if (teamFeeds == null) {
            return Collections.emptyList();
        }
        return teamFeeds;
    }
}
