package com.web.curation.team.join;

import com.web.curation.member.MemberDto;
import com.web.curation.team.TeamDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinTeamDto {
    private int joinTeamId;
    private TeamDto team;
    private MemberDto member;
}
