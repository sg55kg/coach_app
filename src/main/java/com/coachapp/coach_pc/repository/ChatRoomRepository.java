package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.AthleteData;
import com.coachapp.coach_pc.model.CoachData;
import com.coachapp.coach_pc.model.chat.ChatRoom;
import com.coachapp.coach_pc.model.chat.ChatRoomMember;
import com.coachapp.coach_pc.model.chat.Message;
import com.coachapp.coach_pc.request.chat.ChatRoomRequest;
import com.coachapp.coach_pc.view.chat.ChatMemberViewModel;
import com.coachapp.coach_pc.view.chat.ChatRoomViewModel;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class ChatRoomRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public ChatRoomRepository(EntityManager entityManager, CriteriaBuilderFactory builderFactory, EntityViewManager evm) {
        this.em = entityManager;
        this.cbf = builderFactory;
        this.evm = evm;
    }

    @Transactional
    public ChatRoom findById(UUID id) {
        try {
            ChatRoom chatRoom = em.createQuery("SELECT c FROM ChatRoom c WHERE c.id = ?1", ChatRoom.class)
                    .setParameter(1, id)
                    .getSingleResult();

            return chatRoom;
        } catch (NoResultException e) {
            return null;
        }

    }

    @Transactional
    public ChatWithMappings createChatRoom(ChatRoomRequest request) {
        ChatRoom chatRoom = new ChatRoom();
        List<ChatRoomMember> members = new ArrayList<>();

        List<AthleteData> athletes =
                em.createQuery("SELECT a FROM AthleteData a JOIN a.user u JOIN a.team t WHERE a.team.id = ?1", AthleteData.class)
                .setParameter(1, request.getTeamId())
                .getResultList();

        // This could be included in the above query with the team, but for now I will leave this separate
        CoachData coach = em.createQuery("SELECT c FROM CoachData c JOIN c.user u WHERE c.id = ?1", CoachData.class)
                .setParameter(1, request.getCoachId())
                .getSingleResult();

        if (coach != null) {
            ChatRoomMember member = new ChatRoomMember();
            member.setUser(coach.getUser());
            //member.setPhotoURL(coach.getUser().get);
            member.setChatRoom(chatRoom);
            members.add(member);
        }

        if (athletes.size() > 0) {
            for (AthleteData athlete : athletes) {
                ChatRoomMember member = new ChatRoomMember();
                member.setChatRoom(chatRoom);
                member.setUser(athlete.getUser());
                members.add(member);
            }


            chatRoom.setTeam(athletes.get(0).getTeam());

        } else {
            chatRoom.setTeam(coach.getTeams().stream().filter(t -> t.getId().equals(request.getTeamId())).toList().get(0));
        }
        chatRoom.setMembers(members);
        chatRoom.setName(request.getName());
        em.persist(chatRoom);

        CriteriaBuilder<ChatRoom> c = cbf.create(em, ChatRoom.class);
        ChatWithMappings vm = evm.applySetting(EntityViewSetting.create(ChatWithMappings.class), c)
                .where("id").eq(chatRoom.getId())
                .getSingleResult();

        return vm;
    }


    public List<ChatWithMappings> findAllByChatRoomMemberUserId(UUID userId) {
        CriteriaBuilder<ChatRoom> c = cbf.create(em, ChatRoom.class);
        List<ChatWithMappings> chatRooms = evm.applySetting(EntityViewSetting.create(ChatWithMappings.class), c)
                .where("members.user.id").eq(userId)
                .getResultList();

        return chatRooms;
    }
}
