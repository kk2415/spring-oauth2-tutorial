package com.oauth2.custom.security.oauth2.oauth2UserService;

import com.oauth2.custom.entity.Member;
import com.oauth2.custom.entity.RoleType;
import com.oauth2.custom.repository.MemberRepository;
import com.oauth2.custom.security.oauth2.ProviderType;
import com.oauth2.custom.security.oauth2.UserPrincipal;
import com.oauth2.custom.security.oauth2.userInfo.OAuth2UserInfo;
import com.oauth2.custom.security.oauth2.userInfo.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("start loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);

        ProviderType providerType = ProviderType.valueOf(userRequest.getClientRegistration().getRegistrationId().toUpperCase());
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(providerType, oAuth2User.getAttributes());
        Optional<Member> optionalMember = memberRepository.findByEmail(userInfo.getEmail());
        Member member;

        if (optionalMember.isPresent()) {
            member = optionalMember.get();
            updateMember(member, userInfo);
        }
        else {
            member = createMember(userInfo, providerType);
        }

        UserPrincipal principal = UserPrincipal.create(member, oAuth2User.getAttributes());
//        setAuthentication(principal, member);

        return principal;
    }

    private Member createMember(OAuth2UserInfo userInfo, ProviderType providerType) {
        log.info("create Member");

        Member member = Member.builder()
                .memberId(userInfo.getId())
                .email(userInfo.getEmail())
                .name(userInfo.getName())
                .providerType(providerType)
                .role(RoleType.USER)
                .build();

        memberRepository.save(member);
        return member;
    }

    private void updateMember(Member member, OAuth2UserInfo userInfo) {
        log.info("update Member");

        if (userInfo.getName() != null && !member.getName().equals(userInfo.getName())) {
            member.setName(userInfo.getName());
        }
    }

    private void setAuthentication(UserPrincipal principal, Member member) {
        log.info("start setAuthentication");
        SecurityContext context = SecurityContextHolder.getContext();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole().getCode()));

        AbstractAuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(principal, authorities, principal.getProviderType().name());

        context.setAuthentication(authenticationToken);
    }

}
