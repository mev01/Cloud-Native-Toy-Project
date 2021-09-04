package com.web.curation.team.join;

import com.web.curation.member.Member;
import com.web.curation.member.MemberAdapter;
import com.web.curation.team.Team;
import com.web.curation.team.TeamAndDtoAdapter;

public class JoinTeamAdapter {
    public static JoinTeam dtoToEntity(JoinTeamDto joinTeamDto) {
        return new JoinTeam.JoinTeamBuilder()
                .joinTeamId(joinTeamDto.getJoinTeamId())
                .team(new Team(joinTeamDto.getTeam().getTeamId()))
                .member(new Member(joinTeamDto.getMember().getMemberId()))
                .build();
    }

    public static JoinTeamDto entityToDto(JoinTeam joinTeam) {
        return new JoinTeamDto.JoinTeamDtoBuilder()
                .joinTeamId(joinTeam.getJoinTeamId())
                .team(TeamAndDtoAdapter.entityToDto(joinTeam.getTeam()))
                .member(MemberAdapter.entityToDto(joinTeam.getMember()))
                .build();
    }
}
