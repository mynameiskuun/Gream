package com.project.gream.domain.member.repository;

import com.project.gream.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,String> {

    boolean existsById(String userId);
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);


}
