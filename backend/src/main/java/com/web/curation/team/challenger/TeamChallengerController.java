package com.web.curation.team.challenger;

import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController("/challenger")
public class TeamChallengerController {
    @Autowired
    private TeamChallengerService teamChallengerService;

    @GetMapping("/my_teamchallenger_list")
    @ApiOperation(value = "팀 챌린저 리스트")
    public Object findTeamChallenges(@RequestParam(name = "member_id") int memberId,
                                     @RequestParam(name = "team_id") int teamId) {

        List<TeamChallenger> list = teamChallengerService.getTeamChallengerList(memberId, teamId);
        BasicResponse result = new BasicResponse();
        ResponseEntity response = null;
        log.info("팀 챌린저 리스트 : " + teamId);
        if (list == null) {
            result.status = false;
            result.data = "fail";
            response = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } else {
            result.status = true;
            result.data = "success";
            result.object = list;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }
}
