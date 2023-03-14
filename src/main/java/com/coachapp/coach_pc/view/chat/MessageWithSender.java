package com.coachapp.coach_pc.view.chat;

import com.blazebit.persistence.view.EntityView;
import com.coachapp.coach_pc.model.chat.Message;

@EntityView(Message.class)
public interface MessageWithSender extends MessageViewModel {
    ChatMemberViewModel getSender();
}
