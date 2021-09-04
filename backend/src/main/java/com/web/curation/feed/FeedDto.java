package com.web.curation.feed;

import com.web.curation.files.PhotoDto;
import com.web.curation.member.Member;
import com.web.curation.team.Team;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.util.List;


@Getter
@Setter
@ToString
@Builder
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class FeedDto {
    private int feedId;

    @ApiModelProperty(required = true)
    private int teamchallengeId;

    @ApiModelProperty(required = true)
    private int memberId;

    @ApiModelProperty(required = true)
    private int teamId;

    @ApiModelProperty(required = true)
    private String teamName;

    @ApiModelProperty(required = true)
    private String contents;

    @ApiModelProperty(required = true)
    private String writer;

    private MultipartFile image;

    private List<PhotoDto> photos;

    private Member member;
    private Team team;
}