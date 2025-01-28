package com.example.communication.service.channel;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class EmailConfig {

    private String host;
    private String port;
    private String user;
    private String password;
    private boolean startTls;

    public EmailConfig() {
        // Load configuration from environment variables or application.properties
        this.host = System.getenv().getOrDefault("SMTP_HOST", "smtp.example.com");
        this.port = System.getenv().getOrDefault("SMTP_PORT", "587");
        this.user = System.getenv().getOrDefault("SMTP_USER", "your-email@example.com");
        this.password = System.getenv().getOrDefault("SMTP_PASSWORD", "your-password");
        this.startTls = Boolean.parseBoolean(System.getenv().getOrDefault("SMTP_STARTTLS", "true"));
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

    public boolean isStartTls() {
        return startTls;
    }
}