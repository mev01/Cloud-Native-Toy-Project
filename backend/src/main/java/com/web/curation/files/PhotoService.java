package com.web.curation.files;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class PhotoService {
    private PhotoDao photoDao;

    public PhotoDto addPhoto(PhotoDto photoDto) {
        Photo savePhoto = PhotoAndDtoAdapter.dtoToEntity(photoDto);
        return PhotoAndDtoAdapter.entityToDto(photoDao.save(savePhoto));
    }

    public Optional<PhotoDto> findByImageName(String uploadedImageName) {
        return photoDao.findByImageName(uploadedImageName);
    }

    public void delete(Integer id) {
        Optional<Photo> uploadedImage = photoDao.findById(id);
        if (uploadedImage.isPresent()) {
            photoDao.delete(uploadedImage.get());
        }
    }
}
