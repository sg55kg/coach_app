package com.coachapp.coach_pc.model.chat;

import com.blazebit.persistence.view.Limit;
import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "chat_room")
public class ChatRoom {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoom")
    private List<ChatRoomMember> members = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnore
    private Team team;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "chatRoom", fetch = FetchType.LAZY)
    @JsonIgnore
    //@JoinTable(name = "(select * from message order by sent_at limit 10)")
   // @Limit(limit = "10", order = "sentAt DESC")
    private List<Message> messages = new ArrayList<>();
    private String name;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
