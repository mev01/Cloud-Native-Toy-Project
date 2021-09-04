package com.web.curation.sport;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Sport")
@Builder
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sportId;

    @Column(name = "sport_name")
    private String name;

    public Sport(int sportId) {
        this.sportId = sportId;
    }
}
