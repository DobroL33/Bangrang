package com.ssafy.bangrang.domain.event.entity;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "likes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name="likes_unique",
                        columnNames = {
                                "event_idx",
                                "member_idx"
                        }
                )
        })
public class Likes {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "likes_idx")
    private Long idx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_idx", nullable = false)
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_idx", nullable = false)
    private AppMember appMember;


    @Builder
    public Likes(Event event, AppMember appMember){
        this.event = event;
        this.changeAppMember(appMember);
    }

    public void changeAppMember(AppMember appMember){
        this.appMember = appMember;
        appMember.getLikes().add(this);
    }
}
