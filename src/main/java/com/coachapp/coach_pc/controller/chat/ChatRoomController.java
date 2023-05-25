package com.coachapp.coach_pc.controller.chat;

import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import com.coachapp.coach_pc.service.chat.ChatRoomService;
import com.coachapp.coach_pc.view.chat.ChatRoomViewModel;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat-rooms")
public class ChatRoomController {

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
