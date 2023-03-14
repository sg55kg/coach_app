package com.coachapp.coach_pc.request.chat;

import com.coachapp.coach_pc.model.Team;
import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.repository.ChatRoomRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChatRoomRequest {

    private UUID teamId;
    private List<UUID> members;
    private UUID coachId;
    private String name;

    public ChatRoomRequest() {}

    public UUID getTeamId() {
        return teamId;
    }

    public void setTeamId(UUID teamId) {
        this.teamId = teamId;
    }

    public List<UUID> getMembers() {
        return members;
    }

    public void setMembers(List<UUID> members) {
        this.members = members;
    }

    public UUID getCoachId() {
        return coachId;
    }

    public void setCoachId(UUID coachId) {
        this.coachId = coachId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
