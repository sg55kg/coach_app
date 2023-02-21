package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.model.chat.Message;
import com.coachapp.coach_pc.request.chat.MessageRequest;
import com.coachapp.coach_pc.service.chat.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<Message> createMessage(@RequestBody MessageRequest message) {
        System.out.println("Made it");
        //return new ResponseEntity<>(null, HttpStatus.CREATED);
        return messageService.createMessage(message);
    }

    @GetMapping("/chat/{chatId}")
    public ResponseEntity<List<Message>> getMessagesByChatRoomId(@PathVariable UUID chatId) {
        return messageService.getMessagesByChatRoomId(chatId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Message> updateMessage(@PathVariable UUID id, @RequestBody MessageRequest message) {
        return messageService.updateMessage(message);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        return messageService.deleteMessage(id);
    }
}
