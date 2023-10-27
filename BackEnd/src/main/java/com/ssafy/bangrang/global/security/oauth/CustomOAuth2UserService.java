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
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final AppMemberRepository appMemberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // loadUser : 소셜 로그인 API의 사용자 정보 제공 URI로 요청
        // => 사용자 정보를 얻은 후 객체 반환
        OAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // userRequest에서 registrationId가 NAVER, KAKAO 같은 소셜 키워드임
        // userNameAttributeName은 이후에 nameAttributeKey로 설정
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        // userNameAttributeName은 이후에 nameAttributeKey로 설정  'id' & 'sub' 이거로 소셜 설정해줘야함
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        // 소셜 로그인에서 API가 제공하는 userInfo의 Json 값(유저 정보)
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 위에 3요소 합쳐주기
        OAuthAttributes Attributes = OAuthAttributes.of(registrationId, userNameAttributeName, attributes);

        AppMember appMember = findUser(Attributes,registrationId);

        /**
         * 추후 '탈퇴한 회원입니다' 에러 메시지 날라가게 처리해주기
         */
//        if(appMember.getDeletedDate() != null) {
//            throw new OAuth2AuthenticationException("탈퇴한 회원입니다.");
//        }

        // DefaultOAuth2User 객체를 생성하여 반환
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("user")),
                attributes,
                Attributes.getNameAttributeKey(),
                appMember.getEmail()
        );
    }

    // 계정을 찾아보고
    private AppMember findUser(OAuthAttributes oAuthAttributes, String registrationId) {
        AppMember user = appMemberRepository.findByEmail(registrationId + "@" + oAuthAttributes.getOAuth2UserInfo().getId())
                .orElse(null);

        if(user == null)
            // 없으면 생성
            return saveUser(oAuthAttributes, registrationId);

        return user;
    }

    // 소셜 계정 생성 코드
    private AppMember saveUser(OAuthAttributes oAuthAttributes, String registrationId) {
        AppMember user = oAuthAttributes.toEntity(registrationId, oAuthAttributes.getOAuth2UserInfo());
        return appMemberRepository.save(user);
    }

}
