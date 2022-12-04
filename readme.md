점심

- 회원가입 인증 (이메일/문자)

- 테스트코드 작성
- jasypt
- 테스트 후 데이터 초기화 방법
- front 작성 후 oauth




## issue
### @SpringBootTest profile ? jasypt
clean bootRun -Pprofile=local
clean bootRun -Pprofile=dev
환경변수 
- jasypt.encryptor.password=passkey

**clean build -Pprofile=local**
1. clean build -Pprofile=local -x test
   - >Task :asciidoctor FAILED
   - 테스트를 안할수는 없음

2. clean build -Pprofile=local
   - >ApplicationTests > contextLoads() FAILED
   - 빌드 할때 jsaypt 변수를 넘겨주는 방법을 찾아야함