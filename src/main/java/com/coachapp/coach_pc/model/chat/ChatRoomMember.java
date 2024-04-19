package com.coachapp.coach_pc.model.chat;

import com.coachapp.coach_pc.model.user.UserData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

// Generally recommended to import only the specific classes you need
// - clarity, avoiding name conflicts, optimization and easier to determine if a class or library is no longer required in your project (clean)
import javax.persistence.*;
import java.util.UUID;

// There is no universally accepted convention for naming SQL tables, but many developers and database administrators prefer to use plural table names.
// The reasoning behind this is that a table typically represents a collection of similar entities, and the plural form better reflects this concept.
// e.g chat_room_members (each record being a chat room member)
@Entity
@Table(name = "chat_room_member")
public class ChatRoomMember {

    // recommend using lombok @Getter and @Setter for your fields
    // You can annotate any field with @Getter and/or @Setter, to let lombok generate the default getter/setter automatically.
    // https://projectlombok.org/features/GetterSetter
    // makes it more readable and less prone to errors

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
