package com.web.curation.team.challenge;

import java.util.List;

import com.web.curation.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TeamChallengeDao extends JpaRepository<TeamChallenge, Integer> {

    List<TeamChallenge> findTeamChallengeByTeam(Team team);
}
