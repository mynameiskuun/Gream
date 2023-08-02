# Gream
의류 전문 온라인 쇼핑몰.

### ⭐️ [프로젝트 바로가기](http://ec2-3-36-228-95.ap-northeast-2.compute.amazonaws.com)

### ⭐️ 프로젝트 기간
2023/04/15 ~ 진행중

### ⭐️ 프로젝트 목표

* MVC Model, Layered-Architecture에 입각한 역할의 분리.
* 람다, 스트림, 3항 연산자를 통한 코드의 가독성 향상.
* 이해를 기반으로 한 코드 작성.

<!--### 카카오 오븐 링크

* 프로젝트 요약
- - - -->


## UseCase
<img width="990" alt="Use-Case" src="https://github.com/mynameiskuun/Gream/assets/87435491/d51b6a10-bf47-4628-a5ed-ad382cc674bd"><br><br><br>


## 사용 기술
![Gream_사용기술](https://github.com/mynameiskuun/Gream/assets/87435491/7e9dc294-bfb6-4c9e-8fe5-68e913ad6bd2)<br><br><br>


## ERD
![Gream_erd](https://github.com/mynameiskuun/Gream/assets/87435491/e6a834c6-23a8-40c2-85c6-4eb490443852)<br><br><br>


## 구현 기능

### ⭐️ 구매자<br>
* 로그인, 회원가입
* 상품 조회, 주문, 결제
* 결제 시 할인쿠폰 사용
* 결제 시 포인트 사용 및 적립
* 장바구니 저장, 추가, 삭제
* 리뷰 작성, 문의글 작성.
* 상품 좋아요 기능
<!-- * 쿠폰 저장, 사용
* 댓글 작성, 삭제
* 포인트 사용 -->

### ⭐️ 관리자<br>
* 상품 조회, 등록, 삭제
* 주문내역 메일 발송.
* 할인쿠폰 발급, 조회, 삭제
<!-- * 판매기록 조회, 매출현황 조회
* 배송처리
* 쿠폰 발급, 삭제 -->

### ⭐️ 현재 구현중 기능
* 관리자 시기별 판매기록 조회, 매출현황 조회
* 추후 발생 가능한 동시성 문제 해결
* Spring Batch를 이용한 발급 쿠폰 기간만료 처리 기능


## 프로젝트를 진행하며 경험하고 체득한 부분

* 로그인(Spring Security)
> [바로가기](https://trusting-judge-fc4.notion.site/Spring-security-92e97a7a8644419e9815d3845a119f89?pvs=4)<br>

* Table을 대신하는 Enum을 통한 데이터 관리
> [바로가기](https://trusting-judge-fc4.notion.site/DB-3039da4956ec4378ae1588f3a7cab9e3?pvs=4)<br>

[//]: # (* CI / CD 구현을 통해 학습한 WAS)

[//]: # (> [바로가기]&#40;https://notion.com/mynameiskuun&#41;)



- - -





