## Mungta Accusation Service (신고관리 서비스)

### Domain Modeling

![domain_modeling](./img/domain_modeling.png)



### Swagger

- http://localhost:8089/api/accusation/swagger-ui.html

![swagger](./img/swagger.png)



### Event 통신

![event](./img/event.png)



### SAGA 패턴

- 패널티 카운팅 실패시 "PenaltyFailed" Event를 받아 신고 처리한 상태를 롤백.

![saga](./img/saga.png)

### 신고 처리시 Email 전송

- 유저 서비스에서 패널티 카운팅 성공했다고 보낸 "PenaltySucceed" Event를 받으면 비동기로 Email 전송.
- Email Format은 Thymeleaf 사용.

![email](./img/email.png)



### 정적 분석

![sonarqube](./img/sonarqube.png)



### Jenkins

![jenkins](./img/jenkins.png)



### Infra

![infra](./img/infra.png)



### Kiali

![kiali](./img/kiali.png)



### Zipkin

![zipkin](./img/zipkin.png)
