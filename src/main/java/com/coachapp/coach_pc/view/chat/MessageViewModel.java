package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.*;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;
import com.coachapp.coach_pc.model.chat.Message;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@EntityView(Message.class)
@UpdatableEntityView
public interface MessageViewModel {

    @IdMapping
    UUID getId();
    ChatMemberViewModel getSender();
    void setSender(ChatMemberViewModel sender);
    @Mapping("chatRoom.id")
    UUID getChatId();
    String getContents();
    void setContents(String contents);
    @Mapping("sentAt")
    OffsetDateTime getSentAt();
    void setSentAt(OffsetDateTime sentAt);
    @Mapping("updatedAt")
    OffsetDateTime getUpdatedAt();
    void setUpdatedAt(OffsetDateTime updatedAt);


}

