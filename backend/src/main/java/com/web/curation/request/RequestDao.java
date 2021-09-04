package com.web.curation.request;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.member.Member;
import com.web.curation.team.Team;

public interface RequestDao extends JpaRepository<Request, Integer> {
    public Optional<Request> getRequestByTeamAndMember(Team team, Member member);

    public List<Request> findAllByTeam(Team team);

    Optional<Request> findRequestByTeamAndMember(Team team, Member member);
}
