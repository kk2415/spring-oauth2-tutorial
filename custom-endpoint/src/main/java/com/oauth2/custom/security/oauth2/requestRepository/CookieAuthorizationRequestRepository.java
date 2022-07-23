package com.oauth2.custom.security.oauth2.requestRepository;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;
import com.oauth2.custom.security.oauth2.CookieUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * AuthorizationRequestRepository 를 그대로 사용하지 않고 커스텀한 이유는
 * 기본 AuthorizationRequestRepository 은 AuthorizationRequest 를 세션에 저장한다.
 * 하지만 stateless 방식으로 진행하고 싶어서 Cookie 담아야한다.
 * 그래서 AuthorizationRequest 를 Cookie 에 담는 로직을 만들었다.
 * */
@Slf4j
public class CookieAuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public final static String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public final static String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public final static String REFRESH_TOKEN = "refresh_token";
    private final static int cookieExpireSeconds = 180;

    /**
     * 쿠키 안에 담겨있는 OAuth2AuthorizationRequest 리턴해주는 메서드
     * */
    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("start loadAuthorizationRequest");

        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    /**
     * authorizationRequest을 담는 쿠키
     * redirect_uri을 담는 쿠키
     * 각 각 하나씩 추가
     * */
    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
            CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
            return;
        }

        log.info("set authorizationRequest in cookie");
        CookieUtils.addCookie(response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);
        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            log.info("set redirect_uri in cookie");
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
    }

}
