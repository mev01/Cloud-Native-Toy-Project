package com.web.curation.team.challenger;

import com.web.curation.error.CustomException;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;
import com.web.curation.team.challenge.TeamChallenge;
import com.web.curation.team.challenge.TeamChallengeDao;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.web.curation.error.ErrorCode.MEMBER_NOT_FOUND;
import static com.web.curation.error.ErrorCode.TEAM_NOT_FOUND;

@Service
@AllArgsConstructor
public class TeamChallengerService {

    private TeamChallengerDao teamChallengerDao;
    private TeamChallengeDao teamChallengeDao;
    private MemberDao memberDao;
    private TeamDao teamDao;


    public List<TeamChallenger> getTeamChallengerList(@Valid int memberId, int teamId) {
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(MEMBER_NOT_FOUND));
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));

        List<TeamChallenge> teamChallenges = teamChallengeDao.findTeamChallengeByTeam(team);
        List<TeamChallenger> teamChallengers = teamChallengerDao.findTeamChallengerByMember(member);
        List<TeamChallenger> res = new ArrayList<>();

        for (TeamChallenger teamChallenger : teamChallengers) {
            int id = teamChallenger.getTeamChallenge().getTeamChallengeId();
            boolean flag = false;
            for (TeamChallenge teamChallenge : teamChallenges) {
                if (teamChallenge.getTeamChallengeId() == id) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                res.add(teamChallenger);
            }
        }
        return res;
    }
}

