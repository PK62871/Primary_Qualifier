package com.example.service.Impl;

import com.example.service.MessageService;
import org.springframework.stereotype.Service;

@Service("sMSService")
public class SMSService implements MessageService {
    @Override
    public void sendMessage(String message) {
        System.out.println("SMS sent: " + message);
    }
}