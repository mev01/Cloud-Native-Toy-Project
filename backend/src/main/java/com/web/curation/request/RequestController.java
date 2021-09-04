package com.web.curation.request;

import java.util.List;

import com.web.curation.model.BasicResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@RequestMapping("/request")
public class RequestController {
    @Autowired
    RequestService requestService;

    @PostMapping
    @ApiOperation(value = "팀 가입요청")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "팀 가입요청이 생성됨"),
            @ApiResponse(code = 409, message = "이미 요청한 적이 있음")})
    public Object sendRequest(@RequestBody RequestDto requestDto) {
        requestService.makeRequest(requestDto);
        log.info("멤버 아이디 : " + requestDto.getMember().getMemberId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{teamId}")
    @ApiOperation(value = "팀별 가입요청 목록")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "팀별 가입요청 리스트", response = Request.class, responseContainer = "List")})
    public Object getRequestList(@PathVariable int teamId) {
        List<RequestDto> list = requestService.getRequestList(teamId);

        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PutMapping("/accept/{requestId}")
    @ApiOperation(value = "가입요청 수락")
    @ApiResponses(value = {@ApiResponse(code = 202, message = "가입요청 수락됨")})
    public Object acceptRequest(@PathVariable int requestId) {
        requestService.acceptRequest(requestId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }

    @PutMapping("/reject/{requestId}")
    @ApiOperation(value = "가입요청 거절")
    @ApiResponses(value = {@ApiResponse(code = 202, message = "가입요청 거절됨")})
    public Object rejectRequest(@PathVariable int requestId) {
        requestService.rejectRequest(requestId);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        return new ResponseEntity<>(result, HttpStatus.ACCEPTED);
    }
}
