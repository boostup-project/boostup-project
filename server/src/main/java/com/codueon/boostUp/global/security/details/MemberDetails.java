package com.codueon.boostUp.global.security.details;

import com.codueon.boostUp.domain.member.entity.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;


import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class MemberDetails implements UserDetails, OAuth2User {
    private final Member member;
    private Map<String, Object> attribute;

    // 일반 로그인 사용자
    public MemberDetails(Member member) {
        this.member = member;
    }

    // OAuth2 로그인 사용자
    public MemberDetails(Member member, Map<String, Object> attribute) {
        this.member = member;
        this.attribute = attribute;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    // 유저 권한 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream()
                .map(role -> (GrantedAuthority) () -> "ROLE_" + role)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}
