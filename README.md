# 게시판 서비스 프로젝트

<<<<<<< HEAD

=======
>>>>>>> 35226672b2b95f5b74b8318a51e5ca5378840c45
**게시판 서비스 프로젝트의 목표**

   ****

- 누구나 이해하기 쉬운 소재로 명확한 기능 요구사항
- 요구사항을 구현하는데 각종 문서 작업
- Spring boot 요구사항을 실제로 구현하는 기술적인 방법
- 기획과 문서 작성부터 개발, 형상관리, 테스트, 배포까지 개발 프로세스

**개발의 목적**

- 고객의 니즈와 문제를 정리
    - 공부가 목표
- 문제 → 요구사항 → 기능 도출 → 구현 방안의 기획 → 개발 계획 수립 → 실행

**테스트와 배포**

- 테스트
    - 개발 요구사항이 빠짐 없이 모두 구현되었는가
    - 구현된 요구사항이 오류 없이 동작하는가
- 배포
    - 깃헙 릴리즈 작성
    - 클라우드 서버에 배포 (헤로쿠)

**개발 Stack**

- Java + Srping Boot 기반에서 선택
- 웹 서비스 제공 Spring Web
- 도메인의 설계와 DB 저장
    - Spring Data JPA
    - H2 Database
    - MySQL Driver
- JSON API 로 데이터 제공
    - Rest Repositories
    - Rest Repositories HAL Explorer
- 웹 화면 ( Thymeleaf )
- 디자인 요소 ( Bootstrap 5.2 )
- 적절한 입출력 데이터의 검증 ( Validation )
- 인증 기능 ( Spring Security )
- 생산성에 도움이 되는 도구들 선택
    - Lombok
    - Spring Boot DevTools
    - Spring Boot Actuator

**세부 기능들**

- 게시판, 댓글 도메인의 설계
- 도메인 데이터를 DB 에 저장
- JSON API 로 데이터 제공
- 사용자에게 웹 화면으로 서비스 제공 + 디자인 요소
- 적절한 입출력 데이터의 검증
- 인증 기능
- 생산성에 도움이 되는 도구들 선택
