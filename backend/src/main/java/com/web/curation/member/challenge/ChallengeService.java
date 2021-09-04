package com.web.curation.member.challenge;

import java.time.LocalDate;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.web.curation.feed.FeedDao;
import com.web.curation.feed.comment.CommentDao;
import com.web.curation.member.Member;
import com.web.curation.team.join.JoinTeamDao;

import lombok.AllArgsConstructor;

@Slf4j
@Service
@AllArgsConstructor
public class ChallengeService {
    private ChallengeDao challengeDao;
    private JoinTeamDao joinTeamDao;
    private FeedDao feedDao;
    private CommentDao commentDao;

    public void createChallenge(int memberId) {
        challengeDao.save(ChallengeAdapter.createEntity(memberId));
    }

    public void attend(int memberId) {
        Challenge entity = challengeDao.getOneByMember(new Member(memberId));
        ChallengeDto dto = ChallengeAdapter.entityToDto(entity);

        if (!dto.getAttendanceDate().isEqual(LocalDate.now())) {
            dto.setAttendance(dto.getAttendance() + 1);
            dto.setAttendanceDate(LocalDate.now());
        }

        challengeDao.save(ChallengeAdapter.dtoToEntity(dto));
    }

    public ChallengeDto getChallenge(int memberId) {
        Challenge entity = challengeDao.getOneByMember(new Member(memberId));
        log.info("챌린지 아이디 : " + entity.getChallengeId());
        ChallengeDto dto = updateInfo(ChallengeAdapter.entityToDto(entity));

        challengeDao.save(ChallengeAdapter.dtoToEntity(dto));

        return dto;
    }

    private ChallengeDto updateInfo(ChallengeDto challengeDto) {
        System.out.println("update");
        int cnt = 0;
        cnt += feedDao.countByMember(new Member(challengeDto.getMemberId()));


        challengeDto.setTeamCount(joinTeamDao.countByMember(new Member(challengeDto.getMemberId())));
        challengeDto.setCommentCount(commentDao.countByMember(new Member(challengeDto.getMemberId())));
        challengeDto.setFeedCount(cnt);

        return challengeDto;
    }
}
