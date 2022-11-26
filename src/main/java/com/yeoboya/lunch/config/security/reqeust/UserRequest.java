package com.yeoboya.lunch.config.security.reqeust;

import com.yeoboya.lunch.config.security.validation.ValidationGroups;
import com.yeoboya.lunch.config.security.validation.ValidationGroups.UmKnowOldPassword;
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

        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @Length(min = 2, max = 6)
        @NotEmpty(message = "이름은 필수 입력값입니다.")
        private String name;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String password;
    }

    @Getter
    @Setter
    @ToString
    public static class Password {

        @NotEmpty(message = "이메일은 필수 입력값입니다.", groups = {ValidationGroups.KnowOldPassword.class, UmKnowOldPassword.class})
        @Email(message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.", groups = ValidationGroups.KnowOldPassword.class)
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String oldPassword;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.", groups = {ValidationGroups.KnowOldPassword.class, UmKnowOldPassword.class})
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String newPassword;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.", groups = {ValidationGroups.KnowOldPassword.class, UmKnowOldPassword.class})
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$", message = "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용하세요.")
        private String ConfirmNewPassword;
    }

    @Getter
    @Setter
    @ToString
    public static class SignIn {
        @NotEmpty(message = "이메일은 필수 입력값입니다.")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식에 맞지 않습니다.")
        private String email;

        @NotEmpty(message = "비밀번호는 필수 입력값입니다.")
        private String password;

        public UsernamePasswordAuthenticationToken toAuthentication() {
            return new UsernamePasswordAuthenticationToken(email, password);
        }
    }

    @Getter
    @Setter
    public static class SignOut {
        @NotEmpty(message = "Invalid request.")
        private String accessToken;

        @NotEmpty(message = "Invalid request.")
        private String refreshToken;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Reissue {
        @NotEmpty(message = "accessToken 을 입력해주세요.")
        private String accessToken;

        @NotEmpty(message = "refreshToken 을 입력해주세요.")
        private String refreshToken;

        public Reissue(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }
    }


}