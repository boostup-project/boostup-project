package com.codueon.boostUp.global.security.provider;

import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final String ROLES = "roles";
    private static final String USERNAME = "username";
    private final JwtTokenUtils jwtTokenUtils;
    private final CustomAuthorityUtils authorityUtils;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        Map<String, Object> claims = jwtTokenUtils.getClaims(jwtToken.getAccessToken());
        String email = (String) claims.get(USERNAME);
        List<String> roles = (List<String>) claims.get(ROLES);

        List<GrantedAuthority> authorities = authorityUtils.createAuthorities(roles);

        return new JwtAuthenticationToken(authorities, email, null);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
