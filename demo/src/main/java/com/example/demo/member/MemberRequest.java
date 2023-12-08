package com.example.demo.member;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collections;

public class MemberRequest {

    @Data
    public static class JoinDTO {
        @NotEmpty
        @Pattern(regexp = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "이메일 형식으로 작성해주세요")
        private String email;
        @NotEmpty
        @Size(min = 8, max = 20, message = "8자 이상 20자 이내로 작성 가능합니다.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@#$%^&+=!~`<>,./?;:'\"\\[\\]{}\\\\()|_-])\\S*$", message = "영문, 숫자, 특수문자가 포함되어야하고 공백이 포함될 수 없습니다.")
        private String password;

        /*
        @NotEmpty
        private String nickName;
        @NotEmpty
        @Pattern(regexp = "^[0-9]{10,11}$", message = "휴대폰 번호는 숫자 10~11자리만 가능합니다.")
        private String phoneNumber;*/

        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .roles(Collections.singletonList("ROLE_USER"))
                    .build();
        }
    }
    @Data
    public static class LoginDTO {
        private String email;
        private String password;


        public Member toEntity() {
            return Member.builder()
                    .email(email)
                    .password(password)
                    .build();
        }

    }
}
