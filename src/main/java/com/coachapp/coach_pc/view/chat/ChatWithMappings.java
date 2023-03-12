package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.Limit;
import com.blazebit.persistence.view.Mapping;
import com.coachapp.coach_pc.model.chat.ChatRoom;

import java.util.List;
import java.util.UUID;

@EntityView(ChatRoom.class)
public interface ChatWithMappings extends ChatRoomViewModel {
    @Limit(limit = "20", order = "sentAt DESC")
    List<MessageViewModel> getMessages();
    @Mapping("members")
    List<ChatMemberViewModel> getMembers();
    @Mapping("team.id")
    UUID getTeamId();
}
