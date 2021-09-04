package com.web.curation.message;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.curation.team.Team;

public interface MessageDao extends JpaRepository<Message, Integer> {
    public List<Message> findByTeam(Team team, Pageable pageable);
}
