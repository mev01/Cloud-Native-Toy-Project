package com.web.curation.team.challenge;

public class TeamChallengeAdaptor {
    public static TeamChallengeDto entityToDto(TeamChallenge t) {
        return TeamChallengeDto.builder()
                .teamChallengeId(t.getTeamChallengeId())
                .teamId(t.getTeam().getTeamId())
                .title(t.getTitle())
                .contents(t.getContents())
                .startDate(t.getStartDate())
                .endDate(t.getEndDate())
                .build();
    }

    public static TeamChallenge dtoToEntity(TeamChallengeDto dto) {
        return TeamChallenge.builder()
                .teamChallengeId(dto.getTeamChallengeId())
                .title(dto.getTitle())
                .contents(dto.getContents())
                .status(Status.BEGIN)
                .memberCount(1)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .build();
    }
}
