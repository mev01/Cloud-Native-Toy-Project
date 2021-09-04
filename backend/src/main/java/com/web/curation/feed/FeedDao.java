package com.web.curation.feed;

import com.web.curation.member.Member;
import com.web.curation.team.Team;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface FeedDao extends JpaRepository<Feed, Integer> {
    @Query("SELECT f FROM Feed f join fetch f.photos where f.team in ?1 ORDER BY f.writeDate DESC")
    List<Feed> findAllJoinFetch(List<Team> teamList, Pageable pageable);

    int countByMember(Member member);

    List<Feed> findByMember(Member member);

    @Query("SELECT f FROM Feed f join fetch f.photos where f.team in ?1 ORDER BY f.writeDate DESC")
    List<Feed> findByTeam(Team team);
}