package com.mungta.accusation.service;

import com.mungta.accusation.domain.AccusedMember;
import com.mungta.accusation.exception.ApiException;
import com.mungta.accusation.exception.ApiStatus;
import com.mungta.accusation.handler.MailHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Slf4j
@RequiredArgsConstructor
@Service
public class PenaltyMailService {

    private static final String PENALTY_TITLE = "[뭉타] 패널티 부여";
    private static final String PENALTY_TEMPLATE_NAME = "mail_penalty";
    private static final String CONTEXT_KEY = "name";

    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void send(AccusedMember accusedMember) {
        log.info("PenaltyMail send to {}", accusedMember.getEmail());

        try {
            MailHandler mailHandler = new MailHandler(mailSender);

            // 받는 사람
            mailHandler.setTo(accusedMember.getEmail());

            // 보내는 사람
            mailHandler.setFrom(fromEmail);

            // 제목
            mailHandler.setSubject(PENALTY_TITLE);

            Context context = new Context();
            context.setVariable(CONTEXT_KEY, accusedMember.getName());

            // HTML Layout
            String html = templateEngine.process(PENALTY_TEMPLATE_NAME, context);
            mailHandler.setText(html, true);

            mailHandler.send();
        } catch(Exception e) {
            throw new ApiException(ApiStatus.SEND_EMAIL_ERROR, e);
        }
    }

}
