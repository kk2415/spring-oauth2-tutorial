# spring-oauth2-tutorial
# OAuth2 용어 정리
용어 | 설명
-- | --
Authorization Server (권한 서버) | 권한을 관리하는 서버로 Access Token, Refresh Token을 발급 및 관리한다. 배달의 민족을 구글 아이디로 가입했다면 해당 구글 계정에 대한 인증과 토큰 발급을 담당하는 서버로 이이해할 수 있습니다
Resource Server (리소스 서버) | 서비스를 제공하는 서버. 배달의 민족을 구글 아이디로 가입했다면, 권한 서버는 구글 서버, 리소스 서버는 배민 서버에 해당한다
Resource Owner (리소스 주인) | 해당 리소스(계정)의 주인으로, 일반 사용자의 경우 계정의 소유자에 해당한다
Client |  Resource Owner를 대리하여 리소스 요청을 하는 애플리케이션 규모가 작은 프로젝트에서는 Resource Server와 같은 애플리케이션일 수 있다.
Access Token |  Authorization Server가 Resource Owner를 식별하여 발급받은 키
Refresh Token |  Access Token를 재발급 받을 때 사용하는 키

# Spring Security OAuth2
### OAuth2AuthorizationRequest
인가 응답을 연계하고 검증할 때 사용한다.  
### AuthorizationRequestRepository
AuthorizationRequestRepository는 인가 요청을 시작한 시점부터 인가 요청을 받는 시점까지 (콜백) OAuth2AuthorizationRequest를 유지해준다.
기본 구현체는 세션에 저장하는 HttpSessionOAuth2AuthorizationRequestRepository

# 예제 설명
## simple
index 페이지 접속시 바로 깃허브 로그인 페이지로 리다이렉트되는 간단한 Oauth2 로그인 예제 프로젝트

## two-provider
welcome 페이지에서 구글, 깃허브 둘 중 하나를 선택해서 로그인할 수 있는 예제 프로젝트

# Reference
https://spring.io/guides/tutorials/spring-boot-oauth2/#github-register-application  
https://velog.io/@devsh/%EC%8A%A4%ED%94%84%EB%A7%81-%EC%8B%9C%ED%81%90%EB%A6%AC%ED%8A%B8-OAuth2-Filter-OAuth2AuthorizationRequestRedirectFilter-%EB%82%B4%EB%B6%80-%EB%A1%9C%EC%A7%81  
https://deeplify.dev/back-end/spring/oauth2-social-login#%EC%8B%9C%ED%80%80%EC%8A%A4-%EC%84%A4%EB%AA%85  
https://jyami.tistory.com/121
