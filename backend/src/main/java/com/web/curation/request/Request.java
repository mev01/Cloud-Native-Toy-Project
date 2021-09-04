package com.web.curation.request;

import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;


import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.web.curation.member.Member;
import com.web.curation.team.Team;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "Team_id", nullable = false)
    private Team team;

    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

    @JsonProperty(access = Access.READ_ONLY)
    @Enumerated(EnumType.ORDINAL)
    private Status status;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createDate;

    @UpdateTimestamp
    @JsonIgnore
    private LocalDateTime updateDate;
}