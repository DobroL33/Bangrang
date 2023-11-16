package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.event.entity.Likes;
import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.stamp.entity.AppMemberStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("app")
@ToString(of = {"nickname"})
public class AppMember extends Member{

    @Column(name = "app_member_nickname", unique = true)
    protected String nickname;

    @Column(name = "app_member_img_url")
    protected String imgUrl;

    @Column(name = "app_member_firebase_token")
    private String firebaseToken;

    @Column(name = "app_member_deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "app_member_alarms")
    private Boolean alarms;

    @OneToMany(mappedBy = "appMember")
    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Friendship> friendships = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<AppMemberStamp> appMemberStamps = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<MemberMarker> memberMarkers = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<MemberMapArea> memberMapAreas = new ArrayList<>();

    @Builder
    public AppMember(String id, String nickname, String password, String imgUrl,
                     String firebaseToken){
        this.id = id;
        this.nickname = nickname;
        this.password = password;
        this.imgUrl = imgUrl;
        this.firebaseToken = firebaseToken;
        this.alarms = false;
    }

    /**
     * 닉네임 변경
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 유저 알람 설정 on/off
     */
    public void alarmOnOff(Boolean alarmSet) {
        this.alarms = !alarmSet;
    }

    /**
     * 회원탈퇴 등록
     */
    public void updateDeletedDate() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * 회원탈퇴 취소
     */
    public void cancelDeletedDate() {
        this.deletedAt = null;
    }

    public void updateProfileImg(String imgPath) {
        this.imgUrl=imgPath;
    }

    public void updateFirebase(String token) {
        this.firebaseToken=token;
    }
}
