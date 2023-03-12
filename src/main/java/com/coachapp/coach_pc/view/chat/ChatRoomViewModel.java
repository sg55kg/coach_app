package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Limit;
import com.coachapp.coach_pc.model.chat.ChatRoom;

import java.util.List;
import java.util.UUID;

@EntityView(ChatRoom.class)
public interface ChatRoomViewModel {

    @IdMapping
    UUID getId();
    String getName();
}

