package com.web.curation.team.challenger;

import org.springframework.data.jpa.repository.JpaRepository;
import com.web.curation.member.Member;
import com.web.curation.team.challenge.TeamChallenge;

import java.util.List;
import java.util.Optional;


public interface TeamChallengerDao extends JpaRepository<TeamChallenger, Integer> {
    Optional<TeamChallenger> findTeamChallengerByTeamChallengeAndMember(TeamChallenge teamChallenge, Member member);

    List<TeamChallenger> findTeamChallengerByMember(Member member);
}
