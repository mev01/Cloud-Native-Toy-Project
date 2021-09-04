package com.web.curation.team;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.curation.files.Photo;
import com.web.curation.member.Member;
import com.web.curation.sport.Sport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "Team")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Integer teamId;

    private String name;

    private String introduction;

    private String leader;

    @Column(name = "member_count")
    @ColumnDefault("1")
    @JsonIgnore
    private int memberCount;

    @Column(name = "img_path")
    private String imgPath;

    @JsonIgnore
    @Column(columnDefinition = "DATETIME default CURRENT_TIMESTAMP")
    private LocalDateTime createDate;

    //참조키
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    public Team(int teamId) {
        this.teamId = teamId;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    public void updateMemberCount(boolean upOrDown) {
        if (upOrDown) {
            this.memberCount++;
        } else {
            this.memberCount--;
        }
    }
}
