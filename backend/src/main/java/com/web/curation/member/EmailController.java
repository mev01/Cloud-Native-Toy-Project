package com.web.curation.member;

import java.util.HashMap;
import java.util.Map;


import javax.mail.MessagingException;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.web.curation.model.NormalResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private EmailService emailService;
    @Autowired
    private MemberService memberService;

    private static int authenticNum;

    @PostMapping(value = "/send")
    @ApiOperation(value = "이메일 전송")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "이메일 정상 전송", response = NormalResponse.class)})
    public ResponseEntity<Map<String, Object>> sendEmail(@RequestParam(name = "member_email") String memberEmail) throws MessagingException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        StringBuilder emailcontent = new StringBuilder();
        authenticNum = (int) (Math.random() * 8999) + 1000;
        emailcontent.append("<!DOCTYPE html>");
        emailcontent.append("<html>");
        emailcontent.append("<head>");
        emailcontent.append("</head>");
        emailcontent.append("<body>");
        emailcontent.append(
                " <div" +
                        "	style=\"font-family: 'Apple SD Gothic Neo', 'sans-serif' !important; width: 400px; height: 600px; border-top: 4px solid #02b875; margin: 100px auto; padding: 30px 0; box-sizing: border-box;\">" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">혼자 머하니?</span><br />" +
                        "		<span style=\"color: #02b875\">메일인증</span> 안내입니다." +
                        "	</h1>\n" +
                        "	<p style=\"font-size: 16px; line-height: 26px; margin-top: 50px; padding: 0 5px;\">" +
                        "		안녕하세요.<br />" +
                        "		혼자 머하니?에 가입해 주셔서 진심으로 감사드립니다.<br />" +
                        "		아래 <b style=\"color: #02b875\">'인증번호'</b> 를 입력하여 회원가입을 완료해 주세요.<br />" +
                        "		감사합니다." +
                        "	</p>" +
                        "	<h1 style=\"margin: 0; padding: 0 5px; font-size: 28px; font-weight: 400;\">" +
                        "		<span style=\"font-size: 15px; margin: 0 0 10px 3px;\">인증번호</span><br />" +
                        "		<span style=\"color: #02b875\"> " + authenticNum + "</span>" +
                        "	</h1>\n" +
                        "<div style=\"border-top: 1px solid #DDD; padding: 5px;\"></div>" +
                        " </div>"
        );
        emailcontent.append("</body>");
        emailcontent.append("</html>");
        emailService.sendMail(memberEmail + " <" + memberEmail + ">", "혼자 머하니 이메일 인증", emailcontent.toString());

        resultMap.put("message", "success");
        status = HttpStatus.OK;

        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping(value = "/certified")
    @ApiOperation(value = "이메일 인증")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "이메일 인증 정상 작동", response = NormalResponse.class)})
    public ResponseEntity<Map<String, Object>> checkEmail(@RequestParam(name = "num") int num) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        log.info("이메일 확인");

        if (authenticNum == num) {
            resultMap.put("message", "success");
        } else {
            resultMap.put("message", "fail");
        }
        status = HttpStatus.OK;
        return new ResponseEntity<>(resultMap, status);
    }
}
