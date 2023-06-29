package com.coachapp.coach_pc.repository.user;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.user.UserData;
import com.coachapp.coach_pc.view.user.UserWithMappings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;
    private Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(EntityManager em, CriteriaBuilderFactory cbf, EntityViewManager evm) {
        this.em = em;
        this.cbf =  cbf;
        this.evm = evm;
    }

    public Optional<UserWithMappings> findUserByEmail(String email) {
        CriteriaBuilder<UserData> cb = cbf.create(em, UserData.class);
        CriteriaBuilder<UserWithMappings> cb1 = evm.applySetting(EntityViewSetting.create(UserWithMappings.class), cb)
                //.from(UserData.class, "user")
                .where("email").eq(email);


        try {
            return Optional.ofNullable(cb1.getSingleResult());
        } catch (NoResultException e) {
            // some of your loggers use info for errors and others use errors
            // would suggest employing these practices across the board
            // https://www.papertrail.com/solution/tips/logging-in-java-best-practices-and-tips/
            logger.info(e.getMessage());
            e.printStackTrace();
            return Optional.ofNullable(null);
        }
    }

    @Transactional
    public boolean userExistsByEmail(String email) {
        Long result = cbf.create(em, Long.class)
                .from(UserData.class)
                .select("COUNT(email)").where("email").eq(email)
                .getSingleResult();

        return result > 0;
    }

    @Transactional
    public Optional<UserData> findUserById(UUID id) {
        return Optional.ofNullable(em.find(UserData.class, id));
    }

    @Transactional
    public UserWithMappings addUser(UserData newUser) {
        CriteriaBuilder<UserData> cb = cbf.create(em, UserData.class);
        em.persist(newUser);
        CriteriaBuilder<UserWithMappings> cb1 = evm.applySetting(EntityViewSetting.create(UserWithMappings.class), cb)
                .where("email").eq(newUser.getEmail());

        UserWithMappings user = cb1.getSingleResult();

        return user;
    }

    @Transactional
    public UserWithMappings updateUser(UserData updatedUser) {
        em.merge(updatedUser);
        UserWithMappings user = evm.find(em, UserWithMappings.class, updatedUser.getId());

        return user;
    }
}
