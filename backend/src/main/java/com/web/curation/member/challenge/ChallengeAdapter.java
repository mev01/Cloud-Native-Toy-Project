package com.web.curation.member.challenge;

import com.web.curation.member.Member;

public class ChallengeAdapter {
    public static Challenge createEntity(int memberId) {
        return new Challenge.ChallengeBuilder()
                .member(new Member(memberId))
                .attendance(1)
                .teamCount(0)
                .feedCount(0)
                .commentCount(0)
                .build();
    }

    public static Challenge dtoToEntity(ChallengeDto challengeDto) {
        return new Challenge.ChallengeBuilder()
                .challengeId(challengeDto.getChallengeId())
                .member(new Member(challengeDto.getMemberId()))
                .attendance(challengeDto.getAttendance())
                .teamCount(challengeDto.getTeamCount())
                .feedCount(challengeDto.getFeedCount())
                .commentCount(challengeDto.getCommentCount())
                .attendanceDate(challengeDto.getAttendanceDate())
                .build();
    }

    public static ChallengeDto entityToDto(Challenge challenge) {
        return new ChallengeDto.ChallengeDtoBuilder()
                .challengeId(challenge.getChallengeId())
                .memberId(challenge.getMember().getMemberId())
                .attendance(challenge.getAttendance())
                .teamCount(challenge.getTeamCount())
                .feedCount(challenge.getFeedCount())
                .commentCount(challenge.getCommentCount())
                .attendanceDate(challenge.getAttendanceDate())
                .build();
    }
}
