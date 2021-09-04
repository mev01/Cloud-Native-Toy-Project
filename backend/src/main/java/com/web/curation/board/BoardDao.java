package com.web.curation.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.team.Team;

public interface BoardDao extends JpaRepository<Board, Integer> {

    List<Board> findByTeam(Team team);
}
