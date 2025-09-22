package com.example.controller;

import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/qualifier")
public class QualifierMessageController {

    private final MessageService messageService;

    public QualifierMessageController(@Qualifier("sMSService") MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/send")
    public String send() {
        messageService.sendMessage("Hello!");
        return "Message sent!";
    }
}
