package com.web.curation.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.web.curation.files.Photo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int memberId;

    @Column(nullable = false, length = 45)
    private String name;

    @Column(nullable = false, length = 128)
    private String email;

    @Column(nullable = false, length = 45)
    private String phone;

    @Column(nullable = false, length = 240)
    private String password;

    @Column(nullable = false, length = 200)
    private String address;

    @Column(nullable = false, length = 200)
    private String addressDetail;

    @Column(nullable = false, length = 11)
    private String zonecode;

    @Column(nullable = false, length = 1)
    private String sex;

    @Column(nullable = false)
    private int point;

    @Column(nullable = false, length = 5)
    private String mbti;

    @OneToOne
    @JoinColumn(name = "PHOTO_ID")
    private Photo photo;

    @CreationTimestamp
    @JsonIgnore
    private LocalDateTime createDate;

    @Column(nullable = false)
    private Boolean authenticated;

    public Member(int memberId) {
        this.memberId = memberId;
    }
}
