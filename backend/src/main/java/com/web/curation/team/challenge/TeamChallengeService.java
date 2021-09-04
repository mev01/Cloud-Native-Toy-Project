package com.web.curation.team.challenge;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.web.curation.error.CustomException;
import com.web.curation.team.challenger.TeamChallengerDto;
import com.web.curation.team.join.JoinTeam;
import com.web.curation.team.join.JoinTeamDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.TeamDao;
import com.web.curation.team.challenger.TeamChallenger;
import com.web.curation.team.challenger.TeamChallengerDao;

import static com.web.curation.error.ErrorCode.*;

@Service
@AllArgsConstructor
public class TeamChallengeService {

    private TeamChallengeDao teamChallengeDao;
    private TeamDao teamDao;
    private TeamChallengerDao teamChallengerDao;
    private MemberDao memberDao;
    private JoinTeamDao joinTeamDao;

    public TeamChallengeDto addTeamChallenge(TeamChallengeDto teamChallengeDto) {
        TeamChallenge teamChallenge = TeamChallengeAdaptor.dtoToEntity(teamChallengeDto);
        teamChallenge.setTeam(teamDao.findById(teamChallengeDto.getTeamId())
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND))
        );
        return TeamChallengeAdaptor.entityToDto(teamChallengeDao.save(teamChallenge));
    }

    public TeamChallengeDto updateTeamChallenge(TeamChallengeDto teamChallengeDto) {
        TeamChallenge teamChallenge = teamChallengeDao.findById(teamChallengeDto.getTeamChallengeId())
                .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND));

        teamChallenge.setTitle(teamChallengeDto.getTitle());
        teamChallenge.setContents(teamChallenge.getContents());
        teamChallenge.setStartDate(teamChallenge.getStartDate());
        teamChallenge.setEndDate(teamChallenge.getEndDate());

        return TeamChallengeAdaptor.entityToDto(teamChallengeDao.save(teamChallenge));
    }

    public void deleteTeamChallenge(TeamChallengeDto teamChallengeDto) {
        TeamChallenge teamChallenge = teamChallengeDao.findById(teamChallengeDto.getTeamChallengeId())
                .orElseThrow(() -> new CustomException(TEAM_CHALLENGER_NOT_FOUND));
        teamChallengeDao.delete(teamChallenge);
    }

    public List<TeamChallengeDto> getTeamChallengeList(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        List<JoinTeam> joinTeams = joinTeamDao.findJoinTeamByMember(member);

        List<TeamChallengeDto> teamChallengeDtos = new ArrayList<>();
        for (JoinTeam join : joinTeams) {
            List<TeamChallenge> teamChallenges = teamChallengeDao.findTeamChallengeByTeam(join.getTeam());
            for (TeamChallenge tc : teamChallenges) {
                teamChallengeDtos.add(TeamChallengeAdaptor.entityToDto(tc));
            }
        }
        return teamChallengeDtos;
    }

    public void participateTeamChallenge(@Valid TeamChallengerDto teamChallengerDto) {
        TeamChallenge teamChallenge = teamChallengeDao.findById(teamChallengerDto.getTeamChallengeId())
                .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND));
        Member member = memberDao.findById(teamChallengerDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));

        teamChallengerDao.findTeamChallengerByTeamChallengeAndMember(teamChallenge, member)
                .ifPresent(teamChallenger -> {
                    throw new CustomException(DUPLICATE_RESOURCE);
                });

        TeamChallenger teamChallenger = TeamChallenger.builder()
                .done(false)
                .teamChallenge(teamChallenge)
                .member(member)
                .build();

        teamChallengerDao.save(teamChallenger);
    }

    public void giveUpTeamChallenge(@Valid TeamChallengerDto teamChallengerDto) {
        TeamChallenge teamChallenge = teamChallengeDao.findById(teamChallengerDto.getTeamChallengeId())
                .orElseThrow(() -> new CustomException(TEAM_CHALLENGE_NOT_FOUND));
        Member member = memberDao.findById(teamChallengerDto.getMemberId())
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        TeamChallenger teamChallenger = teamChallengerDao.findTeamChallengerByTeamChallengeAndMember(teamChallenge, member)
                .orElseThrow(() -> new CustomException(TEAM_CHALLENGER_NOT_FOUND));

        teamChallengerDao.delete(teamChallenger);
    }

    public List<TeamChallenger> getTeamChallengingList(int memberId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        return teamChallengerDao.findTeamChallengerByMember(member);
    }
}