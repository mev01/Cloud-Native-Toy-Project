package com.web.curation.recommendation;

import com.web.curation.feed.Feed;
import com.web.curation.model.BasicResponse;
import com.web.curation.team.TeamDto;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@AllArgsConstructor
@RestController
public class RecommendationController {
    private RecommendationService recommendationService;

    @GetMapping("/recommend/{member_id}")
    public ResponseEntity getRecommendations(@PathVariable(name = "member_id") int memberId){
        log.info("추천 팀 요청");
        List<RecommendationDto> teamList = recommendationService.getRecommendations(memberId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = teamList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
