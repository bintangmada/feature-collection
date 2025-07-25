package com.bintang.email_confirmation.listener;

import com.bintang.email_confirmation.model.EmailMessage;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailConfirmationListener {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @JmsListener(destination = "email.queue")
    public void onMessage(EmailMessage message) throws MessagingException{
        Context context = new Context();
        context.setVariables(message.getVariables());

        String body = templateEngine.process(message.getTemplateName(), context);

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setTo(message.getTo());
        helper.setSubject(message.getSubject());
        helper.setText(body, true);

        mailSender.send(mimeMessage);
    }
}
