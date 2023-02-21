package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChatRoomRepository {

    @PersistenceContext
    EntityManager em;

    public ChatRoom findById(UUID id) {
        ChatRoom chatRoom = em.createQuery("SELECT c FROM chat_room c WHERE c.id = ?1", ChatRoom.class)
                .setParameter(1, id)
                .getSingleResult();

        return chatRoom;
    }

    public ChatRoom createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom();
        List<ChatRoomMember> members = new ArrayList<>();

        List<AthleteData> athletes =
                em.createQuery("SELECT a FROM athlete_data a JOIN a.user u JOIN a.team t WHERE a.team.id = ?1", AthleteData.class)
                .setParameter(1, request.getTeamId())
                .getResultList();

        // This could be included in the above query with the team, but for now I will leave this separate
        CoachData coach = em.createQuery("SELECT c FROM coach_data c JOIN c.user u WHERE c.id = ?1", CoachData.class)
                .setParameter(1, request.getCoachId())
                .getSingleResult();

        if (coach != null) {
            ChatRoomMember member = new ChatRoomMember();
            member.setUserId(coach.getUser().getId());
            member.setChatRoom(chatRoom);
            members.add(member);
        }

        if (athletes.size() > 0) {
            for (AthleteData athlete : athletes) {
                ChatRoomMember member = new ChatRoomMember();
                member.setChatRoom(chatRoom);
                member.setUserId(athlete.getUser().getId());
                members.add(member);
            }

            chatRoom.setMembers(members);
            chatRoom.setTeam(athletes.get(0).getTeam());

        }

        em.persist(chatRoom);

        return chatRoom;
    }
}