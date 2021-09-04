package com.web.curation.member.challenge;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.member.Member;

public interface ChallengeDao extends JpaRepository<Challenge, Integer> {

    Challenge getOneByMember(Member member);

}
