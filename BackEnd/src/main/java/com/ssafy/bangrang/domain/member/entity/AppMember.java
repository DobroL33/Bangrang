package com.ssafy.bangrang.domain.member.entity;

import com.ssafy.bangrang.domain.inquiry.entity.Inquiry;
import com.ssafy.bangrang.domain.map.entity.MemberMapArea;
import com.ssafy.bangrang.domain.map.entity.MemberMarker;
import com.ssafy.bangrang.domain.member.api.request.AppMemberNicknamePlusRequestDto;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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


//    @OneToMany(mappedBy = "appMember")
//    private List<Inquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "appMember")
    private List<Friendship> friendships = new ArrayList<>();

//    @OneToMany(mappedBy = "appMember")
//    private List<MemberMarker> memberMarkers = new ArrayList<>();

//    @OneToMany(mappedBy = "appMember")
//    private List<MemberMapArea> memberMapAreas = new ArrayList<>();
    @Builder
    public AppMember(Long idx, String email, String nickname,  String imgUrl) {
        this.idx = idx;
        this.email = email;
        this.nickname = nickname;
        this.imgUrl = imgUrl;
    }


    public Long updateProfileImage(String profileImg) {
        this.imgUrl = profileImg;

        return this.idx;
    }

    /**
     * 닉네임 변경
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * 소셜 회원탈퇴 등록
     */
//    public void updateSocialDelete() {
//        this.socialProvider = null;
//        this.deletedDate = LocalDateTime.now();
//    }

    /**
     * 회원탈퇴 날짜 등록
     */
//    public void updateDeletedDate() {
//        this.deletedDate = LocalDateTime.now();
//    }
    public void updateAppMemberNicknamePlus(AppMemberNicknamePlusRequestDto appMemberNicknamePlusRequestDto) {
        this.nickname = appMemberNicknamePlusRequestDto.getNickname();
    }

}
