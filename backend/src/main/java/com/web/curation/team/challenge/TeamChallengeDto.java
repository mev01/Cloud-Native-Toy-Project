package com.web.curation.team.challenge;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Valid
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamChallengeDto {
    @ApiModelProperty(required = false)
    private int teamChallengeId;

    @ApiModelProperty(required = true)
    @NotNull
    private int teamId;

    @ApiModelProperty(required = true)
    @NotNull
    private String title;

    @ApiModelProperty(required = true)
    @NotNull
    private String contents;

    @ApiModelProperty(required = true)
    @NotNull
    private LocalDateTime startDate;

    @ApiModelProperty(required = true)
    @NotNull
    private LocalDateTime endDate;
}
