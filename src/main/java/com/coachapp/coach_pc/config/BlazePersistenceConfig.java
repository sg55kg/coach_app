package com.coachapp.coach_pc.config;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.integration.view.spring.EnableEntityViews;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.blazebit.persistence.view.EntityViewManager;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import com.coachapp.coach_pc.view.chat.ChatWithMappings;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.util.UUID;

@Configuration
@EnableEntityViews(value = {
        "com.coachapp.coach_pc.view.chat",
        "com.coachapp.coach_pc.view.user",
        "com.coachapp.coach_pc.view.team",
        "com.coachapp.coach_pc.view.program"
})
public class BlazePersistenceConfig {
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(false)
    public CriteriaBuilderFactory createCriteriaBuilderFactory() {
        CriteriaBuilderConfiguration config = Criteria.getDefault();
        // do some configuration
        return config.createCriteriaBuilderFactory(entityManagerFactory);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy(false)
    // inject the criteria builder factory which will be used along with the entity view manager
    public EntityViewManager createEntityViewManager(CriteriaBuilderFactory cbf, EntityViewConfiguration entityViewConfiguration) {
        entityViewConfiguration.setTypeTestValue(UUID.class, UUID.randomUUID());
        //entityViewConfiguration.setTypeTestValue(ChatWithMappings.class, new ChatWithMappingsI)
        return entityViewConfiguration.createEntityViewManager(cbf);
    }
}
