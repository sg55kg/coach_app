package com.coachapp.coach_pc.service.chat;

import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.repository.ChatRoomRepository;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomService {

    private ChatRoomRepository chatRoomRepo;

    public ChatRoomService(ChatRoomRepository chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    public ResponseEntity<ChatRoom> createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = chatRoomRepo.createChatRoom(request);
        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteChatRoom(UUID chatId) {
        //chatRoomRepo.deleteById(chatId);
        return new ResponseEntity<>("Successfully deleted chat room", HttpStatus.OK);
    }

    public ResponseEntity<ChatRoom> getChatRoom(UUID id) {
        ChatRoom chatRoom = chatRoomRepo.findById(id);
        if (chatRoom == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(chatRoom, HttpStatus.OK);
        }
    }
}
