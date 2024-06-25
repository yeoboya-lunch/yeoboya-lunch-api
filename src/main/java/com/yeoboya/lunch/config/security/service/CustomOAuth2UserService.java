package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.domain.MemberInfo;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.domain.MemberRole;
import com.yeoboya.lunch.config.security.domain.Role;
import com.yeoboya.lunch.config.security.domain.UserSecurityStatus;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import com.yeoboya.lunch.config.security.response.OAuth2Attribute;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute =
                OAuth2Attribute.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();
        String email = (String) memberAttribute.get("email");
        String name = (String) memberAttribute.get("name");
//todo
        Optional<Member> findMember = memberRepository.findByEmail(email);

        SimpleGrantedAuthority authority;

        if (findMember.isPresent()) {
            memberAttribute.put("memberExists", true);
            Member member = findMember.get();
            Role role = member.getMemberRoles().get(0).getRole();
            authority = new SimpleGrantedAuthority(String.valueOf(role));

        } else {
            memberAttribute.put("memberExists", false);
            authority = new SimpleGrantedAuthority("ROLE_USER");

            Member build = Member.builder()
                    .loginId(String.valueOf(UUID.randomUUID()))
                    .email(email)
                    .name(name)
                    .provider(userRequest.getClientRegistration().getRegistrationId())
                    .providerId(oAuth2User.getName())
                    .build();
            Role role = roleRepository.findByRole(Authority.ROLE_USER);
            List<MemberRole> memberRoles = List.of(MemberRole.createMemberRoles(build, role));
            MemberInfo memberInfo = MemberInfo.createMemberInfo(build);
            UserSecurityStatus userSecurityStatus = UserSecurityStatus.createUserSecurityStatus(build);
            Member saveMember = Member.createMember(build, memberInfo, memberRoles, userSecurityStatus);

            memberRepository.save(saveMember);
        }

        return new DefaultOAuth2User(
                Collections.singleton(authority), memberAttribute, "email"
        );


    }
}
