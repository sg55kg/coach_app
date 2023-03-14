package com.coachapp.coach_pc.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.EntityViewSetting;
import com.coachapp.coach_pc.model.UserData;
import com.coachapp.coach_pc.view.user.UserViewModel;
import com.coachapp.coach_pc.view.user.UserWithMappings;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepository {

    private final EntityManager em;
    private final CriteriaBuilderFactory cbf;
    private final EntityViewManager evm;

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



        return Optional.ofNullable(cb1.getSingleResult());
//        return Optional.ofNullable(evm.applySetting(EntityViewSetting.create(UserWithMappings.class), cb)
//                .from(UserData.class, "user")
//                .where("user.email").eq(email)
//                .getSingleResult());
    }

    @Transactional
    public Optional<UserData> findUserById(UUID id) {
        return Optional.ofNullable(em.find(UserData.class, id));
    }

    public UserWithMappings addUser(UserData newUser) {
        CriteriaBuilder<UserData> cb = cbf.create(em, UserData.class);
        em.persist(newUser);
        CriteriaBuilder<UserWithMappings> cb1 = evm.applySetting(EntityViewSetting.create(UserWithMappings.class), cb)
                .from(UserData.class, "user")
                .where("user.email").eq(newUser.getEmail());

        UserWithMappings user = cb1.getSingleResult();

        return user;
    }

    @Transactional
    public UserWithMappings updateUser(UserData updatedUser) {
        CriteriaBuilder<UserData> cb = cbf.create(em, UserData.class);
        em.merge(updatedUser);
        UserWithMappings user = evm.find(em, UserWithMappings.class, updatedUser.getId());

        return user;
    }
}
