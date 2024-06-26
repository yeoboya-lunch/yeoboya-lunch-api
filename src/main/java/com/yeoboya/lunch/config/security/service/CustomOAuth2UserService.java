package com.yeoboya.lunch.config.security.service;

import com.yeoboya.lunch.api.v1.member.domain.Member;
import com.yeoboya.lunch.api.v1.member.repository.MemberRepository;
import com.yeoboya.lunch.config.security.constants.Authority;
import com.yeoboya.lunch.config.security.repository.RoleRepository;
import com.yeoboya.lunch.config.security.response.OAuth2Attribute;
import com.yeoboya.lunch.config.util.YeoboyaUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(registrationId, userNameAttributeName, attributes);

        Map<String, Object> memberAttribute = oAuth2Attribute.convertToMap();
        String email = (String) memberAttribute.get("email");
        String name = (String) memberAttribute.get("name");
        String provider = (String) memberAttribute.get("provider"); //socialType

        Member member = memberRepository.findByEmailAndProvider(email, provider)
                .orElse(Member.builder()
                        .loginId("@"+YeoboyaUtils.generateRandomString(7))
                        .email(email)
                        .name(name)
                        .provider(provider)
                        .role(roleRepository.findByRole(Authority.ROLE_GUEST))
                        .providerId(attributes.toString())
                        .build());

        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(member.getRole().getRole().name());

        return new OAuth2UserImpl(Collections.singleton(simpleGrantedAuthority), memberAttribute, "email", member);
    }
}
