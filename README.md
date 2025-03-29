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

