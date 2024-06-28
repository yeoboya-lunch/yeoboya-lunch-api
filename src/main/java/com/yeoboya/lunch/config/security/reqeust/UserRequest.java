package com.yeoboya.lunch.config.security.reqeust;

import com.yeoboya.lunch.config.security.validation.ValidationGroups.KnowOldPassword;
import com.yeoboya.lunch.config.security.validation.ValidationGroups.UnKnowOldPassword;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public class UserRequest {

    @Getter
    @Setter
    @ToString
    public static class SignUp {

        @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9_.]{2,14}$",
                message = "로그인 아이디는 영어, 숫자, '_', '.' 만 포함할 수 있으며, 첫 글자에 '_', '.'는 사용할 수 없습니다.")
        private String loginId;

        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Email
        private String email;

        @Length(min = 2, max = 6)
        @NotEmpty(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;

        private String provider = "yeoboya";
    }

    @Getter
    @Setter
    @ToString
    public static class Credentials {

        @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9_.]{2,14}$",
                message = "로그인 아이디는 영어, 숫자, '_', '.' 만 포함할 수 있으며, 첫 글자에 '_', '.'는 사용할 수 없습니다.")
        private String loginId;

        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Email
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.", groups = KnowOldPassword.class)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String oldPassword;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String newPassword;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String confirmNewPassword;

        @NotEmpty(message = "please enter passKey", groups = UnKnowOldPassword.class)
        private String passKey;
    }

    @Getter
    @Setter
    @ToString
    public static class SignIn {
        @NotEmpty(message = "로그인 아이디는 필수 입력값입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9][a-zA-Z0-9_.]{2,14}$",
                message = "로그인 아이디는 영어, 숫자, '_', '.' 만 포함할 수 있으며, 첫 글자에 '_', '.'는 사용할 수 없습니다.")
        private String loginId;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(loginId, password);
        }
    }

    @Getter
    @Setter
    public static class SignOut {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class Reissue {
        @NotEmpty(message = "로그인 아이디를 입력해주세요.")
        private String loginId;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;

        @NotEmpty(message = "로그인 유형을 입력해주세요.")
        private String provider;

        public Reissue(String loginId, String refreshToken, String provider) {
            this.loginId = loginId;
            this.refreshToken = refreshToken;
            this.provider = provider;
        }
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @ToString
    public static class ResetPassword {
        private String email;
        private String phone;
        private String authorityLink;
    }

}
