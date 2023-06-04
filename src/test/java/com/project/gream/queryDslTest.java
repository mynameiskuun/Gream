package com.project.gream;


import com.project.gream.common.enumlist.LikeTargetType;
import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import com.project.gream.domain.post.entity.QLikes;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Slf4j
@SpringBootTest
@Transactional
public class queryDslTest {

    @Autowired
    EntityManager em;

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    MemberRepository memberRepository;
    @Test
    void querydsltest() {

        QLikes likes = new QLikes("likes");
        Member member = memberRepository.findById("namsge@naver.com").orElseThrow();

        Integer fetchOne = queryFactory
                .selectOne()
                .from(likes)
                .where(likes.member.eq(member), likes.targetId.eq(2L), likes.likeTargetType.eq(LikeTargetType.COMMENT))
                .fetchFirst();

        log.info("result : " + fetchOne);
    }
}
