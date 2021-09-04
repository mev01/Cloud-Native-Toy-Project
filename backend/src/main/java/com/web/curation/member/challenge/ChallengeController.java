package com.web.curation.member.challenge;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
@RequestMapping("/member/challenge")
public class ChallengeController {
    private ChallengeService challengeService;

    @GetMapping(value = "/{memberId}")
    @ApiOperation(value = "공통 챌린지 정보 가져오기")
    public ResponseEntity<Map<String, Object>> getChallenge(@PathVariable int memberId) {
        Map<String, Object> resultMap = new HashMap<>();

        resultMap.put("data", challengeService.getChallenge(memberId));
        resultMap.put("message", "success");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }
}
