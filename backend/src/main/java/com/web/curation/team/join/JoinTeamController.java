package com.web.curation.team.join;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
public class JoinTeamController {
    @Autowired
    JoinTeamService joinTeamService;

    @PostMapping("/jointeam")
    @ApiOperation(value = "팀 가입")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "팀 가입 성공")})
    public Object getMyTeamList(@RequestBody JoinTeamDto joinTeamDto) {
        if (!joinTeamService.getSameContent(joinTeamDto).isPresent()) {
            joinTeamService.createJoin(joinTeamDto);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.CONFLICT);
    }

    @GetMapping("/jointeam/member/{teamId}")
    @ApiOperation(value = "팀 멤버 리스트 호출")
    public Object getMyTeamMember(@PathVariable int teamId) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("data", joinTeamService.findMemberByTeam(teamId));
        resultMap.put("message", "success");
        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }

    @DeleteMapping("/jointeam")
    @ApiOperation(value = "팀 멤버 삭제")
    public Object deleteTeamMember(@RequestParam int teamId, @RequestParam int memberId) {
        Map<String, Object> resultMap = new HashMap<>();

        joinTeamService.deleteTeamMember(teamId, memberId);
        resultMap.put("message", "success");

        return new ResponseEntity<Map<String, Object>>(resultMap, HttpStatus.OK);
    }
}
