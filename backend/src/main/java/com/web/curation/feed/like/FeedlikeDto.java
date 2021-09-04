package com.web.curation.feed.like;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;

@Getter
@Setter
@ToString
@Builder
@Valid
@NoArgsConstructor
@AllArgsConstructor
public class FeedlikeDto {
    private int feedlikeId;

    @ApiModelProperty(required = true)
    private int memberId;

    @ApiModelProperty(required = true)
    private int feedId;

    @ApiModelProperty(required = true)
    private int feedLike;
}
