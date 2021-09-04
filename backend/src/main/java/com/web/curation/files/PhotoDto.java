package com.web.curation.files;


import com.web.curation.feed.Feed;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoDto {
    private Integer photoId;
    private String originalName;
    private String filePath;
    private Long fileSize;
    private String imageName;
    private String imageExtension;

    private Feed feed;

    public PhotoDto(Integer photoId) {
        this.photoId = photoId;
    }
}
