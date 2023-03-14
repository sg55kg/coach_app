package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.view.ConvertOption;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.chat.Message;
import com.coachapp.coach_pc.request.chat.MessageRequest;
import com.coachapp.coach_pc.view.chat.MessageCreateView;
import com.coachapp.coach_pc.view.chat.MessageViewModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MessageRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

    public MessageRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf = cbf;
        this.evm = evm;
    }

    @Transactional
    public MessageViewModel save(MessageRequest message) {
        Message message1 = MessageRequest.convertRequest(message);
        em.persist(message1);

        // TODO: find a better way to convert to view model without a separate query
        MessageViewModel m = evm.find(em, MessageViewModel.class, message1.getId());
        return m;
    }

    @Transactional
    public MessageViewModel update(MessageViewModel message) {
        em.merge(message);
        return message;
    }

    public Optional<MessageViewModel> findById(UUID id) {
        return Optional.ofNullable(evm.find(em, EntityViewSetting.create(MessageViewModel.class), id));
    }

    @Transactional
    public void deleteById(UUID id) {
        em.remove(id);
    }

    public PagedList<MessageViewModel> fetchNext20Messages(UUID chatId, int start, int end) {
        CriteriaBuilder<Message> cb = cbf.create(em, Message.class);

        PagedList<MessageViewModel> messages = evm.applySetting(EntityViewSetting.create(MessageViewModel.class), cb)
                .orderByAsc("sentAt")
                .orderByAsc("id")
                .where("chatRoom.id").eq(chatId)
                .page(null, start, end)
                .getResultList();

        return messages;
    }
}