package com.flat.swiss.config;

import com.flat.swiss.helper.EmailCodeHelper;
import org.openapitools.model.SupportRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderConfig {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private EmailCodeHelper emailCodeHelper;

    @Value("${base.url}")
    private String apiUrl;

    public void sendVerificationEmail(String toEmail, Integer code) {
        if (toEmail != null && !toEmail.isEmpty()) {
            String subject = "Email Verification";
            String url = apiUrl + "/validation/" + emailCodeHelper.encodeEmail(toEmail) + "/" + code;
            String message = "Please click the link below to verify your email: " + url;

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(toEmail);
            email.setSubject(subject);
            email.setText(message);

            email.setFrom("betcsapata@belsoellenorzes.com");

            mailSender.send(email);
        }
    }

    public void sendForgetPasswordEmail(String toEmail) {
        if (toEmail != null && !toEmail.isEmpty()) {
            String subject = "Email Verification";
            // TODO change to original site page
            String url = apiUrl + "/realpagesite/" + emailCodeHelper.encodeEmail(toEmail);
            String message = "Please click the link below to change your password: " + url;

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(toEmail);
            email.setSubject(subject);
            email.setText(message);

            email.setFrom("betcsapata@belsoellenorzes.com");

            mailSender.send(email);
        }
    }

    public void sendSupportRequest(SupportRequestDTO supportRequestDTO) {
        if(supportRequestDTO != null) {
            //User
            String subject = "Support";
            String message = "Thank you for contacting us! Our team is working on your request.";

            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(supportRequestDTO.getEmail());
            email.setSubject(subject);
            email.setText(message);

            email.setFrom("betcsapata@belsoellenorzes.com");

            mailSender.send(email);

            //Admin
            String subjectToAdmin = supportRequestDTO.getSubject();
            String messageToAdmin = "User:" + supportRequestDTO.getEmail() + "asked for help with the following topic:\n" + supportRequestDTO.getDetails();

            SimpleMailMessage emailAdmin = new SimpleMailMessage();
            email.setTo("kramerbala@gmail.com");
            email.setSubject(subjectToAdmin);
            email.setText(messageToAdmin);

            email.setFrom("betcsapata@belsoellenorzes.com");

            mailSender.send(emailAdmin);
        }
    }
}
