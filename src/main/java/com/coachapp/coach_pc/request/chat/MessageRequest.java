package com.coachapp.coach_pc.request.chat;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.model.chat.Message;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRequest {

    private UUID id;
    private String contents;
    private UUID senderId;
    private String senderName;
    private UUID chatId;
    private List<MessageRequest> replies;
    private OffsetDateTime sentAt;

    public MessageRequest() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public UUID getSenderId() {
        return senderId;
    }

    public void setSenderId(UUID senderId) {
        this.senderId = senderId;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public List<MessageRequest> getReplies() {
        return replies;
    }

    public void setReplies(List<MessageRequest> replies) {
        this.replies = replies;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public static Message convertRequest(MessageRequest messageRequest) {
        Message message = new Message();
        ChatRoom chatRoom = new ChatRoom();
        UserData user = new UserData();
        List<Message> replies = new ArrayList<>();

        messageRequest.getReplies().forEach(r -> replies.add(MessageRequest.convertRequest(r)));
        chatRoom.setId(messageRequest.getChatId());
        user.setId(messageRequest.getSenderId());

        message.setReplies(replies);
        message.setContents(messageRequest.getContents());
        if (messageRequest.getId() != null) {
            message.setId(messageRequest.getId());
        }
        if (messageRequest.getSentAt() != null) {
            message.setSentAt(messageRequest.getSentAt());
        }

        message.setSender(user);
        message.setChatRoom(chatRoom);

        return message;
    }
}
