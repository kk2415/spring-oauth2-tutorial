package com.oauth2.custom.controller;

import com.oauth2.custom.security.oauth2.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    @GetMapping("/api/member")
    public String getMemberName(@AuthenticationPrincipal UserPrincipal principal) {
        if (principal == null) {
            return null;
        }
        return principal.getAttribute("name");
    }

}
