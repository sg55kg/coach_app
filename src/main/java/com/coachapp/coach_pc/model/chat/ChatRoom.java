package com.coachapp.coach_pc.model.chat;

import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.UserData;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
public class ChatRoom {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToMany
    private List<ChatRoomMember> members;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
    @OneToMany
    private List<Message> messages;

    public ChatRoom() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public List<ChatRoomMember> getMembers() {
        return members;
    }

    public void setMembers(List<ChatRoomMember> members) {
        this.members = members;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
