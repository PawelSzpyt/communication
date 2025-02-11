package com.example.communication.service.channel;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


@ApplicationScoped
public class EmailConfig {

    private String host;
    private String port;
    private String user;
    private String password;
    private String from;
    private boolean startTls;

    @PostConstruct
    public void init() {
        try (InputStream input = getClass().getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found!");
            }
            Properties prop = new Properties();
            prop.load(input);

            this.host = prop.getProperty("SMTP_HOST", "smtp.example.com");
            this.port = prop.getProperty("SMTP_PORT", "587");
            this.user = prop.getProperty("SMTP_USER", "user");
            this.from = prop.getProperty("SMTP_FROM", "your-email@example.com");
            this.password = prop.getProperty("SMTP_PASSWORD", "your-password");
            this.startTls = Boolean.parseBoolean(prop.getProperty("SMTP_STARTTLS", "true"));


        } catch (IOException e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public String getFrom() {
        return from;
    }

    public boolean isStartTls() {
        return startTls;
    }
}