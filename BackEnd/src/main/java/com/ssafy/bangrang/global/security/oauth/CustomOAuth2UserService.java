package com.ssafy.bangrang.global.security.oauth;

import com.ssafy.bangrang.domain.member.entity.AppMember;
import com.ssafy.bangrang.domain.member.model.vo.SocialProvider;
import com.ssafy.bangrang.domain.member.repository.AppMemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final AppMemberRepository appMemberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // loadUser : 소셜 로그인 API의 사용자 정보 제공 URI로 요청
        // => 사용자 정보를 얻은 후 객체 반환
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // userRequest에서 registrationId 추출 후 registrationId으로 SocialType 저장
        // userNameAttributeName은 이후에 nameAttributeKey로 설정
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = getSocialProvider(registrationId);

        // OAuth2 로그인 시 PK
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println(userRequest.getAccessToken());

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialProvider, userNameAttributeName, attributes);

        AppMember createdUser = getUser(extractAttributes, socialProvider);

        /**
         * 추후 '탈퇴한 회원입니다' 에러 메시지 날라가게 처리해주기
         */
//        if(createdUser.getDeletedDate() != null) {
//            throw new OAuth2AuthenticationException("탈퇴한 회원입니다.");
//        }

        // DefaultOAuth2User를 구현한 CustomOAuth2User 객체를 생성하여 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("?")),
                attributes,
                extractAttributes.getNameAttributeKey(),
                createdUser.getEmail(),
                createdUser.getNickname()
        );
    }

    private SocialProvider getSocialProvider(String registrationId) {
        if(NAVER.equals(registrationId))
            return SocialProvider.NAVER;

        if(KAKAO.equals(registrationId))
            return SocialProvider.KAKAO;

        if(GOOGLE.equals(registrationId))
            return SocialProvider.GOOGLE;

        return null;
    }

    private AppMember getUser(OAuthAttributes oAuthAttributes, SocialProvider socialProvider) {
        AppMember user = appMemberRepository.findBySocialTypeAndSocialId(
                        socialProvider,
                        oAuthAttributes.getOAuth2UserInfo().getId())
                .orElse(null);

        if(user == null)
            return saveUser(oAuthAttributes, socialProvider);

        return user;
    }

    private AppMember saveUser(OAuthAttributes oAuthAttributes, SocialProvider socialProvider) {
        AppMember user = oAuthAttributes.toEntity(socialProvider, oAuthAttributes.getOAuth2UserInfo());

        AppMember saveUser = appMemberRepository.save(user);

        return saveUser;
    }
}
