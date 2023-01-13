package com.codueon.boostUp.global.event.email.service;

import com.codueon.boostUp.domain.member.service.MemberDbService;
import com.codueon.boostUp.global.event.email.dto.PostEmailCode;
import com.codueon.boostUp.domain.member.exception.AuthException;
import com.codueon.boostUp.global.exception.BusinessLogicException;
import com.codueon.boostUp.global.exception.ExceptionCode;
import com.codueon.boostUp.global.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;
    private final RedisUtils redisUtils;
    private final MemberDbService memberDbService;

    @Value("${mail.gmail-admin-id}")
    private String adminAddress;

    /**
     * Email 인증코드 전송 메서드
     * @param to 전송할 이메일
     * @author mozzi327
     */
    @Async
    @SneakyThrows
    public void sendAuthorizationCodeToMemberEmail(String to) {
        redisUtils.deleteEmailCode(to);
        MimeMessage message = createAuthorizationCodeMessage(to);
        try {
            emailSender.send(message);
        } catch (MailException es) {
            es.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * 이메일 인증 코드 유효성 확인 메서드
     * @param emailCode 이메일 코드 인증 정보
     * @author mozzi327
     */
    @Transactional
    public void isRightEmailAuthorizationCode(PostEmailCode emailCode) {
        if (!memberDbService.checkExistEmail(emailCode.getEmail()))
            throw new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND);
        String findEmailCode = redisUtils.getEmailAuthorizationCode(emailCode.getEmail());
        if(!findEmailCode.equals(emailCode.getEmailCode())) throw new AuthException(ExceptionCode.INVALID_EMAIL_CODE);
        redisUtils.deleteEmailCode(emailCode.getEmail());
        redisUtils.setEmailAuthorizationCode(emailCode.getEmail(), findEmailCode);
    }

    /**
     * Email 메시지 생성 메서드
     * @param to 전송할 이메일
     * @return MimeMessgae
     * @author mozzi327
     */
    @SneakyThrows
    private MimeMessage createAuthorizationCodeMessage(String to) {
        MimeMessage message = emailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, to);
        message.setSubject("CodueOn 비밀번호 이메일 인증");
        String createAuthorizationCode = createCode();
        String msg;
        StringBuilder msgBuilder = new StringBuilder();
        msgBuilder.append("<div style='margin:100px;'>");
        msgBuilder.append("  <h1> 안녕하세요. CodueOn입니다. </h1>");
        msgBuilder.append("  <br>");
        msgBuilder.append("  <p>아래 코드를 비밀번호 찾기 창으로 돌아가 입력해주세요.<p>");
        msgBuilder.append("  <br>");
        msgBuilder.append("  <p>감사합니다!</p>");
        msgBuilder.append("  <br>");
        msgBuilder.append("  <h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>");
        msgBuilder.append("  <div style='font-size: 130%'>");
        msgBuilder.append("    CODE : <strong>").append(createAuthorizationCode).append("</strong><br/> ");
        msgBuilder.append("  </div>");
        msgBuilder.append("</div>");
        msg = msgBuilder.toString();
        message.setText(msg, "UTF-8", "HTML");
        message.setFrom(new InternetAddress(adminAddress, "Codueon"));
        redisUtils.setEmailAuthorizationCode(to, createAuthorizationCode);
        return message;
    }

    /**
     * 인증 코드 랜덤 생성 메서드
     * @return String(EmailAuthorizationCode)
     * @author mozzi327
     */
    private String createCode() {
        StringBuffer code = new StringBuffer();
        Random rnd = new Random();

        int idx;
        for (int i = 0; i < 8; i++) {
            idx = rnd.nextInt(3);
            switch (idx) {
                case 0:
                    code.append((char) ((int) (rnd.nextInt(26)) + 97));
                    break;
                case 1:
                    code.append((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    code.append((rnd.nextInt(10)));
                    break;
            }
        }
        return code.toString();
    }
}
