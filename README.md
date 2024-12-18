
# 프로젝트 소개


**물류 관리 및 배송 시스템을 위한 MSA 기반 플랫폼**

-   B2B 기반의 물류 배송 시스템을 구축합니다.
-   메시징 큐를 활용하여 MSA 구조를 설계합니다.

# 프로젝트 주요 기능 및 팀소개

**💁‍♂️ Team Mosaic**: MSA의 "M"과 모듈성을 비유한 "Mosaic"

| **연이현** | **황시연** | **안주환** |
| --- | --- | --- |
| 계정/업체 관리 | 상품/재고 관리 | 허브 관리 |
| 슬랙 메시지 관리 | 주문 관리 | 허브간 이동 관리 |
| AI 응답 관리 | 배송 관리 | 배송 담당자 관리 |





# 인프라 아키텍처

![img (2)](https://github.com/user-attachments/assets/5e9bd947-8103-465b-b10d-fa1a12d2dbe9)

# 기술스택

-   **백엔드:** Spring Boot 3.4.0
-   **데이터베이스:** PostgreSQL , Redis
-   **빌드 툴:** Gradle
-   **API 문서화:** Swagger
-   **API 게이트웨이:** Spring Cloud Gateway
-   **서비스 디스커버리:** Spring Cloud Eureka
-   **버전 관리:** GitHub
-   **그외** : Apache Kafka

# 프로젝트 주요 기능 소개

### 🏭 허브간 이동 경로 로직화 및 캐싱

-   물류센터인 허브간의 거리를 계산한 값을 통해 배송 담당자를 배정합니다.
-   허브와 허브간의 정보를 레디스를 활용하여 빠르게 캐싱합니다.

### ✨ Gemini 를 활용한 배송 메시지 전송

-   배송 담당자에게 Slack 을 활용하여 배송 예상 시간을 전달합니다.
-   배송 경로 계산 결과를 적용하여 담당자들에게 공지합니다.

### 📬 KafKa 를 활용한 상품-주문-배송 메시징 처리

-   주문 생성 시 상품 재고 확인 후 차감 
-   주문 상태를 변경한 후 배송을 생성하는 프로세스가 구현

# 트러블 슈팅


### 카프카를 사용하면서 서비스간 정보를 전달할 때 Jpaauting 이슈가 발생


-   문제: 카프카를 통한 서비스 간 메시지 전달 시 JPA Auditing(AOP)에 문제 발생
    
-   원인: 기존 Auditing 설정이 HTTP 요청의 헤더 값에만 의존하여 적용되고 있었음해결
    
-   과정:
    
    1.  현재 상황 분석:
    
    -   JPA Auditing이 웹 요청의 헤더에서만 user_uuid를 가져오도록 구현되어 있음
    -   카프카 메시지 처리 시에는 HTTP 헤더가 없어 Auditing 정보 누락
    
    2.  대안 검토:
    
    a) 카프카 메시지에 user_uuid 포함: 메시지 크기 증가
    
    b) 글로벌 컨텍스트 사용: 스레드 안전성 문제 가능성
    
    3.  해결책 선택
    
    처음 b로 구현을 했지는 스레드 로컬을 해제해주는 타이밍이 빨라서 userid가 정확하게 기입이 안됨
    
    a로 전향해서 user_uuid를 메시지로 보내줌4. 구현: 카프카 리스너에서 메시지 처리 전 user_uuid를 설정하는 로직 추가
    
    JPA Auditing이 이 설정된 값을 사용하도록 AuditorAware 인터페이스 구현 수정
    
-   결과:
    
    AS-IS: 카프카 메시지 처리 시 Auditing 정보 누락, 불완전한 데이터 저장
    
    TO-BE: 카프카 메시지 처리를 포함한 모든 상황에서 정확한 Auditing 정보 기록
    
    -   시스템 일관성: HTTP 요청과 카프카 메시지 처리 간 Auditing 동작 통일

<br>

### Company ↔ User 연관관계 영향으로 인한 페이징 실패


-   문제:
    
    Company 조회시 `Could not write JSON: Could not initialize proxy` 라는 오류가 발생
    
-   원인:
    
    Company 내부의 OneToOne 관계인 User 객체를 Dto화 하지 않고 Entity 타입으로 Paging 처리하여 JSON parsing 실패 발생
    
    이는 JPA의 지연 로딩(Lazy Loading) 전략과 관련
    
-   해결과정
    
    1.  디버깅
        
        Gateway 에서 테스트 중 발견하여 각 서비스마다 디버깅을 찍으며 위치를 찾아보았다. Company Search 호출시 동일한 에러가 발생함하르 확인
        
    2.  구글링
        
        “Return 값으로 Entity 타입이 들어오기 때문에 오류 발생. 모든 반환 값은 반드시 DTO 처리”
        
    3.  단시간내 빠른 해결을 위해 QueryDSL 쿼리문을 작성하기 보다는 map . 을 활용하여 조회한 Company를 UserID 를 포함한 Dto 로 변환
        
-   AS-IS:
    
    -   Company 엔티티를 직접 반환하여 JSON 파싱 오류 발생
    -   지연 로딩된 User 객체로 인한 프록시 초기화 문제
-   TO-BE:
    
    -   Company 조회 결과를 DTO로 변환하여 반환
    -   UserID만 포함하여 지연 로딩 문제 해결
    -   JSON 파싱 오류 해결 및 정상적인 데이터 반환
    -   반환 값에 대한 주의깊은 고려의 중요성 인식

<br>

### 허브 최단 경로 조회 시 I/O와 연산 최적화

-   문제 상황
    -   허브 배송 경로를 조회할 때마다 허브 이동 정보를 기반으로 최단 경로 알고리즘을 실행하고 있었다. 이로 인해 **불필요한 I/O 및 연산이 발생**하여 서버의 성능이 저하
-   문제 정의
    1.  문제
        -   매번 최단 경로를 실시간으로 계산함으로써 성능 저하 발생
        -   CPU와 I/O 리소스 낭비로 시스템 응답 시간이 길어짐
    2.  문제 이유
        -   허브 이동 정보는 자주 변경되지 않으나, 동일한 최단 경로 계산이 반복적으로 수행됨
        -   서버 부하가 증가할수록 응답 시간이 더 길어지는 문제가 나타남
-   해결 과정
    -   **캐싱을 통한 최단 경로 사전 계산**
        -   서버 시작 시, 모든 허브를 기준으로 **최단 경로를 미리 계산**하고 캐시에 저장
        -   캐싱된 데이터를 활용해 허브 조회 시 **즉시 결과를 반환**하도록 구현
    -   **캐시 무효화 처리**
        -   허브 정보가 수정되거나 삭제되는 경우, 해당 캐시 데이터를 삭제
-   결과
    -   AS-IS
        -   허브 최단 경로 조회 시마다 알고리즘을 실행
        -   불필요한 CPU 연산과 I/O 발생
        -   서버 부하 증가 시 응답 지연
    -   TO-BE
        -   **서버 시작 시 최단 경로를 사전 계산**하고 캐싱
        -   허브 정보 변경 시 캐시 무효화 및 업데이트
        -   불필요한 연산과 I/O 제거로 **응답 시간 단축**
    -   개선 효과
        -   **서버 부하**: I/O 및 CPU 리소스 사용률 감소
        -   **안정성**: 트래픽이 증가해도 일관된 성능 유지

# ERD

![img (1)](https://github.com/user-attachments/assets/fb01f295-4009-4663-a8cf-00d09f79dbb0)
![img](https://github.com/user-attachments/assets/29a360d6-ce2d-4fb3-8ae4-c1f9575020ac)


