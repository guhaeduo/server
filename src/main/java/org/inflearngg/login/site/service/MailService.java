package org.inflearngg.login.site.service;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.inflearngg.login.site.redis.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final RedisUtil redisUtil;

    public MailService(JavaMailSender javaMailSender, RedisUtil redisUtil) {
        this.javaMailSender = javaMailSender;
        this.redisUtil = redisUtil;
    }

    // 보낼 이메일 만드는 서비스
    public MimeMessage createEmail(String email, String code) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(Message.RecipientType.TO, email);
        message.setSubject("구해듀오 인증 번호입니다.");
        message.setText("이메일 인증코드 : " + code);
        message.setFrom("guhaeduo@naver.com");
        return message;
    }

    // 이메일 보내는 서비스
    public void sendMail(String email, String code) throws Exception {
        try {
            MimeMessage mimeMessage = createEmail(email, code);
            log.info("이메일 전송 중");
            javaMailSender.send(mimeMessage);
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
    private void saveRedisAuthNumber(String email, String authNumber, long expireTime) {


        redisUtil.setDataExpire(email, authNumber, expireTime);
    }

    public String sendCertificationMail(String email) throws Exception {
        String code = createAuthNumber();
        sendMail(email, code);
        saveRedisAuthNumber(email, code, 180L); //3분
        return code;

    }
}