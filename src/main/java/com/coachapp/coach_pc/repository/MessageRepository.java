package com.coachapp.coach_pc.repository;

import com.coachapp.coach_pc.model.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("select m from Message m where m.chatRoom.id = ?1")
    public Page<Message> findMessagesByChatRoomId(UUID chatRoomId, Pageable pageable);
}