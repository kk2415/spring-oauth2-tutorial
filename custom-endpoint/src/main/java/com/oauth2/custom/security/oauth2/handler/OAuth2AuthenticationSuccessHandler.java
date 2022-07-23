package com.oauth2.custom.security.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oauth2.custom.entity.LoginResponse;
import com.oauth2.custom.security.jwt.TokenProvider;
import com.oauth2.custom.security.oauth2.CookieUtils;
import com.oauth2.custom.security.oauth2.ProviderType;
import com.oauth2.custom.security.oauth2.requestRepository.CookieAuthorizationRequestRepository;
import com.oauth2.custom.security.oauth2.userInfo.OAuth2UserInfo;
import com.oauth2.custom.security.oauth2.userInfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.oauth2.custom.security.oauth2.requestRepository.CookieAuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
    private final TokenProvider tokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("start onAuthenticationSuccess");

        String targetUrl = determineTargetUrl(request, response, authentication);

        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("start determineTargetUrl");

        String url = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue)
                .orElse(getDefaultTargetUrl());

        OAuth2UserInfo userInfo = getUserInfo(authentication);
        String token = tokenProvider.create(userInfo.getId());

        // 토큰을 쿼리스트링에 넣지 않으면 onAuthenticationSuccess()가 실행되지 않는다.
        return UriComponentsBuilder.fromUriString(url)
                .queryParam("token", token)
                .build().toUriString();
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        log.info("start clearAuthenticationAttributes");

        super.clearAuthenticationAttributes(request);
        cookieAuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }


    private OAuth2UserInfo getUserInfo(Authentication authentication) {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        ProviderType providerType = ProviderType.valueOf(authToken.getAuthorizedClientRegistrationId().toUpperCase());

        OidcUser user = ((OidcUser) authentication.getPrincipal());
        return OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, user.getAttributes());
    }

}
