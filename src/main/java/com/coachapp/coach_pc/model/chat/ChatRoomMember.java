package com.coachapp.coach_pc.model.chat;

import com.coachapp.coach_pc.model.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "chat_room_member")
public class ChatRoomMember {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    @JsonIgnore
    private ChatRoom chatRoom;
    @ManyToOne(cascade = {})
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private UserData user;

    public ChatRoomMember() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public UserData getUser() {
        return user;
    }

    public void setUser(UserData user) {
        this.user = user;
    }

}
