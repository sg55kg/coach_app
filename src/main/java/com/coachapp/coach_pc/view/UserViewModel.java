package com.coachapp.coach_pc.view;

import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;
import com.coachapp.coach_pc.view.chat.ChatMemberViewModel;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserViewModel {
    private UUID id;
    private Date createdAt;
    private Date updatedAt;
    private String email;
    private String username;
    private AthleteViewModel athleteData;
    private CoachViewModel coachData;
    private List<ChatRoomMember> members;

    public UserViewModel() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public AthleteViewModel getAthleteData() {
        return athleteData;
    }

    public void setAthleteData(AthleteViewModel athleteData) {
        this.athleteData = athleteData;
    }

    public CoachViewModel getCoachData() {
        return coachData;
    }

    public void setCoachData(CoachViewModel coachData) {
        this.coachData = coachData;
    }

    public List<ChatRoomMember> getMembers() {
        return members;
    }

    public void setMembers(List<ChatRoomMember> members) {
        this.members = members;
    }

    public static UserViewModel convertUser(UserData user) {
        UserViewModel viewModel = new UserViewModel();

        viewModel.setId(user.getId());
        viewModel.setEmail(user.getEmail());
        viewModel.setCreatedAt(user.getCreatedAt());
        viewModel.setUpdatedAt(user.getUpdatedAt());
        viewModel.setCoachData(
                CoachViewModel.convertCoach(user.getCoachData())
        );
        viewModel.setAthleteData(
                AthleteViewModel.convertAthlete(user.getAthleteData())
        );
        viewModel.setMembers(user.getMembers());
       // viewModel.setMembers(
       //         user.getMembers().stream().map(ChatMemberViewModel::convertMember).toList()
       // );

        return viewModel;
    }
}
