package com.coachapp.coach_pc.service.chat;

import com.coachapp.coach_pc.model.chat.Message;
import com.coachapp.coach_pc.repository.MessageRepository;
import com.coachapp.coach_pc.request.chat.MessageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MessageService {

    private MessageRepository messageRepo;

    public MessageService(MessageRepository messageRepo) {
        this.messageRepo = messageRepo;
    }

    public ResponseEntity<Message> createMessage(MessageRequest request) {
        Message message = MessageRequest.convertRequest(request);
        message = messageRepo.save(message);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteMessage(UUID messageId) {
        messageRepo.deleteById(messageId);
        return new ResponseEntity<>("Successfully deleted message", HttpStatus.OK);
    }

    public ResponseEntity<Message> updateMessage(MessageRequest request) {
        Optional<Message> o = messageRepo.findById(request.getId());

        if (o.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        Message message = o.get();
        message = MessageRequest.convertRequest(request);
        message = messageRepo.save(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    public ResponseEntity<List<Message>> getMessagesByChatRoomId(UUID chatRoomId) {
        Page<Message> messages = messageRepo.findMessagesByChatRoomId(
                chatRoomId,
                PageRequest.of(0, 10, Sort.by("sentAt"))
        );
        List<Message> list = messages.stream().toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
