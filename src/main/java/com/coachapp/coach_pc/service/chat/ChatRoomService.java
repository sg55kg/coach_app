package com.coachapp.coach_pc.service.chat;

import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.repository.ChatRoomRepository;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import com.coachapp.coach_pc.view.chat.ChatRoomViewModel;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ChatRoomService {

    private ChatRoomRepository chatRoomRepo;

    public ChatRoomService(ChatRoomRepository chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

    public ResponseEntity<ChatWithMappings> createChatRoom(ChatRoomRequest request) {
        ChatWithMappings chatRoom = chatRoomRepo.createChatRoom(request);

        return new ResponseEntity<>(chatRoom, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteChatRoom(UUID chatId) {
        //chatRoomRepo.deleteById(chatId);
        return new ResponseEntity<>("Successfully deleted chat room", HttpStatus.OK);
    }

    public ResponseEntity<ChatRoomViewModel> getChatRoom(UUID id) {
        ChatRoom chatRoom = chatRoomRepo.findById(id);
//        if (chatRoom == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else {
//            ChatRoomViewModel vm = ChatRoomViewModel.convertChatRoom(chatRoom);
//            return new ResponseEntity<>(vm, HttpStatus.OK);
//        }
        return null;
    }

    public ResponseEntity<List<ChatWithMappings>> getChatRoomsByUserId(UUID userId) {
        List<ChatWithMappings> chatRooms = chatRoomRepo.findAllByChatRoomMemberUserId(userId);

        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }
}
