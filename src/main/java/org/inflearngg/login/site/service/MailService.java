package org.inflearngg.login.site.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.jwt.token.AuthToken;
import org.inflearngg.login.site.exception.ExpireEmailCodeException;
import org.inflearngg.login.site.exception.InvalidEmailCodeException;
import org.inflearngg.login.site.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    @Value("${mail.reset.redirect}")
    private String redirectUri;
    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;



    // 보낼 이메일 만드는 서비스
    public MimeMessage createCodeEmail(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("구해듀오 인증 번호입니다.");
        message.setText("이메일 인증코드 : " + code);
        message.setFrom(new InternetAddress("guhaeduo@naver.com", "구해듀오"));
        return message;
    }
    public MimeMessage createResetPasswordEmail(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("구해듀오 비밀번호 재설정입니다.");
        message.setText("비밀번호 재설정 코드 : "+ redirectUri + code);
        message.setFrom(new InternetAddress("guhaeduo@naver.com", "구해듀오"));
        return message;
    }

    // 이메일 보내는 서비스
    public void sendMail(MimeMessage email) throws Exception {
        try {
            log.info("이메일 전송 중");
            javaMailSender.send(email);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }

        // 이메일 보내는 코드
    }

    // 인증 번호 만드는 서비스
    private String createAuthNumber() {
        return UUID.randomUUID().toString().substring(0, 6); //랜덤 인증번호 uuid를 이용!
    }

    // 해당 인증번호와 이메일, 만료기한을 저장하고 관리하는 서비스
    private void saveRedisAuthNumber(String email, String authNumber, Long expireTime) {
        redisUtil.setDataExpire(email, authNumber, expireTime);
    }

    public void verifyEmailCode(String email, String code) {
        String redisCode = redisUtil.getData(email);
        if (redisCode == null) {
            //
            throw new ExpireEmailCodeException();
        }
        if (redisCode.equals(code)) {
            redisUtil.deleteData(email);
            return;
        }
        //인증번호가 일치하지 않음
        throw new InvalidEmailCodeException();
    }

    public void sendCertificationMail(String email) throws Exception {
        String code = createAuthNumber();
        //메일 생성
        sendMail(createCodeEmail(email, code));
        saveRedisAuthNumber(email, code, 1800L); //30분
//        return code;
    }
    //이메일 비밀번호 바꾸기
    public void sendPasswordResetMail(String email, String code) throws Exception {
        sendMail(createResetPasswordEmail(email, code));
        // 레디스 저장할 필요 없음
    }




}