package com.coachapp.coach_pc.service.chat;

import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.repository.chat.ChatRoomRepository;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import com.coachapp.coach_pc.view.chat.ChatRoomViewModel;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

// Add logs to every service method indicating request params (masking private data) and response values for debugging
@Service
public class ChatRoomService {

    private ChatRoomRepository chatRoomRepo;

    public ChatRoomService(ChatRoomRepository chatRoomRepo) {
        this.chatRoomRepo = chatRoomRepo;
    }

//    The responsibility of different layers should be separated based on the principles of the Model-View-Controller (MVC) pattern.
//    Here's a brief overview of the responsibilities of each layer:
//
//    Controller: Handles incoming requests, processes user input, and returns the appropriate view.
//    Service: Contains the business logic and handles interactions with the data layer (repositories).
//    Repository: Manages data access, storage, and retrieval.
//    With this in mind, it's generally better to keep the Service layer focused on business logic and data manipulation, and let the Controller layer handle the responsibility of working with HTML entities and code.
//
//    So, in your case, the Service layer should return the necessary data (e.g., objects, lists) to the Controller, and the Controller should then use that data to generate the appropriate HTML response.
//    This separation of concerns keeps your code more modular and easier to maintain.
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

        // remove unused code
//        if (chatRoom == null) {
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        } else {
//            ChatRoomViewModel vm = ChatRoomViewModel.convertChatRoom(chatRoom);
//            return new ResponseEntity<>(vm, HttpStatus.OK);
//        }
        // return chatRoom?
        return null;
    }

    public ResponseEntity<List<ChatWithMappings>> getChatRoomsByUserId(UUID userId) {
        List<ChatWithMappings> chatRooms = chatRoomRepo.findAllByChatRoomMemberUserId(userId);

        return new ResponseEntity<>(chatRooms, HttpStatus.OK);
    }
}
