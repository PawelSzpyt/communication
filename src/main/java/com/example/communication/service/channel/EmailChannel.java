package com.example.communication.service.channel;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Map;
import java.util.Properties;

@ApplicationScoped
public class EmailChannel implements CommunicationChannel {

    @Inject
    private EmailConfig emailConfig;

    private Properties mailProperties;

    @PostConstruct
    public void init() {
        // Setup mail server properties
        mailProperties = new Properties();
        mailProperties.put("mail.smtp.host", emailConfig.getHost());
        mailProperties.put("mail.smtp.port", emailConfig.getPort());
        mailProperties.put("mail.smtp.auth", "true");
        mailProperties.put("mail.smtp.starttls.enable", emailConfig.isStartTls());
    }

    @Override
    public void send(String body, String deliverySettings) {
        try {
            // Parse deliverySettings JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> settings = mapper.readValue(deliverySettings, Map.class);

            String to = settings.get("to");
            String subject = settings.get("subject");

            if (to == null || subject == null) {
                throw new IllegalArgumentException("Invalid delivery settings: missing required fields");
            }

            // Create a mail session with authentication
            Session session = Session.getInstance(mailProperties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(emailConfig.getUser(), emailConfig.getPassword());
                }
            });

            // Create the email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailConfig.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    @Override
    public String getName() {
        return "email";
    }
}