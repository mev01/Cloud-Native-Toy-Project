package com.web.curation.feed.like;

import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@AllArgsConstructor
public class FeedlikeController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String SUCCESS = "success";
    private FeedlikeService feedlikeService;

    @PostMapping("/feedlike")
    public ResponseEntity createFeedlike(@RequestBody @Valid FeedlikeDto feedlikeDto){
        logger.info("좋아요 생성 요청");
        feedlikeService.likeFeed(feedlikeDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/feedlike/feed/{feed_id}")
    public ResponseEntity getFeedlikes(@PathVariable(value = "feed_id")int feedId){
        logger.info("피드별 좋아요 요청");
        List<FeedlikeDto> feedlikeList = feedlikeService.getfeedlikeList(feedId);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = feedlikeList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/feedlike/member/{member_id}")
    public ResponseEntity getMyFeedlikes(@PathVariable(value = "member_id")int memberId){
        logger.info("멤버별 좋아요 요청");
        List<FeedlikeDto> feedlikeList = feedlikeService.getMyFeedlikes(memberId);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = feedlikeList;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/feedlike")
    public ResponseEntity updateFeedlike(@RequestBody @Valid FeedlikeDto feedlikeDto){
        logger.info("좋아요 수정 요청");
        feedlikeService.updateFeedlike(feedlikeDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/feedlike/{feedlike_id}")
    public ResponseEntity deleteFeedlike(@PathVariable(value = "feedlike_id") int feedlikeId){
        logger.info("좋아요 삭제 요청");
        feedlikeService.deleteFeedlike(feedlikeId);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
