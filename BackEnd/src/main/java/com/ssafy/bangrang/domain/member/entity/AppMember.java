package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.event.entity.Likes;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.member.model.vo.AppMemberStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("app")
@ToString(of = {"email", "nickname"})
public class AppMember extends Member{

    @Column(name = "app_member_nickname", unique = true)
    protected String nickname;

    @Column(name = "app_member_img_url")
    protected String imgUrl;

    @Column(name = "app_member_firebase_token")
    private String firebaseToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_status")
    private AppMemberStatus appMemberStatus;

    @Column(name = "app_member_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "app_member_alarms")
    private Boolean alarms;

    @OneToMany(mappedBy = "appMember")
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Friendship> friendships = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Stamp> stamps = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<MemberMarker> memberMarkers = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<MemberMapArea> memberMapAreas = new ArrayList<>();

    @Builder
    public AppMember(Long idx, String id, String nickname, String password, String imgUrl,
                     String firebaseToken, AppMemberStatus appMemberStatus){
        this.idx = idx;
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.imgUrl = imgUrl;
        this.firebaseToken = firebaseToken;
        this.alarms = true;
        this.appMemberStatus = appMemberStatus.ACTIVE;
    }


    public void changeAppMemberStatus(AppMemberStatus appMemberStatus){
        if(appMemberStatus == AppMemberStatus.UNACTIVE){
            this.deletedAt = LocalDateTime.now();
        }
        this.appMemberStatus = appMemberStatus;
    }


}
