package com.codueon.boostUp.global.security.filter;

import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.security.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.codueon.boostUp.global.security.utils.AuthConstants.AUTHORIZATION;
import static com.codueon.boostUp.global.security.utils.AuthConstants.BEARER;

@RequiredArgsConstructor
public class JwtVerificationFilter extends OncePerRequestFilter {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String accessToken = request.getHeader(AUTHORIZATION);

        if(StringUtils.hasText(accessToken) && accessToken.startsWith(BEARER)) {
            accessToken = jwtTokenUtils.parseAccessToken(accessToken);
            Authentication authentication = new JwtAuthenticationToken(accessToken);
            Authentication authenticatedToken = authenticationManager.authenticate(authentication);
            SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
        }
        doFilter(request, response, filterChain);
    }
}
