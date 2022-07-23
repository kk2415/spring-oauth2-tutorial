package com.oauth2.custom.security.jwt;

import com.oauth2.custom.entity.Member;
import io.jsonwebtoken.*;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenProvider {

    private static final String SECRET_KEY = "NMA8JPctFuna59f5";// HS512 + 비밀키로 시그니쳐 값 생성

    /*
    * JWT 토큰 생성
    * */
    public String create(Member member) {
        Pair<String, Key> key = JwtKey.getRandomKey();
        Claims subject = Jwts.claims().setSubject(String.valueOf(member.getId())); //subject
        Date expireDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));

        //JWT Token 생성
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) //header

                .setClaims(subject) //subject
                .setIssuer("demo app") //iis
                .setIssuedAt(new Date()) //iat
                .setExpiration(expireDate) //exp

                .signWith(key.getSecond()) //sig
                .compact();
    }

    public String create(String id) {
        Pair<String, Key> key = JwtKey.getRandomKey();
        Claims subject = Jwts.claims().setSubject(id); //subject
        Date expireDate = Date.from(Instant.now().plus(30, ChronoUnit.MINUTES));

        //JWT Token 생성
        return Jwts.builder()
                .setHeaderParam(JwsHeader.KEY_ID, key.getFirst()) //header

                .setClaims(subject) //subject
                .setIssuer("demo app") //iis
                .setIssuedAt(new Date()) //iat
                .setExpiration(expireDate) //exp

                .signWith(key.getSecond()) //sig
                .compact();
    }

    /*
     * JWT 토큰 검증 및 토큰 subject 에 저정했던 데이터 반환
     * */
    public String getMemberId(String token) {
        return Jwts.parserBuilder()
                .setSigningKeyResolver(SigningKeyResolver.instance) //JWT 만들었을 때 사용했던 비밀키를 넣어줘야됨
                .build()
                .parseClaimsJws(token) //토큰 파싱 -> 토큰 검증 실패 시 에외 발생
                .getBody()
                .getSubject();
    }

}

//   [Header]
//   {
//    "alg" : "HS521",
//   }.
//
//   [payload]
//   {
//    "sub" : "40288093784915d20174916a40c0001",
//    "iis" : "demo app",
//    "iat" : 15973367,
//    "exp" : 15973367,
//   }.
//
//   [signature]
//   Nn4d1MOVLZg79sfFACTIpCPKqWmpZMZQsbNdJJNWKRv50_l7bPLQPwhMobT4BOG6Q3JYjhDrKFLBSaUxz
