package com.web.curation.member;

import java.util.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.web.curation.feed.Feed;
import com.web.curation.member.JwtServiceImpl;
import com.web.curation.member.Member;
import com.web.curation.member.challenge.ChallengeService;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.web.curation.model.BasicResponse;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import com.github.scribejava.core.model.OAuth2AccessToken;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
@CrossOrigin(origins = {"*"}, maxAge = 6000)
@RestController
@RequestMapping("/member")
public class MemberController {
    // test1
    @Autowired
    MemberService memberService;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    private JwtServiceImpl jwtService;

    private NaverLoginBO naverLoginBO;
    private String apiResult = null;

    @Autowired
    private void setNaverLoginBO(NaverLoginBO naverLoginBO) {
        this.naverLoginBO = naverLoginBO;
    }

    HttpSession rootSession;


    EntityManager em;

    @PostMapping("/signup")
    @ApiOperation(value = "회원가입")
    @ApiResponses(value = {@ApiResponse(code = 201, message = "회원이 생성됨", response = BasicResponse.class),
            @ApiResponse(code = 409, message = "중복된 값이 있음", response = BasicResponse.class)})
    public ResponseEntity<Map<String, Object>> signup(@RequestBody MemberDto memberDto) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        memberDto.setAuthenticated(false);
        if (!memberService.hasSameEmail(memberDto.getEmail())) {
            MemberDto responseMemberDto = memberService.registerMember(memberDto);
            challengeService.createChallenge(responseMemberDto.getMemberId());

            resultMap.put("message", "success");
            status = HttpStatus.CREATED;
            log.info(resultMap.get("message").toString());
        } else {
            resultMap.put("message", "fail");
            status = HttpStatus.CONFLICT;
        }
        log.info(resultMap.get("message").toString());
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "로그인", notes = "Access-token과 로그인 결과 메세지를 반환한다.", response = Map.class)
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody @ApiParam(value = "로그인 시 필요한 회원정보(아이디, 비밀번호).", required = true) MemberDto memberDto) {
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;
        try {
            Optional<MemberDto> loginUser = memberService.getUser(memberDto.getEmail(), memberDto.getPassword());
            if (loginUser.isPresent()) {
                challengeService.attend(loginUser.get().getMemberId());

                String token = jwtService.create("memberEmail", loginUser.get().getEmail(), "access-token");// key, data, subject
                resultMap.put("access-token", token);
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
            } else {
                resultMap.put("message", "fail");
                status = HttpStatus.CONFLICT;
            }
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "회원인증", notes = "회원 정보를 담은 Token을 반환한다.", response = Map.class)
    @GetMapping("/info/{memberEmail}")
    public ResponseEntity<Map<String, Object>> getInfo(
            @PathVariable("memberEmail") @ApiParam(value = "인증할 회원의 아이디.", required = true) String memberEmail,
            HttpServletRequest request) {
        log.info("회원인증");
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status;

        if (jwtService.isUsable(request.getHeader("access-token"))) {
            try {
                Optional<MemberDto> member = memberService.getMemberByEmail(memberEmail);
                log.info(member.toString());
                resultMap.put("memberInfo", member);
                resultMap.put("message", "success");
                status = HttpStatus.ACCEPTED;
            } catch (Exception e) {
                resultMap.put("message", e.getMessage());
                status = HttpStatus.INTERNAL_SERVER_ERROR;
            }
        } else {
            resultMap.put("message", "fail");
            status = HttpStatus.CONFLICT;
        }
        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "회원결제", notes = "회원 authenticated 변경한다.", response = Map.class)
    @GetMapping("/{email}")
    public ResponseEntity<Map<String, Object>> updateAuthenticate(
            @PathVariable("email") @ApiParam(value = "인증할 회원의 이메일.", required = true) String memberEmail,
            HttpServletRequest request) {
        log.info("회원결제");
        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        try {
            memberService.updateMemberAuthenticate(memberEmail);
            resultMap.put("message", "success");
            status = HttpStatus.ACCEPTED;
        } catch (Exception e) {
            resultMap.put("message", e.getMessage());
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(resultMap, status);
    }

    @ApiOperation(value = "회원정보 수정")
    @PutMapping
    public ResponseEntity<Map<String, Object>> updateInfo(MemberDto memberDto) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();

        MemberDto responseMemberDto = memberService.updateMember(memberDto);
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("memberInfo", responseMemberDto);
        resultMap.put("data", dataMap);
        resultMap.put("message", "success");

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @ApiOperation(value = "비밀번호 수정")
    @PostMapping("/password")
    public ResponseEntity<Map<String, Object>> updatePassword(@RequestBody MemberDto memberDto) throws IOException {
        Map<String, Object> resultMap = new HashMap<>();
        memberService.updateMemberPassword(memberDto.getEmail(), memberDto.getPassword());
        resultMap.put("message", "success");
        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @GetMapping("/your/{email}")
    @ApiOperation(value = "이메일로 멤버 받기")
    public ResponseEntity getMemberFeeds(@PathVariable(name = "email") String email) {
        log.info("이메일로 멤버받기" + email);

        Optional<MemberDto> member = memberService.getMemberByEmail(email);
        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.data = "success";
        result.object = member;
        log.info("멤버는: " + member.get().getMemberId());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @ApiOperation(value = "카카오 로그인")
    @GetMapping("/kakao")
    public ResponseEntity<Map<String, Object>> kakaoLogin(@RequestParam String code) {
        Map<String, Object> resultMap = new HashMap<>();

        String token = getKakaoToken(code);
        Map<String, String> userInfo = getKaKaoUserInfo(token);
        Optional<MemberDto> dto = memberService.getMemberByEmail(userInfo.get("email"));

        resultMap.put("message", "카카오 유저 정보");
        resultMap.put("data", userInfo);
        resultMap.put("member", dto);
        String accessToken = jwtService.create("memberEmail", userInfo.get("email"), "access-token");
        resultMap.put("access-token", accessToken);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    private String getKakaoToken(String code) {
        String accessToken = "";
        String refreshToken = "";
        HttpURLConnection conn = null;

        try {
            URL url = new URL("https://kauth.kakao.com/oauth/token");
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=4ae2f007d84e033a263932b5176355f1");
            sb.append("&redirect_uri=http://i5c105.p.ssafy.io/kakaosignup");
            sb.append("&code=" + code);
            bw.write(sb.toString());
            bw.flush();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public Map<String, String> getKaKaoUserInfo(String accessToken) {

        Map<String, String> userInfo = new HashMap<>();
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        try {
            URL url = new URL(reqURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result.toString());

            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String id = element.getAsJsonObject().get("id").getAsString();
            String email = null;
            if (kakaoAccount.getAsJsonObject().get("email") != null) {
                email = kakaoAccount.getAsJsonObject().get("email").getAsString();
                userInfo.put("id", id);
                userInfo.put("email", email);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return userInfo;
    }

    @ApiOperation(value = "구글 로그인 사용자 체크")
    @GetMapping("/google")
    public ResponseEntity<Map<String, Object>> googleLogin(@RequestParam String email) {
        Map<String, Object> resultMap = new HashMap<>();

        Optional<MemberDto> dto = memberService.getMemberByEmail(email);

        resultMap.put("message", "카카오 유저 정보");
        resultMap.put("data", dto);

        return new ResponseEntity<>(resultMap, HttpStatus.OK);
    }

    @RequestMapping(value = "/naverlogin", method = {RequestMethod.GET, RequestMethod.POST})
    public String login(Model model, HttpSession session) {
        rootSession = session;
        String naverAuthUrl = naverLoginBO.getAuthorizationUrl(session);
        model.addAttribute("url", naverAuthUrl);
        return naverAuthUrl;
    }

    @RequestMapping(value = "/navercallback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<Map<String, Object>> callback(Model model, @RequestParam String code, @RequestParam String state, HttpSession session) throws IOException, ParseException {
        OAuth2AccessToken oauthToken;
        oauthToken = naverLoginBO.getAccessToken(rootSession, code, state);
        apiResult = naverLoginBO.getUserProfile(oauthToken);

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(apiResult);
        JSONObject jsonObj = (JSONObject) obj;
        JSONObject responseObj = (JSONObject) jsonObj.get("response");
        String email = (String) responseObj.get("email");
        String name = (String) responseObj.get("name");

        session.setAttribute("sessionId", email);
        model.addAttribute("result", apiResult);

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = HttpStatus.ACCEPTED;

        Optional<MemberDto> member = memberService.getMemberByEmail(email);
        if (member.isPresent()) {
            String token = jwtService.create("memberEmail", email, "access-token");
            resultMap.put("access-token", token);
            resultMap.put("message", "nocreate");
            status = HttpStatus.CREATED;
        } else {
            resultMap.put("message", "needcreate");
            resultMap.put("email", email);
            resultMap.put("name", name);
            status = HttpStatus.CREATED;
        }

        System.out.println(resultMap.get("message"));
        return new ResponseEntity<>(resultMap, status);
    }

    @GetMapping("/id/{memberId}")
    public ResponseEntity getMemberInfo(@PathVariable int memberId) {
        MemberDto memberDto = memberService.getMemberInfo(memberId);
        ResponseEntity response = null;

        final BasicResponse result = new BasicResponse();
        result.status = true;
        result.message = "success";
        result.object = memberDto;
        response = new ResponseEntity<>(result, HttpStatus.OK);

        return response;
    }

}
