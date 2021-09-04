package com.web.curation.member.challenge;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChallengeDto {
    private int challengeId;
    private int memberId;
    private int attendance;
    private int teamCount;
    private int feedCount;
    private int commentCount;
    private LocalDate attendanceDate;
}
