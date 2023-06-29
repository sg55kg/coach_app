package com.coachapp.coach_pc.request.chat;

import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;
import com.coachapp.coach_pc.model.chat.Message;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageRequest {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

    private UUID id;
    private String contents;
    private ChatMemberRequest sender;
    private UUID chatId;
    private List<MessageRequest> replies;
    private OffsetDateTime sentAt;
    private OffsetDateTime updatedAt;

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

    public ChatMemberRequest getSender() {
        return sender;
    }

    public void setSender(ChatMemberRequest sender) {
        this.sender = sender;
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static Message convertRequest(MessageRequest messageRequest) {
        Message message = new Message();
        ChatRoom chatRoom = new ChatRoom();
        ChatRoomMember member = ChatMemberRequest.convertRequest(messageRequest.getSender());
        member.setChatRoom(chatRoom);
        List<Message> replies = new ArrayList<>();

        messageRequest.getReplies().forEach(r -> replies.add(MessageRequest.convertRequest(r)));
        chatRoom.setId(messageRequest.getChatId());
        message.setReplies(replies);
        message.setContents(messageRequest.getContents());
        if (messageRequest.getId() != null) {
            message.setId(messageRequest.getId());
        }
        if (messageRequest.getSentAt() != null) {
            message.setSentAt(messageRequest.getSentAt());
        }

        message.setSender(member);
        message.setChatRoom(chatRoom);
        message.setUpdatedAt(messageRequest.getUpdatedAt());

        return message;
    }
}
