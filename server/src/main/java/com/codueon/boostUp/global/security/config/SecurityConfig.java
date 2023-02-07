package com.codueon.boostUp.global.security.config;

import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.security.details.OAuth2DetailService;
import com.codueon.boostUp.global.security.filter.JwtVerificationFilter;
import com.codueon.boostUp.global.security.handler.OAuth2FailureHandler;
import com.codueon.boostUp.global.security.handler.OAuth2SuccessHandler;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RedisUtils redisUtils;
    private final ObjectMapper objectMapper;
    private final JwtTokenUtils jwtTokenUtils;
    private final MemberRepository memberRepository;
    private final CustomAuthorityUtils authorityUtils;
    private final OAuth2DetailService oAuth2DetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        http
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(encodingFilter, CsrfFilter.class)
                .headers().frameOptions().disable()
                .and()
                .csrf().disable()
                .cors().configurationSource(corsConfigurationSource())
                .and()
                .apply(new CustomFilterConfig())
                .and()
                .authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/lesson/**").permitAll()
                //.antMatchers(HttpMethod.POST, "/lesson/registration").hasAuthority("ROLE_USER")
                .antMatchers("/suggest/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/ws/chat/**").permitAll()
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .and()
                .oauth2Login(oauth2 -> {
                    oauth2.userInfoEndpoint().userService(oAuth2DetailService);
                    oauth2.successHandler(new OAuth2SuccessHandler(memberRepository, jwtTokenUtils));
                    oauth2.failureHandler(new OAuth2FailureHandler());
                });
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:8080");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedOrigin("http://codeuon.s3-website.ap-northeast-2.amazonaws.com");
        configuration.addAllowedOrigin("https://d12vhbt0xdnnpo.cloudfront.net");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        configuration.addExposedHeader("Authorization");
        configuration.addExposedHeader("RefreshToken");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * 커스텀 필터 클래스
     */
    public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            JwtVerificationFilter jwtVerificationFilter =
                    new JwtVerificationFilter(redisUtils, jwtTokenUtils, authorityUtils);

            builder.addFilterBefore(jwtVerificationFilter, OAuth2LoginAuthenticationFilter.class)
                    .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class);
        }
    }
}
