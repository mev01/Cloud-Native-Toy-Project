package com.web.curation.feed.comment;

import com.web.curation.feed.FeedService;
import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    private final CommentService commentService;
    private final FeedService feedService;
    private static final String SUCCESS = "success";

    @GetMapping("/{feedId}")
    @ApiOperation(value = "피드에 달린 댓글 검색")
    public ResponseEntity getComments(@PathVariable int feedId) {
        log.info("Select Comments of Feed");
        List<CommentDto> commentDtoList = commentService.getCommentOfFeed(feedId);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = commentDtoList;
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @PostMapping("/")
    @ApiOperation(value = "피드에 댓글 달기")
    public ResponseEntity addComment(@RequestBody CommentDto commentDto) {
        log.info("Add Comment");
        CommentDto resultComment = commentService.addCommentOfFeed(commentDto);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = resultComment;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    @ApiOperation(value = "피드에 댓글 삭제")
    public ResponseEntity removeComment(@PathVariable int commentId) {
        log.info("Remove Comment");
        commentService.removeComment(commentId);

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
