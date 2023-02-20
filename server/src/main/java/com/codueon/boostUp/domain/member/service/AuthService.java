package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.lesson.repository.LessonRepository;
import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.dto.AuthDto;
import com.codueon.boostUp.domain.member.dto.PostLogin;
import com.codueon.boostUp.domain.member.dto.TokenDto;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.security.token.JwtAuthenticationToken;
import com.codueon.boostUp.global.security.utils.JwtTokenUtils;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.codueon.boostUp.global.security.utils.AuthConstants.AUTHORIZATION;
import static com.codueon.boostUp.global.security.utils.AuthConstants.REFRESH_TOKEN;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {
    private final MemberDbService memberDbService;
    private final LessonDbService lessonDbService;
    private final JwtTokenUtils jwtTokenUtils;
    private final RedisUtils redisUtils;

    /**
     * 사용자 로그인
     * @param login 로그인 정보
     * @return
     * @author LimJaeminZ
     */
    public TokenDto.Response loginMember(PostLogin login) {
        Member findMember = memberDbService.ifExistsMemberByEmail(login.getEmail());

        memberDbService.isValidPassword(findMember, login.getPassword());

        String generateAccessToken = jwtTokenUtils.generateAccessToken(findMember);
        String generateRefreshToken = jwtTokenUtils.generateRefreshToken(findMember);

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateAccessToken);

        //redis 저장
        redisUtils.setData(findMember.getEmail(), findMember.getAccountStatus().getProvider(), generateRefreshToken, jwtTokenUtils.getRefreshTokenExpirationMinutes());
        Boolean lessonExistence = lessonDbService.ifExistsByMemberId(findMember.getId());
        AuthDto memberRes = AuthDto.builder()
                .memberId(findMember.getId())
                .memberImage(findMember.getMemberImage().getFilePath())
                .email(findMember.getEmail())
                .name(findMember.getName())
                .lessonExistence(lessonExistence)
                .build();

        return TokenDto.Response.builder()
                .response(memberRes)
                .headers(headers)
                .build();
    }

    /**
     * 사용자 로그아웃
     * @param token 사용자 인증 정보
     * @author LimJaeminZ
     */
    public void logoutMember(JwtAuthenticationToken token) {
        Member findMember = memberDbService.ifExistsReturnMember(token.getId());

        // refreshToken 존재 시 삭제
        redisUtils.deleteData(findMember.getEmail(), findMember.getAccountStatus().getProvider());

        // accessToken 만료 전까지 블랙리스트 처리
        Long expiration = jwtTokenUtils.getExpiration(token.getAccessToken());
        if (expiration > 0) redisUtils.setBlackList(token.getAccessToken(), "Logout", expiration);
    }

    /**
     * 토큰 재발급 메서드
     * @param accessToken 엑세스 토큰
     * @param refreshToken 리프레시 토큰
     * @param memberId 사용자 식별자
     * @return TokenDto.Response
     * @author LimJaeminZ
     */
    public TokenDto.Response reIssueToken(String accessToken, String refreshToken, Long memberId) {

        if(jwtTokenUtils.validateToken(refreshToken)) {
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        }

        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        String refresh = (String) redisUtils.getData(findMember.getEmail(), findMember.getAccountStatus().getProvider());

        if(!refreshToken.equals(refresh)) {
            throw new AuthException(ExceptionCode.INVALID_AUTH_TOKEN);
        }

        // 새로운 토큰 생성
        String generateToken = jwtTokenUtils.generateAccessToken(findMember);

        // refreshToken Redis 업데이트
        redisUtils.setData(findMember.getEmail(), findMember.getAccountStatus().getProvider(), generateToken, jwtTokenUtils.getRefreshTokenExpirationMinutes());

        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, generateToken);
        headers.add(REFRESH_TOKEN, refreshToken);

        AuthDto memberRes = AuthDto.builder()
                .name(findMember.getName())
                .email(findMember.getEmail())
                .build();

        return TokenDto.Response.builder()
                .headers(headers)
                .response(memberRes)
                .build();
    }
}

