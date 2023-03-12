package com.coachapp.coach_pc.service.chat;

import com.coachapp.coach_pc.model.chat.Message;
import com.coachapp.coach_pc.repository.MessageRepository;
import com.coachapp.coach_pc.request.chat.MessageRequest;
import com.coachapp.coach_pc.view.chat.MessageCreateView;
import com.coachapp.coach_pc.view.chat.MessageViewModel;
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

    public ResponseEntity<MessageViewModel> createMessage(MessageRequest message) {
        //Message message1 = MessageRequest.convertRequest(message);
        MessageViewModel vm = messageRepo.save(message);
        //MessageViewModel vm = MessageViewModel.convertMessage(message);
        //return new ResponseEntity<>(vm, HttpStatus.CREATED);
        return new ResponseEntity<>(vm, HttpStatus.CREATED);
    }

    public ResponseEntity<String> deleteMessage(UUID messageId) {
        messageRepo.deleteById(messageId);
        return new ResponseEntity<>("Successfully deleted message", HttpStatus.OK);
    }

    public ResponseEntity<MessageViewModel> updateMessage(MessageRequest request) {
        Optional<MessageViewModel> o = messageRepo.findById(request.getId());

        if (o.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
        MessageViewModel message = o.get();
        message = messageRepo.update(message);
       // MessageViewModel vm = MessageViewModel.convertMessage(message);
        return new ResponseEntity<>(message, HttpStatus.OK);
       // return new ResponseEntity<>(vm, HttpStatus.OK);
    }
}
