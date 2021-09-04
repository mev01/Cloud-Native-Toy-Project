package com.web.curation.team.challenger;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Valid
@ToString
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TeamChallengerDto {
    @ApiModelProperty(required = false)
    private int teamchallengerId;

    @ApiModelProperty(required = true)
    @NotNull
    private int memberId;

    @ApiModelProperty(required = true)
    @NotNull
    private int teamChallengeId;

    @ApiModelProperty(required = true)
    @NotNull
    private boolean done;
}
