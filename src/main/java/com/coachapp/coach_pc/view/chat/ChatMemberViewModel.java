package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;

import java.util.UUID;

@EntityView(ChatRoomMember.class)
public interface ChatMemberViewModel {

    @IdMapping
    UUID getId();
    @Mapping("user.id")
    UUID getUserId();
    @Mapping("chatRoom.id")
    UUID getChatId();
    @Mapping("user.username")
    String getUsername();
    @Mapping("user.photoUrl")
    String getPhotoUrl();
}
