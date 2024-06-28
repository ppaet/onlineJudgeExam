package com.zeke.service;

public interface MailService {
    boolean sendMail(String to, String code, String text);
}