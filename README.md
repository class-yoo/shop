# shop

## ✅ 개발 환경 (Docker 기반)

- Java: OpenJDK 21 (Temurin-21)
- Spring Boot: 3.3.10
- Kotlin: 1.9.25
- Build: Gradle 8.5+
- DB: H2 (In-memory)
- 실행: Docker 기반 빌드 & 실행 (로컬 JDK/Gradle 설치 불필요)
  - 기본적으로 빌드 및 실행을 docker 기반으로 진행하고 있습니다.
  - 만약 호스트에서 직접 실행하고 싶은 경우 아래 `2. 빌드 및 실행`을 확인해주세요.


## 🛠️ 실행 방법
```shell
# 1. Git clone
git clone https://github.com/class-yoo/shop.git
cd shop-test

# 2. Docker build
docker build -t shop-app .

# 3. Docker run 
docker run -d \
  -e SPRING_PROFILES_ACTIVE=local \
  -p 8080:8080 \
  shop-app

# host에서 이미 8080 포트를 사용중이라면..
docker run -d \
  -e SPRING_PROFILES_ACTIVE=local \
  -p 8089:8080 \
  shop-app
```

---

## 1. 환경구성

### sdkman 설치 (jdk 설치를 위한 sdkman 설치) [https://sdkman.io/]

- 실제 빌드는 도커 컨테이너 내부에서 진행하지만, 로컬개발환경 구축을 위해 설치 진행
```shell
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### JDK 설치 (Temurin JDK)
```shell
sdk install java 21-tem
sdk default java 21-tem

# 설치 후
java --version

openjdk 21 2023-09-19 LTS
OpenJDK Runtime Environment Temurin-21+35 (build 21+35-LTS)
OpenJDK 64-Bit Server VM Temurin-21+35 (build 21+35-LTS, mixed mode, sharing)
```

## 2. 빌드 및 실행


### 호스트에서 빌드 및 실행

```shell
./gradlew build

# 빌드 후 정상동작을 안하는 경우 clean 후 다시 빌드
./gradlew clean build

# 로그출력으로 실행
./gradlew bootRun --args='--spring.profiles.active=local'

# 데몬으로 실행
./gradlew bootRun --args='--spring.profiles.active=local' > /dev/null 2>&1 &
```

### 도커로 빌드 및 실행

```shell
# 도커 이미지 빌드 및 결과 확인
docker build -t shop-app .

docker images | grep shop-app
shop-app                  latest         6e2b4ebb1167   5 minutes ago   494MB
 
# 도커 컨테이너 데몬으로 실행 및 결과 확인
docker run -d \
  -e SPRING_PROFILES_ACTIVE=local \
  -p 8080:8080 \
  shop-app

docker ps | grep shop-app
1e707f89fecc   shop-app   "java -jar app.jar"   11 seconds ago   Up 10 seconds   0.0.0.0:8080->8080/tcp   determined_rosalind
```

## 3. 구현범위 설명

### ✅ 기능 1 - 카테고리별 최저가 브랜드 및 총액 조회

#### 📌 기능 설명

무신사의 코디 구성 8개 카테고리(상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리)에 대해  
각 카테고리별로 가장 저렴한 상품을 가진 브랜드와 가격을 조회하고,  
모든 상품의 총합 금액을 계산하여 응답하는 API입니다.

#### ✔️ 구현 범위

- 상품 데이터는 브랜드-카테고리별로 하나씩 존재한다고 가정
- DB에서 카테고리별 모든 상품을 조회 후, 가격이 가장 낮은 상품 1개씩 선택
- 총합 가격은 해당 상품들의 가격을 합산하여 계산
- 초기 데이터는 local profile에서 자동 로딩 (ApplicationRunner)
- 에러 발생 시 공통 예외 처리 (`GlobalExceptionHandler`) 적용

#### 🛠 API 상세

- **HTTP Method**: `GET`
- **Endpoint**: `/api/v1/price/lowest`
- **Request Body**: 없음
- **Response**:

```json
{
  "items": [
    { "category": "상의", "brand": "C", "price": 10000 },
    { "category": "아우터", "brand": "E", "price": 5000 },
    ...
  ],
  "totalPrice": 34100
}
```

- **Error Response**
```json
{
  "message": "카테고리 OUTER 에 해당하는 상품이 없습니다.",
  "status": 400
}
```

---

### ✅ 기능 2 - 단일 브랜드로 전체 카테고리 상품을 구매할 때 최저가 브랜드 및 총액 조회

#### 📌 기능 설명

각 브랜드는 모든 카테고리에 대해 하나의 상품을 가지고 있다고 가정하고,  
모든 카테고리(상의, 아우터, 바지, 스니커즈, 가방, 모자, 양말, 액세서리)를  
**단일 브랜드로 구성**했을 때 **총합 가격이 가장 저렴한 브랜드**와  
카테고리별 상품 가격 목록 및 총액을 응답하는 API입니다.

#### ✔️ 구현 범위

- 브랜드별로 카테고리 상품 가격을 합산하여 총액을 계산
- 모든 카테고리에 상품을 보유한 브랜드만 고려
- 총합이 가장 낮은 브랜드 1개만 응답
- Seed 데이터 기준으로 정확히 8개 카테고리 존재한다고 가정
- 예외 처리: 조건을 만족하는 브랜드가 없을 경우 400 응답 발생

#### 🛠 API 상세

- **HTTP Method**: `GET`
- **Endpoint**: `/api/v1/price/lowest-brand`
- **Request Body**: 없음

- **Response**:

```json
{
  "최저가": {
    "brand": "D",
    "categories": [
      { "category": "상의", "price": 10100 },
      { "category": "아우터", "price": 5100 },
      ...
    ],
    "totalPrice": 36100
  }
}
```

- **Error Response**
```json
{
  "message": "카테고리 OUTER 에 해당하는 상품이 없습니다.",
  "status": 400
}
```