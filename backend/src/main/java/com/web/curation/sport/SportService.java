package com.web.curation.sport;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class SportService {
    private SportDao sportDao;

    public List<SportDto> getSportList() {
        List<Sport> sportList = sportDao.findAll();
        List<SportDto> responseSports = new ArrayList<>();
        for (Sport sport : sportList) {
            SportDto sportDto = SportAndDtoAdapter.entityToDto(sport);
            responseSports.add(sportDto);
        }
        return responseSports;
    }

    public SportDto getSportOne(int sportId) {
        return SportAndDtoAdapter.entityToDto(sportDao.findById(sportId));
    }

}
