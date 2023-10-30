package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.event.entity.Likes;
import com.ssafy.bangrang.domain.event.entity.Stamp;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.member.model.vo.AlarmReceivedStatus;
import com.ssafy.bangrang.domain.member.model.vo.AppMemberStatus;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;
import com.ssafy.bangrang.global.fcm.entity.Alarm;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("app")
public class AppMember extends Member{


    @Column(name = "app_member_email", unique = true)
    protected String email;

    @Column(name = "app_member_nickname", unique = true)
    protected String nickname;

    @Column(name = "app_member_img_url")
    protected String imgUrl;

    @Column(name = "app_member_firebase_token")
    private String firebaseToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_social_provider")
    private SocialProvider socialProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_all_alarm_status")
    private AlarmReceivedStatus allAlarmStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_notification_alarm_status")
    private AlarmReceivedStatus notificationAlarmStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_ranking_alarm_status")
    private AlarmReceivedStatus rankingAlarmStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_event_alarm_status")
    private AlarmReceivedStatus eventAlarmStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "app_member_status")
    private AppMemberStatus appMemberStatus;

    @Column(name = "app_member_deleted_at")
    private LocalDateTime deletedAt;

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

    @OneToMany(mappedBy = "appMember")
    private List<Alarm> alarms = new ArrayList<>();

    @Builder
    public AppMember(String email, String nickname, String imgUrl, String firebaseToken, SocialProvider socialProvider, String accessToken, String refreshToken){
        this.email = email;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
        this.firebaseToken = firebaseToken;
        this.socialProvider = socialProvider;

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;

        this.changeAllAlarmStatus(AlarmReceivedStatus.Y);
        this.appMemberStatus = AppMemberStatus.ACTIVE;
    }

    public void changeAllAlarmStatus(AlarmReceivedStatus status) {
        this.allAlarmStatus = status;
        this.notificationAlarmStatus = status;
        this.rankingAlarmStatus = status;
        this.eventAlarmStatus = status;
    }

    public void changeNotificationAlarmStatus(AlarmReceivedStatus status){
        checkAllAlarm(status);
        this.notificationAlarmStatus = status;
    }

    public void changeRankingAlarmStatus(AlarmReceivedStatus status){
        checkAllAlarm(status);
        this.rankingAlarmStatus = status;
    }

    public void changeEventAlarmStatus(AlarmReceivedStatus status){
        checkAllAlarm(status);
        this.eventAlarmStatus = status;
    }

    public void checkAllAlarm(AlarmReceivedStatus status){
        // 전체 알림이 켜져 있고 그 중 특정 알림을 받고 싶지 않을 때
        if(status == AlarmReceivedStatus.N && allAlarmStatus == AlarmReceivedStatus.Y){
            allAlarmStatus = AlarmReceivedStatus.N;
        }
    }

    public void changeAppMemberStatus(AppMemberStatus appMemberStatus){
        if(appMemberStatus == AppMemberStatus.UNACTIVE){
            this.deletedAt = LocalDateTime.now();
        }
        this.appMemberStatus = appMemberStatus;
    }

}
