package com.ssafy.bangrang.domain.member.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "friendship")
@ToString(of = {"idx", "friendIdx"})
public class Friendship {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "friendship_idx")
    private Long idx;

    @Column(name = "friendship_friend_idx", unique = true)
    private Long friendIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx")
    private AppMember appMember;

    @Builder
    public Friendship(Long friendIdx, AppMember appMember){
        this.friendIdx = friendIdx;
        this.changeAppMember(appMember);
    }

    public void changeAppMember(AppMember appMember){
        this.appMember = appMember;
        appMember.getFriendships().add(this);
    }

}
