package com.codueon.boostUp.global.security.details;

import com.codueon.boostUp.domain.member.entity.AccountStatus;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.oauth.OAuth2Attribute;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2DetailService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final MemberRepository memberRepository;


    /**
     * 소셜 로그인 이후 사용자 정보 저장 메서드
     * @param userRequest 사용자 정보
     * @return OAuth2User
     * @throws OAuth2AuthenticationException
     * @author LimJaeminZ
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String attributeKey = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuth2Attribute oAuth2Attribute = OAuth2Attribute.of(provider, attributeKey, oAuth2User.getAttributes());

        String email = oAuth2Attribute.getEmail();
        String name = oAuth2Attribute.getName();

        Member oauthMember = saveOrUpdate(email, name);

        return new MemberDetails(oauthMember, oAuth2Attribute.getAttributes());
    }

    /**
     * OAuth2 로그인 사용자 저장
     * @param email 사용자 이메일
     * @param name 사용자 이름
     * @return Member
     * @author LimJaeminZ
     */
    private Member saveOrUpdate(String email, String name) {
        Optional<Member> findMember = memberRepository.findByEmail(email);
        if(findMember.isPresent()) {
            return findMember.get();
        }
        Member member = Member.builder()
                .name(name)
                .email(email)
                .accountStatus(AccountStatus.GOOGLE_MEMBER)
                .build();

        return memberRepository.save(member);
    }
}
