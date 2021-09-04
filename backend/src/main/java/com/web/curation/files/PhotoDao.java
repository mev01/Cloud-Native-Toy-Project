package com.web.curation.files;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhotoDao extends JpaRepository<Photo, Integer> {
    Optional<PhotoDto> findByImageName(String imageName);
}
