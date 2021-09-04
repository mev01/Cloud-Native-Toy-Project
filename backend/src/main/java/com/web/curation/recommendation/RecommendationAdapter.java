package com.web.curation.recommendation;

import com.web.curation.files.PhotoAndDtoAdapter;
import com.web.curation.sport.SportAndDtoAdapter;
import com.web.curation.team.Team;

public class RecommendationAdapter {
    public static RecommendationDto teamToRecommendation(Team team) {
        RecommendationDto dto = RecommendationDto.builder()
                .teamId(team.getTeamId())
                .name(team.getName())
                .introduction(team.getIntroduction())
                .leader(team.getLeader())
                .memberCount(team.getMemberCount())
                .createDate(team.getCreateDate())
                .memberId(team.getMember().getMemberId())
                .sportDto(SportAndDtoAdapter.entityToDto(team.getSport()))
                .build();
        if (team.getPhoto() != null) {
            dto.setPhotoDto(PhotoAndDtoAdapter.entityToDto(team.getPhoto()));
        }
        return dto;
    }
}
