package com.web.curation.team.challenge;

import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.web.curation.team.Team;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TeamChallenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamchallenge_id")
    private int teamChallengeId;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column(name = "TITLE", length = 128)
    private String title;

    @Column(nullable = false, length = 255)
    private String contents;

    @JsonProperty(access = Access.READ_ONLY)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @Column(name = "member_count")
    int memberCount;

    @Column(name = "goal_count")
    int goalCount;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    public TeamChallenge(int teamChallengeId) {
        this.teamChallengeId = teamChallengeId;
    }
}
