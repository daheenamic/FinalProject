# EZENSEOULLO
[Spring Project] 가이드와 함께하는 서울 투어 예약 사이트

## 💻 프로젝트 소개
'유로자전거나라'(<https://www.eurobike.kr/>)'를 참고하여 만든 서울 투어 예약 사이트입니다.
* 가이드 회원은 본인이 운영하는 서울 투어 정보를 등록하고 수정한다.
* 일반 회원은 투어 정보를 확인하고 원하는 날짜의 투어를 예약한다.
* 회원들은 익명 커뮤니티와 후기 게시판에서 여행 정보를 공유할 수 있다.
* 사이트나 투어 관련 문의는 QnA 게시판에서 질문하여 답변을 받을 수 있다.
* 사이트는 가이드에게 플랫폼 이용료를 받아 수익을 창출한다.

## 🕰 개발 기간
* 2022.03.08. ~ 2022.04.12.

## 👭 담당자별 개발 내용
* ✔ 정다희(팀장) - 투어 예약 및 장바구니 기능, 메인 및 내비게이션 바 필터
* 정하영(PL) - 투어 CRUD, 로그 출력 AOP
* 김소연(PM) - 후기 CRUD, 별점 기능
* 서상희(DBA) - 회원관리 기능
* 김수현(테스트) - QnA CRUD, 익명 커뮤니티 CRUD

## ⚙️ 개발 환경
* Java 8
* JDK 1.8
* IDE: STS 4
* Framework : Spring
* Database : OracleDB 11g XE
* ORM : MyBatis
* DBCP : HikariCP
* 기타 주요 라이브러리 : jdbc, lombok, aspectJ, log4j, log4j2

***
## 📌 주요 개발 기능
#### 투어 예약 <a href="https://github.com/daheenamic/FinalProject/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-(%ED%88%AC%EC%96%B4%EC%98%88%EC%95%BD)">상세보기</a>
- 회원의 투어 일자와 인원 선택
- 예약자 정보 입력 후 결제 및 예약
- 가이드의 투어 상태 변경 (투어완료)
- 회원의 투어 예약 취소
- 관리자의 투어 예약 상태 변경 (예약완료, 예약취소)

#### 장바구니 <a href="https://github.com/daheenamic/FinalProject/wiki/%EC%A3%BC%EC%9A%94-%EA%B8%B0%EB%8A%A5-%EC%86%8C%EA%B0%9C-(%EC%9E%A5%EB%B0%94%EA%B5%AC%EB%8B%88)">상세보기</a>
- 투어 일자와 인원 선택 후 장바구니에 담기
- 장바구니 인원 수정과 장바구니 삭제
