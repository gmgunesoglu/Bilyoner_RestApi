package com.softtech.couponservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "team")
@Data
public class Team {

    @Id
    @SequenceGenerator(
            name = "team_seq",
            sequenceName = "team_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "team_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="name",
            nullable = false,
            length = 30
    )
    private String name;

    @Column(
            name = "match_type",
            length = 10,
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @Column(
            name="statue",
            nullable = false
    )
    private boolean statue;

    @OneToMany(targetEntity = Event.class)
    @JoinColumn(name = "home_team_id",referencedColumnName = "id")
    private List<Event> homeEvents;

    @OneToMany(targetEntity = Event.class)
    @JoinColumn(name = "away_team_id",referencedColumnName = "id")
    private List<Event> awayEvents;
}
