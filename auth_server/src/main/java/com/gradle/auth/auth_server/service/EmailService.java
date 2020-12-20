package com.gradle.auth.auth_server.service;

import javax.mail.internet.MimeMessage;

public interface EmailService {
    MimeMessage createMessage(String to) throws Exception;

    String sendMessage(String to) throws Exception;
}
