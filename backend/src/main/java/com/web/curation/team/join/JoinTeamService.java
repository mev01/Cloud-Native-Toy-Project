package com.web.curation.team.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.web.curation.request.Request;
import com.web.curation.request.RequestDao;
import org.springframework.stereotype.Service;

import com.web.curation.error.CustomException;
import com.web.curation.error.ErrorCode;
import com.web.curation.member.Member;
import com.web.curation.member.MemberDao;
import com.web.curation.team.Team;
import com.web.curation.team.TeamDao;

import lombok.AllArgsConstructor;

import static com.web.curation.error.ErrorCode.*;

@AllArgsConstructor
@Service
public class JoinTeamService {

    private JoinTeamDao joinTeamDao;
    private TeamDao teamDao;
    private MemberDao memberDao;
    private RequestDao requestDao;

    public void createJoin(JoinTeamDto joinTeam) {
        joinTeamDao.save(JoinTeamAdapter.dtoToEntity(joinTeam));
    }

    public Optional<JoinTeamDto> getSameContent(JoinTeamDto joinTeam) {
        Optional<JoinTeam> joinTeamEntity =
                joinTeamDao.findByMemberAndTeam(new Member(joinTeam.getMember().getMemberId()), new Team(joinTeam.getTeam().getTeamId()));

        Optional<JoinTeamDto> responseJoinTeam = Optional.ofNullable(null);
        if (!joinTeamEntity.isPresent()) return responseJoinTeam;
        return Optional.of(JoinTeamAdapter.entityToDto(joinTeamEntity.get()));
    }

    public List<JoinTeamDto> findMemberByTeam(int teamId) {
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        List<JoinTeam> joinTeams = joinTeamDao.findJoinTeamsByTeam(team)
                .orElseThrow(() -> new CustomException(JOIN_TEAM_NOT_FOUND));

        List<JoinTeamDto> list = new ArrayList<>();
        for (JoinTeam joinTeam : joinTeams) {
            list.add(JoinTeamAdapter.entityToDto(joinTeam));
        }

        return list;
    }

    @Transactional
    public boolean deleteTeamMember(int teamId, int memberId) {
        Team team = teamDao.findById(teamId)
                .orElseThrow(() -> new CustomException(TEAM_NOT_FOUND));
        Member member = memberDao.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        joinTeamDao.deleteByTeamAndMember(team, member);

        team.updateMemberCount(false);
        teamDao.save(team);

        Request request = requestDao.findRequestByTeamAndMember(team, member)
                .orElseThrow(() -> new CustomException(REQUEST_NOT_FOUND));
        requestDao.delete(request);

        return true;
    }

}
