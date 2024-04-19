package com.coachapp.coach_pc.controller.chat;

// Unused imports (The Java compiler has to process each import statement, even if the imported class is not used.)
import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import com.coachapp.coach_pc.service.chat.ChatRoomService;
import com.coachapp.coach_pc.view.chat.ChatRoomViewModel;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.http.ResponseEntity;
// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

// Recommended to make arguments final - you want to avoid accidental modification of inputs
// e.g getChatRoom(final @PathVariable UUID id)

// Add logs to every controller route indicating request params (masking private data) and response
@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

    // make instance dependency variables final to avoid accidental overwriting
    private ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomViewModel> getChatRoom(@PathVariable UUID id) {
        return this.chatRoomService.getChatRoom(id);
    }

    @PostMapping("/")
    public ResponseEntity<ChatWithMappings> createChatRoom(@RequestBody ChatRoomRequest chatRoom) {
        return this.chatRoomService.createChatRoom(chatRoom);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChatRoom(@PathVariable UUID id) {
        return this.chatRoomService.deleteChatRoom(id);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ChatWithMappings>> getChatRoomsByUserId(@PathVariable UUID userId) {
        return this.chatRoomService.getChatRoomsByUserId(userId);
    }
}
