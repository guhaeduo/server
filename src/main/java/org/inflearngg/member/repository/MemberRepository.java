package org.inflearngg.member.repository;

import org.inflearngg.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmailAndSocialId(String email, String socialId);

    Optional<Member> findByEmailAndPasswordAndSocialId(String email, String password, String socialId);

}
