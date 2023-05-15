package com.project.gream.api;

import com.project.gream.domain.member.entity.Member;
import com.project.gream.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@SpringBootTest
public class MemberApiTest {

    @Autowired
    MemberRepository memberRepository;

    // 단위 테스트시, @Transactional 설정이 없으면 세션이 이어지지 않는다.
    @Transactional
    @Test
    void getReferenceById() {
        Member proxyEntity = memberRepository.getReferenceById("id");
        // select 쿼리가 날아가지 않아 성능상 이점을 누릴 수 있다.
    }
}
