package com.codueon.boostUp.domain.member.service;

import com.codueon.boostUp.domain.member.dto.PostMember;
import com.codueon.boostUp.domain.member.dto.PostName;
import com.codueon.boostUp.domain.member.dto.PostPasswordInLoginPage;
import com.codueon.boostUp.domain.member.dto.PostPasswordInMyPage;
import com.codueon.boostUp.domain.member.entity.AccountStatus;
import com.codueon.boostUp.domain.member.entity.Member;
import com.codueon.boostUp.domain.member.entity.MemberImage;
import com.codueon.boostUp.domain.member.repository.MemberRepository;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.file.AwsS3Service;
import com.codueon.boostUp.global.file.FileHandler;
import com.codueon.boostUp.global.file.UploadFile;
import com.codueon.boostUp.global.security.utils.CustomAuthorityUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberDbService memberDbService;
    private final CustomAuthorityUtils authorityUtils;
    private final MemberRepository memberRepository;
    private final FileHandler fileHandler;
    private final AwsS3Service awsS3Service;


    @Value("${default.image.address}")
    private String defaultImageAddress;

    /**
     * 회원가입 메서드
     *
     * @param postMember 회원가입 정보
     * @author LimJaeminZ
     */
    public void createMember(PostMember postMember) {
        memberDbService.verifyName(postMember.getName());
        memberDbService.verifyEmail(postMember.getEmail());

        List<String> roles = authorityUtils.createRoles(postMember.getEmail());

        Member member = Member.builder()
                .email(postMember.getEmail())
                .password(memberDbService.encodingPassword(postMember.getPassword()))
                .name(postMember.getName())
                .accountStatus(AccountStatus.COMMON_MEMBER)
                .roles(roles)
                .build();

        MemberImage memberImageS3 = MemberImage.builder()
                .filePath(defaultImageAddress)
                .build();
        member.addMemberImage(memberImageS3);

        memberDbService.saveMember(member);
    }

    /**
     * password 변경 메서드(로그인페이지)
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    public void changePasswordInLoginPage(PostPasswordInLoginPage changePassword) {
        memberDbService.changingPasswordInLoginPage(changePassword);
    }

    /**
     * 이메일 중복 확인 메서드
     * @param email 이메일 정보
     * @author mozzi327
     */
    public void checkIsOverLappedEmail(String email) {
        if(memberDbService.checkExistEmail(email))
            throw new BusinessLogicException(ExceptionCode.EMAIL_ALREADY_EXIST);
    }

    /**
     * 이메일 존재 여부 확인 메서드
     * @param email 이메일 정보
     * @author mozzi327
     */
    public void checkIsExistEmailInDb(String email) {
        if (!memberDbService.checkExistEmail(email))
            throw new BusinessLogicException(ExceptionCode.EMAIL_NOT_FOUND);
    }

    /**
     * 닉네임 중복 확인 메서드
     * @param name 닉네임 정보
     * @author mozzi327
     */
    public void checkIsOverLappedName(String name) {
        memberDbService.checkExistName(name);
    }

    /**
     * 비밀번호 일치 확인 메서드
     * @param password 검증 비밀번호 정보
     * @param memberId 사용자 식별자
     * @author mozzi327
     */
    public void checkIsRightPassword(String password, Long memberId) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        memberDbService.isValidPassword(findMember, password);
    }

    /**
     * 비밀번호 변경 메서드(마이페이지)
     * @param memberId 사용자 식별자
     * @param changePassword 비밀번호 변경 정보
     * @author mozzi327
     */
    public void changePasswordInMyPage(Long memberId, PostPasswordInMyPage changePassword) {
        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        findMember.editNewPassword(memberDbService.encodingPassword(changePassword.getChangePassword()));
        memberDbService.saveMember(findMember);
    }

    @SneakyThrows
    public void changeMemberInfo(PostName name, MultipartFile file, Long memberId) {

        if (file.isEmpty() && name.getName().isEmpty()) return;

        Member findMember = memberDbService.ifExistsReturnMember(memberId);
        UploadFile uploadFile = fileHandler.uploadFile(file);

//        String dir = "memberImage";
//        UploadFile uploadFile = awsS3Service.uploadfile(file, dir);

        if (!file.isEmpty()) {
            MemberImage memberImage = MemberImage.builder()
                    .filePath(uploadFile.getFilePath())
                    .fileName(uploadFile.getFileName())
                    .fileSize(uploadFile.getFileSize())
                    .originFileName(uploadFile.getOriginFileName())
                    .build();
            findMember.addMemberImage(memberImage);
        }

        if (!name.getName().isEmpty()) findMember.setName(name.getName());

        memberDbService.saveMember(findMember);
    }
}
