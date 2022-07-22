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

# 예제 설명
## simple
index 페이지 접속시 바로 깃허브 로그인 페이지로 리다이렉트되는 간단한 Oauth2 로그인 예제 프로젝트

## two-provider
welcome 페이지에서 구글, 깃허브 둘 중 하나를 선택해서 로그인할 수 있는 예제 프로젝트

# Reference
https://spring.io/guides/tutorials/spring-boot-oauth2/#github-register-application
