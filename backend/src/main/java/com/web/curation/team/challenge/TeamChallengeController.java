package com.web.curation.team.challenge;

import java.util.List;

import javax.validation.Valid;

import com.web.curation.team.challenger.TeamChallenger;
import com.web.curation.team.challenger.TeamChallengerDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.curation.model.BasicResponse;

import io.swagger.annotations.ApiOperation;

@Slf4j
@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
public class TeamChallengeController {
    private TeamChallengeService teamChallengeService;
    private static final String SUCCESS = "success";

    @GetMapping("/my_teamchallenge_list/{member_id}")
    @ApiOperation(value = "내 팀 챌린지 리스트")
    public ResponseEntity findTeamChallenges(@Valid @RequestParam(name = "member_id") int memberId) {

        List<TeamChallengeDto> teamChallengeDtos = teamChallengeService.getTeamChallengeList(memberId);

        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = teamChallengeDtos;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/my_teamchalleging_list")
    @ApiOperation(value = "내 팀 진행중인 챌린지 리스트")
    public ResponseEntity findTeamChallenging(@Valid @RequestParam(name = "member_id") int memberId) {
        log.info("내 팀 진행중인 챌린지 리스트");
        List<TeamChallenger> list = teamChallengeService.getTeamChallengingList(memberId);
        BasicResponse result = new BasicResponse();
        ResponseEntity response = null;
        if (list.isEmpty()) {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            result.status = true;
            result.data = SUCCESS;
            result.object = list;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PostMapping("/team_challenge_enroll")
    @ApiOperation(value = "팀 챌런지 생성하기")
    public ResponseEntity createTeamChallenge(@Valid @RequestBody TeamChallengeDto teamChallengeDto) {

        TeamChallengeDto retDto = teamChallengeService.addTeamChallenge(teamChallengeDto);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.object = retDto;
        result.message = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/team_challenge_participate")
    @ApiOperation(value = "팀 챌런지 참여하기")
    public ResponseEntity participateTeamChallenge(@Valid @RequestBody TeamChallengerDto teamChallengerDto) {

        teamChallengeService.participateTeamChallenge(teamChallengerDto);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/team_challenge_giveup")
    @ApiOperation("챌린지 포기하기")
    public ResponseEntity giveUpTeamChallenge(@Valid @RequestBody TeamChallengerDto teamChallengerDto) {

        teamChallengeService.giveUpTeamChallenge(teamChallengerDto);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/team_challenge")
    @ApiOperation(value = "팀 챌린지 수정하기")
    public ResponseEntity updateTeamChallenge(@Valid @RequestBody TeamChallengeDto teamChallengeDto) {

        teamChallengeService.updateTeamChallenge(teamChallengeDto);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/team_challenge")
    @ApiOperation(value = "팀 챌린지 삭제하기")
    public ResponseEntity deleteTeamChallenge(@Valid @RequestBody TeamChallengeDto teamChallengeDto) {

        teamChallengeService.deleteTeamChallenge(teamChallengeDto);
        BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}