package com.web.curation.feed;

import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})
@Slf4j
//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController()
@AllArgsConstructor
public class FeedController {
    private FeedService feedService;
    private static final String SUCCESS = "success";

    @PostMapping("/feed")
    @ApiOperation(value = "피드 생성")
    public ResponseEntity addFeed(FeedDto feedDto) throws IOException {
        log.info("피드 생성 요청");
        feedService.registerFeed(feedDto);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/feed/{memberId}/{page}")
    @ApiOperation(value = "피드 조회")
    public ResponseEntity getFeedList(@PathVariable int memberId, @PathVariable int page) {
        log.info("피드 조회 요청");
        List<Feed> feedList = feedService.getFeedList(memberId, page);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = feedList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/feed")
    @ApiOperation(value = "피드 수정")
    public ResponseEntity updateFeed(FeedDto feedDto) throws IOException {
        log.info("피드 수정 요청");
        if (feedDto.getImage() == null) {
            feedService.updateFeedWithNoImg(feedDto);
        } else {
            feedService.updateFeedWithImg(feedDto);
        }

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/feed/{feed_id}")
    @ApiOperation(value = "피드 삭제")
    public ResponseEntity deleteFeed(@PathVariable(name = "feed_id") int feedId) throws IOException {
        log.info("피드 삭제 요청");
        feedService.deleteFeed(feedId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/feed/team/{team_id}")
    @ApiOperation(value = "팀별 피드 받기")
    public ResponseEntity getTeamFeeds(@PathVariable(name = "team_id") int teamId) {
        log.info("팀별 피드 요청");
        List<Feed> feedList = feedService.getTeamFeeds(teamId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = feedList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/feed/member/{member_id}")
    @ApiOperation(value = "멤버별 피드 받기")
    public ResponseEntity getMemberFeeds(@PathVariable(name = "member_id") int memberId) {
        log.info("멤버별 피드 요청");
        List<Feed> feedList = feedService.getMemberFeeds(memberId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = feedList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}