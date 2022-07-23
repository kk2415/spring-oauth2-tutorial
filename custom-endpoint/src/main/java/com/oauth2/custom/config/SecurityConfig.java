package com.oauth2.custom.config;

import com.oauth2.custom.security.jwt.TokenProvider;
import com.oauth2.custom.security.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.oauth2.custom.security.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.oauth2.custom.security.oauth2.oauth2UserService.CustomOAuth2UserService;
import com.oauth2.custom.security.oauth2.requestRepository.CookieAuthorizationRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired TokenProvider tokenProvider;
    @Autowired private CustomOAuth2UserService customOAuth2UserService;
//    @Autowired private CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository;
//    @Autowired private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
//    @Autowired private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors().and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().permitAll())
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .oauth2Login()
                    .authorizationEndpoint()
                        .baseUri("/oauth2/authorization")
                        .authorizationRequestRepository(cookieAuthorizationRequestRepository()).and()
                    .redirectionEndpoint()
                        .baseUri("/*/oauth2/code/*").and()
                    .userInfoEndpoint()
                        .userService(customOAuth2UserService).and()
//                    .defaultSuccessUrl("/oauth/redirect")
//                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/oauth/redirect"))
                    .successHandler(oAuth2AuthenticationSuccessHandler())
                    .failureHandler(oAuth2AuthenticationFailureHandler())
                .and().build();
    }

    @Bean
    public OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(cookieAuthorizationRequestRepository(),tokenProvider);
    }

    @Bean
    public OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository());
    }


    @Bean
    public CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new CookieAuthorizationRequestRepository();
    }

}
