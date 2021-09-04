package com.web.curation.files;

public class PhotoAndDtoAdapter {
    public static PhotoDto entityToDto(Photo photo) {
        return PhotoDto.builder()
                .photoId(photo.getPhotoId())
                .originalName(photo.getOriginalName())
                .fileSize(photo.getFileSize())
                .filePath(photo.getFilePath())
                .imageName(photo.getImageName())
                .imageExtension(photo.getImageExtension())
                .build();
    }

    public static Photo dtoToEntity(PhotoDto photoDto) {
        return Photo.builder()
                .feed(photoDto.getFeed())
                .originalName(photoDto.getOriginalName())
                .filePath(photoDto.getFilePath())
                .fileSize(photoDto.getFileSize())
                .imageName(photoDto.getImageName())
                .imageExtension(photoDto.getImageExtension())
                .build();
    }
}
