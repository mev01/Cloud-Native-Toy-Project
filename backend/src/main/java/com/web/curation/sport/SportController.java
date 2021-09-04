package com.web.curation.sport;

import com.web.curation.model.BasicResponse;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ApiResponses(value = {@ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class)})

//@CrossOrigin(origins = { "http://localhost:3000" })
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RequiredArgsConstructor
@RestController
public class SportController {

    private final SportService sportService;

    @GetMapping("/sport")
    @ApiOperation(value = "운동 목록 조회")
    public ResponseEntity getSportlist() {
        List<SportDto> sportList = sportService.getSportList();
        ResponseEntity response = null;
        if (!sportList.isEmpty()) {
            final BasicResponse result = new BasicResponse();
            result.status = true;
            result.data = "success";
            result.object = sportList;
            response = new ResponseEntity<>(result, HttpStatus.OK);
        } else {
            response = new ResponseEntity(null, HttpStatus.NO_CONTENT);
        }
        return response;
    }

    @GetMapping("/sport/{sprotId}")
    @ApiOperation(value = "Sport ID로 운동 이름 검색")
    public ResponseEntity getSportOne(@PathVariable int sportId) {
        SportDto searchSport = sportService.getSportOne(sportId);
        return new ResponseEntity<>(searchSport, HttpStatus.OK);
    }
}
