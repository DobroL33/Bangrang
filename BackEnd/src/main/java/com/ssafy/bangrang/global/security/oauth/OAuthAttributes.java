package com.ssafy.bangrang.global.security.oauth;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;

import com.ssafy.bangrang.global.security.oauth.userinfo.GoogleOAuth2UserInfo;
import com.ssafy.bangrang.global.security.oauth.userinfo.KakaoOAuth2UserInfo;
import com.ssafy.bangrang.global.security.oauth.userinfo.NaverOAuth2UserInfo;
import com.ssafy.bangrang.global.security.oauth.userinfo.OAuth2UserInfo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

/*
 * 각 소셜 별로 제공하는 데이터가 다름.
 * 소셜 별로 데이터를 받는 데이터를 분기 처리하는 DTO
 * */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuthAttributes {

    // OAuth2 로그인 진행 시 Key가 되는 필드 값, PK와 같은 의미
    private String nameAttributeKey;

    // 소셜 타입 별 로그인 유저 정보 (닉네임, 프로필 사진 등)
    private OAuth2UserInfo oAuth2UserInfo;

    // 소셜 종류 KAKAO, GOOGLE, NAVER
    private String registrationId;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo,String registrationId) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
        this.registrationId = registrationId;
    }

    /*
     * SocialType에 맞는 메서드 호출 => OAuthAttributes 객체 반환
     * */
    public static OAuthAttributes of(String registrationId, String userNameAttributeName,
                                     Map<String, Object> attributes) {
        if(registrationId == "NAVER")
            return ofNaver(registrationId, userNameAttributeName, attributes);
        else if(registrationId == "KAKAO")
            return ofKakao(registrationId,userNameAttributeName, attributes);
        else if(registrationId == "GOOGLE")
            return ofGoogle(registrationId, userNameAttributeName, attributes);
        else
            return null;
    }

    private static OAuthAttributes ofNaver(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofKakao(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofGoogle(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .registrationId(registrationId)
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    /*
     * User 엔티티 객체 생성
     * */
    public AppMember toEntity(String registrationId, OAuth2UserInfo oAuth2UserInfo) {
        return AppMember.builder()
                .email(registrationId + "@" + oAuth2UserInfo.getId()) // KAKAO@1231221
                .imgUrl(oAuth2UserInfo.getProfileImg()) // 소셜 이미지 불러와서 넣어주기
                .build();
    }
}
