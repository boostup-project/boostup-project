package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.lesson.service.LessonDbService;
import com.codueon.boostUp.domain.member.dto.PostPasswordInLoginPage;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberDbService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisUtils redisUtils;

    /**
     * 사용자 정보 조회 메서드
     *
     * @param memberId 사용자 식별자
     * @return Member
     * @author LimJaeminZ
     */
    public Member ifExistsReturnMember(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
    /**
     * 사용자 정보 조회 메서드(이메일)
     *
     * @param email 사용자 이메일
     * @return Member
     * @author LimJaeminZ
     */
    public Member ifExistsMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
                new AuthException(ExceptionCode.MEMBER_NOT_FOUND));
    }

    /**
     * 이메일 중복 검사 메서드
     *
     * @param email 사용자 이메일
     * @author LimJaeminZ
     */
    public void verifyEmail(String email) {
        memberRepository.findByEmail(email).ifPresent(e -> {
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXIST);
        });
    }

    /**
     * 닉네임 중복 검사 메서드
     *
     * @param name 사용자 닉네임
     * @author LimJaeminZ
     */
    public void verifyName(String name) {
        memberRepository.findByName(name).ifPresent(e -> {
            throw new BusinessLogicException(ExceptionCode.NICKNAME_ALREADY_EXIST);
        });
    }
    /**
     * 사용자 정보 저장 메서드
     *
     * @param member 사용자 정보
     * @author LimJaeminZ
     */
    public Member saveMember(Member member) {
        return memberRepository.save(member);
    }

    /**
     * 비밀번호 암호화 메서드
     *
     * @param password 회원가입 시 비밀번호
     * @return String(인코딩된 비밀번호)
     * @author LimJaeminZ
     */
    public String encodingPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 비밀번호 확인 메서드
     *
     * @param findMember 조회한 사용자 정보
     * @param password 로그인 시도 비밀번호
     * @author LimJaeminZ
     */
    public void isValidPassword(Member findMember, String password) {
        if(!passwordEncoder.matches(password, findMember.getPassword())) {
            throw new AuthException(ExceptionCode.INVALID_MEMBER);
        }
    }

    /**
     * 사용자 비밀번호 변경 메서드
     * @param changePassword 변경할 비밀번호 정보
     * @author mozzi327
     */
    public void changingPasswordInLoginPage(PostPasswordInLoginPage changePassword) {
        if (redisUtils.getEmailAuthorizationCode(changePassword.getEmail()) == null)
            throw new AuthException(ExceptionCode.INVALID_EMAIL_CODE);
        Member findMember = ifExistsMemberByEmail(changePassword.getEmail());
        findMember.editNewPassword(encodingPassword(changePassword.getChangePassword()));
        saveMember(findMember);
    }

    /**
     * 이메일 중복 여부 조회 메서드
     * @param email 이메일 정보
     * @author mozzi327
     */
    public boolean checkExistEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    /**
     * 닉네임 중복 여부 조회 메서드
     * @param name 닉네임 정보
     * @author mozzi327
     */
    public void checkExistName(String name) {
        if (memberRepository.existsByName(name))
            throw new BusinessLogicException(ExceptionCode.NICKNAME_ALREADY_EXIST);
    }

    /**
     * 사용자 조회 메서드(과외 식별자)
     * @param lessonId 과외 식별자
     * @return Member
     * @author mozzi327
     */
    public Member ifExistsReturnMemberByLessonId(Long lessonId) {
        return memberRepository.getMemberByLessonId(lessonId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
    }
}