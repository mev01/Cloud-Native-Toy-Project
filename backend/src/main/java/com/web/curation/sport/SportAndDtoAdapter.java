package com.web.curation.sport;

public class SportAndDtoAdapter {
    public static SportDto entityToDto(Sport sport) {
        return SportDto.builder()
                .sportId(sport.getSportId())
                .name(sport.getName())
                .build();
    }

    public static Sport dtoToEntity(SportDto sportDto) {
        return Sport.builder()
                .sportId(sportDto.getSportId())
                .name(sportDto.getName())
                .build();
    }

}
