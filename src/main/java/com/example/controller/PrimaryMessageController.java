package com.example.controller;

import com.example.service.MessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/primary")
public class PrimaryMessageController {

    private final MessageService messageService;

    public PrimaryMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/send")
    public String send() {
        messageService.sendMessage("Hello!");
        return "Message sent!";
    }
}