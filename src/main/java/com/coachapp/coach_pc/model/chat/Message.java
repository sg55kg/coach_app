package com.coachapp.coach_pc.model.chat;

import com.coachapp.coach_pc.model.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Entity
public class Message {

    @Id
    @GeneratedValue(generator = "UUID")
    @Type(type = "org.hibernate.type.PostgresUUIDType")
    private UUID id;
    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ChatRoomMember sender;
    @ManyToOne
    @JoinColumn(name = "chat_id")
    @JsonIgnore
    private ChatRoom chatRoom;
    @CreationTimestamp
    private OffsetDateTime sentAt;
    @UpdateTimestamp
    private OffsetDateTime lastEdit;
    private String contents;
    @OneToMany
    private List<Message> replies;
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonIgnore
    private Message parent;

    public Message() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ChatRoomMember getSender() {
        return sender;
    }

    public void setSender(ChatRoomMember sender) {
        this.sender = sender;
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public OffsetDateTime getLastEdit() {
        return lastEdit;
    }

    public void setLastEdit(OffsetDateTime lastEdit) {
        this.lastEdit = lastEdit;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public List<Message> getReplies() {
        return replies;
    }

    public void setReplies(List<Message> replies) {
        this.replies = replies;
    }

    public Message getParent() {
        return parent;
    }

    public void setParent(Message parent) {
        this.parent = parent;
    }
}
