package com.example.demo.member;

import com.example.demo.core.security.JwtTokenProvider;
import com.example.demo.core.utils.ApiUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private AuthenticationServiceException authenticationService;

    @PostMapping("/join")
    public ResponseEntity<?> join (@RequestBody @Valid MemberRequest.JoinDTO requestDTO, Error error){
        memberService.join(requestDTO);
        return ResponseEntity.ok(ApiUtils.success(null));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid MemberRequest.JoinDTO requestDTO, Error error, HttpServletResponse response) {
        String token = memberService.login(requestDTO);

        // "Bearer " 접두사 제거
        token = token.replace(JwtTokenProvider.TOKEN_PREFIX, "");

        // 쿠키 설정
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24); // 24시간 유효
        cookie.setPath("/"); // 모든 경로에서 쿠키 접근 가능
        response.addCookie(cookie);

        return ResponseEntity.ok().body(ApiUtils.success(null));
    }

}
