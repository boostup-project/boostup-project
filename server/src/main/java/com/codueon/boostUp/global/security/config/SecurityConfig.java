package com.codueon.boostUp.global.security.config;

import com.codueon.boostUp.global.security.filter.JwtVerificationFilter;
import com.codueon.boostUp.global.security.provider.JwtAuthenticationProvider;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.security.utils.RedisUtils;
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
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

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
                .cors().disable() //.cors(withDefaults())
                .csrf().disable()
                .apply(new CustomFilterConfig())
                .and()
                .authorizeRequests()
                .antMatchers("/h2/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/member/**").permitAll()
                .antMatchers("/lesson/**").permitAll()
                .antMatchers("/suggest/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .anyRequest().permitAll();

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "DELETE"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    /**
     * 커스텀 필터 클래스
     *
     */
    public class CustomFilterConfig extends AbstractHttpConfigurer<CustomFilterConfig, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(authenticationManager, jwtTokenUtils, redisUtils);

            builder
                    .addFilterBefore(jwtVerificationFilter, UsernamePasswordAuthenticationFilter.class)
                    .authenticationProvider(jwtAuthenticationProvider);
        }
    }
}
