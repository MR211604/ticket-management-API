package com.server.ticketmanagement.services.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String to, String name) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Welcome to SmartPass");
            simpleMailMessage.setText("Dear " + name + ",\n\nWelcome to SmartPass! We're excited to have you on board. If you have any questions or need assistance, feel free to reach out.\n\nBest regards,\nSmartPass");
            mailSender.send(simpleMailMessage);
            log.info("Welcome email sent successfully to {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

    public void sendResetOTPEmail(String to, String name, String otp) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Password reset OTP");
            simpleMailMessage.setText("Dear " + name + ",\n\nYour OTP reset is.\n\n" + otp + "\n\nUse this OTP to proceed with resetting your password." + "\n\nBest regards,\nSmartPass");
            mailSender.send(simpleMailMessage);
            log.info("Reset OTP email successfully sent to {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

    public void sendOTPEmail(String to, String name, String otp) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setSubject("Verify your account");
            simpleMailMessage.setText("Dear " + name + ",\n\nYour OTP code is.\n\n" + otp + "\n\nUse this OTP to proceed with verifying your account." + "\n\nBest regards,\nSmartPass");
            mailSender.send(simpleMailMessage);
            log.info("OTP email successfully sent to {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}. Error: {}", to, e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error while sending email to {}: {}", to, e.getMessage(), e);
            throw e;
        }
    }

}
