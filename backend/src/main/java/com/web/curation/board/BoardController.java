package com.web.curation.board;

import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
public class BoardController {

    private final BoardService boardService;
    private static final String SUCCESS = "success";

    @GetMapping("/board/list/{teamId}")
    @ApiOperation(value = "팀 게시글 조회")
    public ResponseEntity getBoardlist(@PathVariable int teamId) {
        List<Board> boardList = boardService.getBoardlist(teamId);
        ResponseEntity response = null;
        if (!boardList.isEmpty()) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = SUCCESS;
            result.object = boardList;
            response = new ResponseEntity(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @GetMapping("/board/{boardId}")
    @ApiOperation(value = "선택 게시글 보기")
    public ResponseEntity getBoardOne(@PathVariable int boardId) {
        BoardDto board = boardService.getBoardOne(boardId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = board;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/board")
    @ApiOperation(value = "게시글 작성")
    public ResponseEntity writeBoard(@Valid @RequestBody BoardDto boardDto) {
        BoardDto writeBoard = boardService.writeBoard(boardDto);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = writeBoard;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/board/{boardId}")
    @ApiOperation(value = "게시글 삭제")
    public ResponseEntity deleteBoard(@PathVariable int boardId) {
        final BasicResponse result = new BasicResponse();
        result.status = boardService.deleteBoard(boardId);
        result.data = SUCCESS;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/board")
    @ApiOperation(value = "게시글 수정")
    public ResponseEntity modifyBoard(@RequestBody BoardDto boardDto) {
        BoardDto modifyBoard = boardService.modifyBoard(boardDto);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = SUCCESS;
        result.object = modifyBoard;
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
