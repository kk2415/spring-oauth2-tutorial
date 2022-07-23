package com.oauth2.custom.entity;

import com.oauth2.custom.security.oauth2.ProviderType;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Builder
@Table(name = "member")
@Entity
@AllArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    String memberId;
    String email;
    @Setter String name;
    String password;

    @Column(name = "PROVIDER_TYPE", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    protected Member() {}

}
