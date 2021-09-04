package com.web.curation.team;

import com.web.curation.amazonS3.S3Uploader;
import com.web.curation.files.PhotoDto;
import com.web.curation.files.PhotoService;
import com.web.curation.member.Member;
import com.web.curation.model.BasicResponse;
import com.web.curation.sport.Sport;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
public class TeamController {

    private final TeamService teamService;
    private final S3Uploader s3Uploader;
    private final PhotoService photoService;
    private static final String SUCCESS = "success";

    //그룹 리스트 조회
    @GetMapping("/team")
    @ApiOperation(value = "팀 목록 조회")
    public ResponseEntity getTeamlist() {
        List<TeamDto> teamList = teamService.getTeamlist();
        ResponseEntity response = null;

        if (!teamList.isEmpty()) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = SUCCESS;
            result.object = teamList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @GetMapping("/team/{member_id}")
    public ResponseEntity getMyManagingTeam(@PathVariable(name = "member_id") int memberId) {
        TeamDto team = teamService.getMyManagingTeam(memberId);

        final BasicResponse result = new BasicResponse();
        if (team.getTeamId() != -1) {
            result.status = true;
            result.data = SUCCESS;
            result.object = team;
        } else {
            result.status = true;
            result.data = "fail";
        }
        log.info(team.toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/team")
    @ApiOperation(value = "팀 등록")
    public ResponseEntity addTeam(@Valid TeamDto teamDto) throws IOException {
        TeamDto resultTeam = teamService.registerTeam(teamDto);

        ResponseEntity response = null;

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = resultTeam;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

    @GetMapping("/team/{name}/exists")
    @ApiOperation(value = "팀 이름 중복검사")
    public ResponseEntity<Boolean> checkNameDuplicate(@PathVariable String name) {
        return ResponseEntity.ok(teamService.checkNameDuplicate(name));
    }

    @GetMapping("/team/my_team_list/{member_id}")
    @ApiOperation(value = "내 팀 가져오기")
    public ResponseEntity getMyTeamList(@PathVariable(name = "member_id") int memberId) {

        ResponseEntity response = null;
        List<TeamDto> teamList = teamService.getMyTeamList(memberId);

        final BasicResponse result = new BasicResponse();
        if (teamList.isEmpty()) {
            result.status = true;
            result.message = "fail";
            response = new ResponseEntity<>(result, HttpStatus.NO_CONTENT);
        } else {
            result.status = true;
            result.message = SUCCESS;
            result.object = teamList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        }
        return response;
    }

    @PutMapping("/team/leader/{memberId}")
    @ApiOperation(value = "팀 리더 변경")
    public Object changeTeamLeader(@PathVariable int memberId, @RequestParam int teamId) {
        Map<String, Object> resultMap = new HashMap<>();
        if (!teamService.checkLeader(memberId)) {
            resultMap.put("message", "fail");
        } else {
            teamService.changeTeamLeader(teamId, memberId);
            resultMap.put("message", SUCCESS);
        }
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    @PutMapping("/team")
    @ApiOperation(value = "팀 정보 변경")
    public Object updateTeam(@Valid TeamDto teamDto) {
        Map<String, Object> resultMap = new HashMap<>();

        teamService.updateTeam(teamDto);
        resultMap.put("message", SUCCESS);
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }
}
