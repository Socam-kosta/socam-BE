package org.example.socam_be.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    // ✅ 기본 Gmail 설정
    @Value("${spring.mail.username}")
    private String gmailUser;
    @Value("${spring.mail.password}")
    private String gmailPassword;

    // ✅ Naver 설정
    @Value("${custom.mail.naver.username}")
    private String naverUser;
    @Value("${custom.mail.naver.password}")
    private String naverPassword;

    public void sendPasswordResetMail(String to, String token) {
        String resetLink = "http://localhost:3000/reset-password?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[SOCAM] 비밀번호 재설정 안내");
        message.setText(
                "안녕하세요.\n\n아래 링크를 클릭하여 비밀번호를 재설정하세요:\n\n" +
                        resetLink + "\n\n이 링크는 10분 동안만 유효합니다."
        );

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setDefaultEncoding("UTF-8"); // ✅ 한글 깨짐 방지
        sender.setProtocol("smtp");         // ✅ 명시적 프로토콜 설정

        if (to.endsWith("@naver.com")) {
            // ✅ Naver SMTP 설정
            sender.setHost("smtp.naver.com");
            sender.setPort(465);
            sender.setUsername(naverUser);
            sender.setPassword(naverPassword);
            var props = sender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            // ✅ 기본 Gmail SMTP 설정
            sender.setHost("smtp.gmail.com");
            sender.setPort(587);
            sender.setUsername(gmailUser);
            sender.setPassword(gmailPassword);
            var props = sender.getJavaMailProperties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
        }

        sender.send(message);
    }
}
