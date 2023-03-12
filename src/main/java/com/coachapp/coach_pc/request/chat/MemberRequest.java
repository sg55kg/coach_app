package com.coachapp.coach_pc.request.chat;

import java.util.UUID;

// TODO: find a better name for this or remove the existing ChatMemberRequest if it's outdated
public class MemberRequest {

    private UUID userId;
    private String photoURL;

    public MemberRequest() {}

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
