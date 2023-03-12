package com.coachapp.coach_pc.controller;

import com.coachapp.coach_pc.request.chat.MessageRequest;
import com.coachapp.coach_pc.service.chat.MessageService;
import com.coachapp.coach_pc.view.chat.MessageViewModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<MessageViewModel> createMessage(@RequestBody MessageRequest message) {
        System.out.println("Made it");
        //return new ResponseEntity<>(null, HttpStatus.CREATED);
        return messageService.createMessage(message);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MessageViewModel> updateMessage(@PathVariable UUID id, @RequestBody MessageRequest message) {
        return messageService.updateMessage(message);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        return messageService.deleteMessage(id);
    }
}
