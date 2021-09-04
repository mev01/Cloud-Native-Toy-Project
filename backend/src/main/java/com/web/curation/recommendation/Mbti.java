package com.web.curation.recommendation;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Mbti")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Builder
public class Mbti {
    @Id
    @Column(name = "team_id")
    private Integer teamId;

    @Column(name = "INFP")
    private int infp;

    @Column(name = "ENFP")
    private int enfp;

    @Column(name = "INFJ")
    private int infj;

    @Column(name = "ENFJ")
    private int enfj;

    @Column(name = "INTJ")
    private int intj;

    @Column(name = "ENTJ")
    private int entj;

    @Column(name = "INTP")
    private int intp;

    @Column(name = "ENTP")
    private int entp;

    @Column(name = "ISFP")
    private int isfp;

    @Column(name = "ESFP")
    private int esfp;

    @Column(name = "ISTP")
    private int istp;

    @Column(name = "ESTP")
    private int estp;

    @Column(name = "ISFJ")
    private int isfj;

    @Column(name = "ESFJ")
    private int esfj;

    @Column(name = "ISTJ")
    private int istj;

    @Column(name = "ESTJ")
    private int estj;

    public Mbti(int teamId, String mbti) {
        this.teamId = teamId;
        switch (mbti) {
            case "infp":
                this.infp = 1;
                break;
            case "enfp":
                this.enfp = 1;
                break;
            case "infj":
                this.infj = 1;
                break;
            case "enfj":
                this.enfj = 1;
                break;
            case "intj":
                this.intj = 1;
                break;
            case "entj":
                this.entj = 1;
                break;
            case "intp":
                this.intp = 1;
                break;
            case "entp":
                this.entp = 1;
                break;
            case "isfp":
                this.isfp = 1;
                break;
            case "esfp":
                this.esfp = 1;
                break;
            case "istp":
                this.istp = 1;
                break;
            case "estp":
                this.estp = 1;
                break;
            case "isfj":
                this.isfj = 1;
                break;
            case "esfj":
                this.esfj = 1;
                break;
            case "istj":
                this.istj = 1;
                break;
            case "estj":
                this.estj = 1;
                break;
            default:
                break;
        }
    }
}
