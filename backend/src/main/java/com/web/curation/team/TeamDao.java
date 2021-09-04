package com.web.curation.team;

import com.web.curation.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamDao extends JpaRepository<Team, Integer> {

    //모든 팀 조회
    List<Team> findAll();

    //팀 이름 중복검사
    boolean existsByName(String name);

    Team getOne(int teamId);

    Optional<Team> findTeamByMember(Member member);

}
