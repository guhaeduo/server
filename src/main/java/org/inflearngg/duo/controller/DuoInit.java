package org.inflearngg.duo.controller;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.inflearngg.duo.entity.DuoPost;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("local")
@Component
@RequiredArgsConstructor
public class DuoInit {

    private final DuoInitService duoInitService;

    @PostConstruct
    public void init() {
        duoInitService.init();
    }


    @Component
    static class DuoInitService {

        @PersistenceContext
        private EntityManager em;

        @Transactional
        public void init() {
            for (int i = 1; i <= 50; i++) {
                DuoPost duoPost = new DuoPost("gameName" + i, "KR" + i, "ABCTEST" + i);
                em.persist(duoPost);
            }
            for (int i = 51; i <= 100; i++) {
                DuoPost duoPost = new DuoPost("gameName" + i, "KR" + i, "ABCTEST" + i, true);
                em.persist(duoPost);
            }
            System.out.println("===========DuoInitService.init============");
        }
    }
}
