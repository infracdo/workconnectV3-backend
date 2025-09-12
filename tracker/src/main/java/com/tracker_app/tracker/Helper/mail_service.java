package com.tracker_app.tracker.Helper;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class mail_service {
    
    @Autowired
    private JavaMailSender emailsender;

    public void processNotification(JSONObject request) {
        try {
            MimeMessage message = emailsender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setFrom("workconnect@apolloglobal.net");
            helper.setTo(request.getString("to"));
            helper.setText(request.getString("text"), true);
            helper.setSubject(request.getString("subject"));
            emailsender.send(message);
        } catch (Exception e) {
            String error = String.format("Error on sending EMAIL : %s", e.getMessage());
            throw new RuntimeException(error, e);
        }
    }
}
