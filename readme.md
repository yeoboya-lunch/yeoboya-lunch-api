# 여보야 글로벌 API

### getting started

1. git clone ~
2. `resources/application-local.yml` 파일 생성

------------------------------------------------------------------------------------

### mapping 유의사항
1. 소문자
2. 동사보다는 명사 사용
3. 언더바(_) 대신 하이픈(-)
    - `GET /noLogging` x
    - `GET /no_nogging` x
    - `GET /no-loging` o
4. 마지막에 슬래시(/) 포함하지 말 것
5. 도메인 이름은 복수 명사를 사용
   - `GET /member/1` -> `GET /members/1`
6. 행위는 URL 대신 `http Method` 를 사용 (get/post/delete/put)
   - `GET /posts/delete/1` -> `DELETE /posts/1`

| CRUD                | HTTP verbs | Mapping       |
|---------------------|------------|---------------|
| resource 들의 목록을 표시  | GET        | /resource     |
| resource 하나의 내용을 표시 | GET        | /resource/:id |
| resource 를 생성       | POST       | /resource     |
| resource 를 수정       | PUT        | /resource/:id |
| resource 를 삭제       | DELETE     | /resource/:id |


------------------------------------------------------------------------------------

### security
여보야 글로벌 API 는 Bearer JWT 토큰 인증을 사용 한다.    
http 요청시 header 에  `Authorization: Bearer ${access token}` 작성   
토큰 없이 테스트 하고싶으면
`SecurityConfiguration.java` 파일에 test pass 밑에 RequestMatcher 작성

------------------------------------------------------------------------------------

### 구조 (mvc)
- controller (@RestController)
- service (@Service)
- repository (@Component 사용하지 말것)
- model

------------------------------------------------------------------------------------

### bean 주입
@Autowired 사용하지말고 생성자 주입 하는거 추천.  
[생성자주입 권장하는 이유](ttps://madplay.github.io/post/why-constructor-injection-is-better-than-field-injection)
~~~java
public class SampleController {

    private final SampleService sampleService;
    private final Response response;

    public SampleController(SampleService sampleService, Response response) {
        this.sampleService = sampleService;
        this.response = response;
    }
}
~~~

롬복 `@RequriedArgsConstructor` 어노테이션 이용해도 무방
~~~java
@RequiredArgsConstructor
public class SampleController {

    private final SampleService sampleService;
    private final Response response;
}
~~~

------------------------------------------------------------------------------------

### i18n
`resource/i18` 디렉터리에 다국어 처리 반드시 idea 통해서 작업 할 것 **리소스 번들**에서 수정 하면됨. (버전은 확인 안해봤음)

------------------------------------------------------------------------------------

### response
`congig/common/response` 사용  
fail 시 500 에러 말고 400 에러 사용하자.
[HTTP 상태 코드](https://developer.mozilla.org/ko/docs/Web/HTTP/Status)

------------------------------------------------------------------------------------

### redis
사용법은 `test/RedisUtilTest.java` 에서 확인

------------------------------------------------------------------------------------

### cache (caffeine)

------------------------------------------------------------------------------------