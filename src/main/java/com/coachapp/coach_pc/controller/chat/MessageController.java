package com.coachapp.coach_pc.controller.chat;

import com.blazebit.persistence.PagedList;
import com.coachapp.coach_pc.request.chat.MessageRequest;
import com.coachapp.coach_pc.service.chat.MessageService;
import com.coachapp.coach_pc.view.chat.MessageViewModel;
import org.springframework.http.ResponseEntity;
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)

// Add logs to every controller route indicating request params (masking private data) and response
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    // make instance dependency variables final to avoid accidental overwriting
    private MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/")
    public ResponseEntity<MessageViewModel> createMessage(@RequestBody MessageRequest message) {
        return messageService.createMessage(message);
    }

    // id route param is unnecessary - route is already PUT so you can take the message request directly (it contains the id)
    @PutMapping("/{id}")
    public ResponseEntity<MessageViewModel> updateMessage(@PathVariable UUID id, @RequestBody MessageRequest message) {
        return messageService.updateMessage(message);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable UUID id) {
        return messageService.deleteMessage(id);
    }


    // Recommend to have a parameter that allows you to modify how many messages you are fetching and setting 20 to default
    // improves readability and makes this a lot more flexible
    @GetMapping("/chat/{chatId}")
    public ResponseEntity<PagedList<MessageViewModel>> fetchNext20Messages(@PathVariable UUID chatId,
                                                                           @RequestParam int start,
                                                                           @RequestParam int end) {
        return messageService.fetchNext20Messages(chatId, start, end);
    }
}
