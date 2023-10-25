package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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


//    @OneToMany(mappedBy = "appMember")
//    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Friendship> friendships = new ArrayList<>();

//    @OneToMany(mappedBy = "appMember")
//    private List<MemberMarker> memberMarkers = new ArrayList<>();

//    @OneToMany(mappedBy = "appMember")
//    private List<MemberMapArea> memberMapAreas = new ArrayList<>();

}
