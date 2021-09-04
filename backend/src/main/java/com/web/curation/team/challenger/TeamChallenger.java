package com.web.curation.team.challenger;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.curation.member.Member;
import com.web.curation.team.challenge.TeamChallenge;
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
public class TeamChallenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "teamchallenger_id")
    private int teamchallengerId;

    @ManyToOne
    @JoinColumn(name = "TEAMCHALLENGE_ID")
    private TeamChallenge teamChallenge;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Column(name = "done")
    private boolean done;
}
