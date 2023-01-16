package com.codueon.boostUp.global.security.token;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String accessToken;
    private Object principal;
    private Object credential;
    private Long id;

    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities, Object principal, Object credential, Long id) {
        super(authorities);
        this.principal = principal;
        this.credential = credential;
        this.id = id;
        this.setAuthenticated(true);
    }

    public JwtAuthenticationToken(String accessToken) {
        super(null);
        this.accessToken = accessToken;
        this.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.credential;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    public String getAccessToken() {
        return this.accessToken;
    }
}
