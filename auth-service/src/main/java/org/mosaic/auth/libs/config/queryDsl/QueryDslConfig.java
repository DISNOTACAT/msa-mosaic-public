package org.mosaic.auth.libs.config.queryDsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QueryDslConfig {

    private final EntityManager entityManager;

    @Bean
    public JPAQueryFactory jPAQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

}
