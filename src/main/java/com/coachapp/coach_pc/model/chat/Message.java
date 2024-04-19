package com.coachapp.coach_pc.model.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;

// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// There is no universally accepted convention for naming SQL tables, but many developers and database administrators prefer to use plural table names.
// The reasoning behind this is that a table typically represents a collection of similar entities, and the plural form better reflects this concept.
// e.g messages (each record being a message)
@Entity
@Table(name = "message")
public class Message {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
    private OffsetDateTime updatedAt;
    private String contents;
    @OneToMany(mappedBy = "parent")
    private List<Message> replies = new ArrayList<>();
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

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
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
