package com.oauth2.custom.security.oauth2.userInfo;

import com.oauth2.custom.security.oauth2.ProviderType;
import com.oauth2.custom.security.oauth2.userInfo.impl.*;

import java.util.Map;

/**
 * Provider 마다 UserInfo 를 구현한 이유는
 * Provider 마다 attributes 안에 저장된 내용이 조금씩 다르기 때문이다.
 * attributes 는 Map 자료구조로 되어있는데
 * Provider 마다 key 이름이 다르고 value 의 자료형이 다르다.
 * */
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(ProviderType providerType, Map<String, Object> attributes) {
        switch (providerType) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attributes);
            case FACEBOOK: return new FacebookOAuth2UserInfo(attributes);
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            case GITHUB: return new GithubOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
