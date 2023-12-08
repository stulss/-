package com.example.demo.member;

import com.example.demo.core.error.exception.Exception400;
import com.example.demo.core.error.exception.Exception401;
import com.example.demo.core.error.exception.Exception500;
import com.example.demo.core.security.CustomUserDetails;
import com.example.demo.core.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    public final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public void join(MemberRequest.JoinDTO requestDTO) {
        // 동일한 email이 존재 하는지 확인.
        checkEmail(requestDTO.getEmail());
        String encodedPassword = passwordEncoder.encode( requestDTO.getPassword());
        requestDTO.setPassword(encodedPassword);
        try {
            memberRepository.save(requestDTO.toEntity());
        }catch (Exception e){
            throw new Exception500(e.getMessage());
        }
    }

    public String login(MemberRequest.JoinDTO requestDTO) {
        try{
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(requestDTO.getEmail(), requestDTO.getPassword());
            // 비인증
            Authentication authentication =  authenticationManager.authenticate(
                    usernamePasswordAuthenticationToken);
            //
            CustomUserDetails customUserDetails = (CustomUserDetails)authentication.getPrincipal();
            return JwtTokenProvider.create(customUserDetails.getMember());
        }catch (Exception e){
            throw new Exception401("인증되지 않음.");
        }
    }

    public void checkEmail(String email){
        // 동일한 이메일이 있는지 확인.
        Optional<Member> byEmail = memberRepository.findByEmail(email);
        if(byEmail.isPresent()) {
            throw new Exception400("이미 존재하는 이메일 입니다. : " + email);
        }
    }

    public Optional<Member> findByMemberEmail(String email) {
        // 멤버 이메일로 멤버를 찾아서 반환하는 로직
        // 예를 들어, 멤버 레포지토리를 이용해 멤버를 찾는다고 가정하면:

        Optional<Member> foundMember = memberRepository.findByEmail(email); // 멤버 레포지토리를 통해 이메일로 멤버 찾기

        return foundMember;
    }
}
