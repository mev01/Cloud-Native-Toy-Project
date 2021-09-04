package com.web.curation.request;

import com.web.curation.member.Member;
import com.web.curation.member.MemberAdapter;
import com.web.curation.team.Team;
import com.web.curation.team.TeamAndDtoAdapter;

public class RequestAdapter {
    public static Request createDtoToEntity(RequestDto requestDto) {
        return new Request.RequestBuilder()
                .requestId(requestDto.getRequestId())
                .team(new Team(requestDto.getTeam().getTeamId()))
                .member(new Member(requestDto.getMember().getMemberId()))
                .status(Status.WAITING)
                .build();
    }

    public static Request updateDtoToEntity(RequestDto requestDto) {
        return new Request.RequestBuilder()
                .requestId(requestDto.getRequestId())
                .team(new Team(requestDto.getTeam().getTeamId()))
                .member(new Member(requestDto.getMember().getMemberId()))
                .status(requestDto.getStatus())
                .createDate(requestDto.getCreateDate())
                .build();
    }

    public static RequestDto entityToDto(Request request) {
        return new RequestDto.RequestDtoBuilder()
                .requestId(request.getRequestId())
                .team(TeamAndDtoAdapter.entityToDto(request.getTeam()))
                .member(MemberAdapter.entityToDto(request.getMember()))
                .status(request.getStatus())
                .createDate(request.getCreateDate())
                .build();
    }
}
