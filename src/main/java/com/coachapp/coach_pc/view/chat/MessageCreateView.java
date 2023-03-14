package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.chat.Message;

import java.time.OffsetDateTime;
import java.util.UUID;

@CreatableEntityView
@EntityView(Message.class)
public interface MessageCreateView {

    @IdMapping
    UUID getId();
    ChatMemberViewModel getSender();
    void setSender(ChatMemberViewModel sender);
    @Mapping("chatRoom.id")
    @UpdatableMapping(updatable = false)
    UUID getChatId();
    void setChatId(UUID chatId);
    String getContents();
    void setContents(String contents);
    @Mapping("sentAt")
    OffsetDateTime getSentAt();
    void setSentAt(OffsetDateTime sentAt);
    @Mapping("updatedAt")
    OffsetDateTime getUpdatedAt();
    void setUpdatedAt(OffsetDateTime updatedAt);
}
