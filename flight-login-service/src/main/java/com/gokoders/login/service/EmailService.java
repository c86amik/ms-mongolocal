/**
 * 
 */
package com.gokoders.login.service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.gokoders.login.model.NotificationMail;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author gokoders
 *
 */
@Service
public class EmailService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
    private JavaMailSender emailSender;
    @Autowired
    private Configuration freemarkerConfig;
    
    public void sendEmail(NotificationMail notificationMail, String emailTemplateName) 
    		throws MessagingException, IOException, TemplateException {
    	LOGGER.info("Sending Registration Email to the Registered User");
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                StandardCharsets.UTF_8.name());

        helper.addAttachment("logo.png", new ClassPathResource("/static/images/mail-logo.png"));
        freemarkerConfig.setClassForTemplateLoading(this.getClass(), "/emailtemplates");

//        Template t = freemarkerConfig.getTemplate("email-template.ftl");
        Template t = freemarkerConfig.getTemplate(emailTemplateName);
        String html = FreeMarkerTemplateUtils.processTemplateIntoString(t, notificationMail.getModel());

        helper.setTo(notificationMail.getTo());
        helper.setText(html, true);
        helper.setSubject(notificationMail.getSubject());
        helper.setFrom(notificationMail.getFrom());
        emailSender.send(message);
        LOGGER.info("Email sent to the registered user of mail Id " + notificationMail.getTo());
    }

}
