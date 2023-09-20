package com.softtech.couponservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "event")
@Data
public class Event {

    @Id
    @SequenceGenerator(
            name = "event_seq",
            sequenceName = "event_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "event_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "match_type",
            length = 10,
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private MatchType matchType;

    @Column(
            name = "start_date",
            nullable = false
    )
    private Date startDate;

    @Column(
            name = "home_team_id",
            nullable = false
    )
    private Long homeTeamId;

    @Column(
            name = "away_team_id",
            nullable = false
    )
    private Long awayTeamId;

    @Column(
            name = "mbs_point",
            nullable = false
    )
    private byte mbsPoint;

    @Column(
            name = "win_ratio",
            nullable = false
    )
    private float winRatio;

    @Column(
            name = "lose_ratio",
            nullable = false
    )
    private float loseRatio;

    @Column(
            name = "win_point",
            nullable = false
    )
    private float winPoint;

    @Column(
            name = "draw_point",
            nullable = false
    )
    private float drawPoint;

    @Column(
            name = "lose_point",
            nullable = false
    )
    private float losePoint;

    @Column(
            name = "end_date"
    )
    private Date endDate;

    @Column(
            name = "score_goal"
    )
    private int scoreGoal;

    @Column(
            name = "concede_goal"
    )
    private int concedeGoal;

    @Column(
            name = "match_result",
            length = 4
    )
    @Enumerated(EnumType.STRING)
    private MatchResultType matchResult;

    @Column(
            name="statue",
            nullable = false
    )
    private boolean statue;

    @OneToMany(targetEntity = EventCoupon.class)
    @JoinColumn(name = "event_id",referencedColumnName = "id")
    private List<EventCoupon> eventCoupons;
}
