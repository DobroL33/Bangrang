package com.ssafy.bangrang.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendship")
public class Friendship {

    @Id
    @GeneratedValue
    @Column(name = "friendship_idx")
    private Long idx;

    @Column(name = "friendship_friend_idx", unique = true)
    private Long friendIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    public void changeAppMember(AppMember appMember){
        this.appMember = appMember;
        appMember.getFriendships().add(this);
    }

}
