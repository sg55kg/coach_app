package com.coachapp.coach_pc.request.chat;

import com.coachapp.coach_pc.model.user.UserData;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;

import java.util.UUID;

public class ChatMemberRequest {

    private UUID id;
    private UUID userId;
    private UUID chatId;

    public ChatMemberRequest() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public UUID getChatId() {
        return chatId;
    }

    public void setChatId(UUID chatId) {
        this.chatId = chatId;
    }

    public static ChatRoomMember convertRequest(ChatMemberRequest request) {
        ChatRoomMember member = new ChatRoomMember();
        UserData user = new UserData();
        user.setId(request.getUserId());

        member.setId(request.getId());
        member.setUser(user);
        // for now, set chat room a level up in message request
        return member;
    }
}
