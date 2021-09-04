package com.web.curation.request;

import java.time.LocalDateTime;

import com.web.curation.member.MemberDto;
import com.web.curation.team.TeamDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RequestDto {
    private int requestId;
    private TeamDto team;
    private MemberDto member;
    private Status status;
    private LocalDateTime createDate;
}
