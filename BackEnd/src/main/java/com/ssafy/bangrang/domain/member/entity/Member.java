package com.ssafy.bangrang.domain.member.entity;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@Table(name = "member")
public abstract class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_idx")
    Long idx;

    @Column(name = "member_access_token")
    private String accessToken;

    @Column(name = "member_refresh_token")
    private String refreshToken;

}
