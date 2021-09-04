package com.web.curation.team;

import com.web.curation.files.Photo;
import com.web.curation.files.PhotoAndDtoAdapter;
import com.web.curation.member.Member;
import com.web.curation.sport.Sport;
import com.web.curation.sport.SportAndDtoAdapter;

public class TeamAndDtoAdapter {
    public static TeamDto entityToDto(Team team) {
        TeamDto teamDto = TeamDto.builder()
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
            teamDto.setPhotoDto(PhotoAndDtoAdapter.entityToDto(team.getPhoto()));
        }
        return teamDto;
    }

    public static Team dtoToEntityPhoto(TeamDto teamDto) {
        return Team.builder()
                .teamId(teamDto.getTeamId())
                .createDate(teamDto.getCreateDate())
                .name(teamDto.getName())
                .introduction((teamDto.getIntroduction()))
                .leader(teamDto.getLeader())
                .memberCount(teamDto.getMemberCount())
                .member(new Member(teamDto.getMemberId()))
                .sport(new Sport(teamDto.getSportId()))
                .photo(new Photo(teamDto.getPhotoDto().getPhotoId()))
                .build();
    }

    public static Team dtoToEntity(TeamDto teamDto) {
        return Team.builder()
                .teamId(teamDto.getTeamId())
                .createDate(teamDto.getCreateDate())
                .name(teamDto.getName())
                .introduction((teamDto.getIntroduction()))
                .leader(teamDto.getLeader())
                .memberCount(teamDto.getMemberCount())
                .member(new Member(teamDto.getMemberId()))
                .sport(new Sport(teamDto.getSportId()))
                .build();
    }
}
