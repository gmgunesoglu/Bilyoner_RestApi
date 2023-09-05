package com.softtech.accountservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "member")
@Data
public class Member {

    @Id
    @SequenceGenerator(
            name = "member_seq",
            sequenceName = "member_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name="first_name",
            nullable = false,
            length = 30
    )
    private String firstName;

    @Column(
            name="last_name",
            nullable = false,
            length = 20
    )
    private String lastName;

    @Column(
            name="user_name",
            nullable = false,
            length = 14,
            unique = true
    )
    private String userName;

    @Column(
            name="password",
            nullable = false,
            length = 60
    )
    private String password;

    @Column(
            name="email",
            nullable = false,
            length = 50,
            unique = true
    )
    private String email;

    @Column(
            name="banned",
            nullable = false
    )
    private boolean banned;

    @Column(
            name="block_date"
    )
    private Date blockedDate;

    @Column(
            name="statue",
            nullable = false
    )
    private boolean statue;

    @OneToMany(targetEntity = BalanceTransaction.class)
    @JoinColumn(name = "member_id",referencedColumnName = "id")
    private List<BalanceTransaction> balanceTransactions;

}
