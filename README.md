### shop test 프로젝트에 대한 설명을 기록한 README 파일입니다.

# 순서
- 개발 환경
- 실행 방법
- 환경구성
- 빌드 및 실행
- 구현범위 설명
  - Entity 설계
  - API 로직 설명
- 테스트 방법

# ✅ 개발 환경 (Docker 기반)

- Java: OpenJDK 21 (Temurin-21)
- Spring Boot: 3.3.10
- Kotlin: 1.9.25
- Build: Gradle 8.5+
- DB: H2 (In-memory)
- 실행: Docker 기반 빌드 & 실행 (로컬 JDK/Gradle 설치 불필요)
  - 기본적으로 빌드 및 실행을 docker 기반으로 진행하고 있습니다.
  - 만약 호스트에서 직접 실행하고 싶은 경우 아래 `2. 빌드 및 실행`을 확인해주세요.


# 🛠️ 실행 방법

만약 호스트에서 직접 실행하고 싶은 경우 아래 `2. 빌드 및 실행`을 확인해주세요.

```shell
# 1. Git clone
git clone https://github.com/class-yoo/shop.git
cd shop

# 2. Docker build (프로젝트 루트 디렉토리)
docker build -t shop-app .

# 2-1. build 된 이미지 확인
docker images | grep shop-app
shop-app                  latest         6e2b4ebb1167   5 minutes ago   494MB

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

# 3-1 실행중인 컨테이너 확인
docker ps | grep shop-app
1e707f89fecc   shop-app   "java -jar app.jar"   11 seconds ago   Up 10 seconds   0.0.0.0:8080->8080/tcp   determined_rosalind
```

---

# 1. 환경구성

### sdkman 설치 (jdk 설치를 위한 sdkman 설치) [https://sdkman.io/]

- 호스트에서 실행하실게 아니면 이 구간은 넘어가셔도 됩니다. 
```shell
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
```

### JDK 설치 (Temurin JDK)
- 호스트에서 실행하실게 아니면 이 구간은 넘어가셔도 됩니다.
```shell

sdk install java 21-tem
sdk default java 21-tem

# 설치 후
java --version

openjdk 21 2023-09-19 LTS
OpenJDK Runtime Environment Temurin-21+35 (build 21+35-LTS)
OpenJDK 64-Bit Server VM Temurin-21+35 (build 21+35-LTS, mixed mode, sharing)
```

### Git Clone - 필수
```shell
git clone https://github.com/class-yoo/shop.git
cd shop
```

# 2. 빌드 및 실행


### 호스트에서 빌드 및 실행
- 호스트에서 실행하실게 아니면 이 구간은 넘어가셔도 됩니다.
```shell
./gradlew build

# 빌드 후 정상동작을 안하는 경우 clean 후 다시 빌드
./gradlew clean build

# 로그출력으로 실행
./gradlew bootRun --args='--spring.profiles.active=local'

# 데몬으로 실행 & 실행한 process(부트 서버) kill
./gradlew bootRun --args='--spring.profiles.active=local' > /dev/null 2>&1 &
[1] 56718 # 실행된 PID

pkill 56718
```

### 도커로 빌드 및 실행

```shell
# 1. Docker build (프로젝트 루트 디렉토리)
docker build -t shop-app .

# 1-1. build 된 이미지 확인
docker images | grep shop-app
shop-app                  latest         6e2b4ebb1167   5 minutes ago   494MB

# 2. Docker run 
docker run -d \
  -e SPRING_PROFILES_ACTIVE=local \
  -p 8080:8080 \
  shop-app

# host에서 이미 8080 포트를 사용중이라면..
docker run -d \
  -e SPRING_PROFILES_ACTIVE=local \
  -p 8089:8080 \
  shop-app

# 2-1. 실행중인 컨테이너 확인
docker ps | grep shop-app
1e707f89fecc   shop-app   "java -jar app.jar"   11 seconds ago   Up 10 seconds   0.0.0.0:8080->8080/tcp   determined_rosalind
```

# 3. 구현범위 설명

## Entity 설계
### `Brand` - [Brand.kt](src/main/kotlin/com/shoptest/domain/brand/Brand.kt)
- **`id`** (Primary Key): 브랜드 ID
- **`name`** (Unique, Non-nullable): 브랜드 이름
- **`createdAt`** (Non-nullable, Updatable = false): 브랜드 생성 시각
- **`updatedAt`** (Non-nullable): 브랜드 수정 시각
- **`products`**: `Product` 테이블과 **1:N** 관계 (하나의 브랜드는 여러 상품을 가질 수 있음)

### `Category` - [Category.kt](src/main/kotlin/com/shoptest/domain/category/Category.kt)
- **`id`** (Primary Key): 카테고리 ID
- **`type`** (Enum, Unique, Non-nullable): 카테고리 타입
- **`createdAt`** (Non-nullable, Updatable = false): 카테고리 생성 시각
- **`updatedAt`** (Non-nullable): 카테고리 수정 시각
- **`products`**: `Product` 테이블과 **1:N** 관계 (하나의 카테고리는 여러 상품을 가질 수 있음)

### `Product` - [Product.kt](src/main/kotlin/com/shoptest/domain/product/Product.kt)
- **`id`** (Primary Key): 상품 ID
- **`name`** (Non-nullable): 상품 이름
- **`brand_id`** (Foreign Key): `Brand` 테이블의 `id` (외래 키)
- **`category_id`** (Foreign Key): `Category` 테이블의 `id` (외래 키)
- **`price`** (Non-nullable): 상품 가격
- **`createdAt`** (Non-nullable, Updatable = false): 상품 생성 시각
- **`updatedAt`** (Non-nullable): 상품 수정 시각


- 인덱스
  - **`Product` 테이블**의 `brand_id`로 인덱스 추가
    - `Index(name = "idx_product_brand_id", columnList = "brand_id")`
  - **`Product` 테이블**의 `category_id`와 `price`로 복합 인덱스 추가
    - `Index(name = "idx_product_category_id_and_price", columnList = "category_id,price")`
    - price가 카디널리티가 높을 것 같지만, 실제 성능테스트를 진행해봐야 확실할 것으로 보임 (데이터가 많은 경우)
    - 특정 카테고리의 가격 조건으로 조회용
  - **`Product` 테이블**의 `brand_id`와 `category_id`로 복합 인덱스 추가
    - `Index(name = "idx_product_brand_category", columnList = "brand_id,category_id")`
    - 특정 브랜드의 특정 카테고리 조회용
  - **`Product` 테이블**의 `name`으로 인덱스 추가
    - `Index(name = "idx_product_name", columnList = "name")`


### 테이블 관계
- `Product`는 `Brand`와 `Category`와 **N:1** 관계입니다.
  - 하나의 상품은 하나의 브랜드와 하나의 카테고리에 속합니다.
  - 하나의 브랜드는 여러 상품을 가질 수 있으며, 하나의 카테고리도 여러 상품을 가질 수 있습니다.
- `Brand`와 `Category`는 **1:N** 관계입니다.
  - 하나의 브랜드는 여러 상품을 가질 수 있고, 하나의 카테고리도 여러 상품을 가질 수 있습니다.



## API 로직 설명
각 구간에 캐싱관련 로직은 제외 했습니다. 1,2,3번 API의 경우 캐싱하여 일정시간동안 제공해주면 좀 더 효율적일 것으로 보입니다.
각 Service, Controller 함수의 유닛테스트를 작성하고, Controller 함수의 통합 테스트 작성을 했습니다.

API Response Wrapper를 통해 응답 구조의 일관성을 만들어서 클라이언트에서 파싱등의 과정을 단순하게 만들 수 있지만 
우선은 요청, 응답에 대한 결과만 확인하면 되는 구조라고 생각하고 생략했습니다.

```shell
# e.g.
{
  "code": "SUCCESS",
  "message": "요청이 성공적으로 처리되었습니다.",
  "data": {
    ...
  }
}
```


**서버포트에 맞게 curl 명령어에 기입된 포트번호를 설정해주세요.** 

### API Spec
- `http://localhost:{port}/swagger-ui/index.html`
  - e.g.) http://localhost:8080/swagger-ui/index.html


#### - 1. 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
- 관련 DTO
  - [CheapestPriceByCategoryResponse.kt](src/main/kotlin/com/shoptest/api/price/dto/CheapestPriceByCategoryResponse.kt)
- 로직 설명
  - CategoryType.entries를 순회하며 각 카테고리에 대해 price의 최솟값을 구하는 서브쿼리를 작성하고, 해당 카테고리에서 해당 최저가를 가진 상품을 하나만 조회합니다
  - 너무 많은 row를 조회하여 서버에서 처리하기에는 부담이 되기에 쿼리로 조건에 맞게 조회 후 정렬 및 중복가격 필터링은 Service Layer에서 진행합니다.
  - 응답 형식이 하나의 브랜드만 제공하기에, 각 카테고리별로 단일 상품가격만 제공합니다.
  - products table에 category_id와 price에 복합 인덱스를 추가하여 조회속도 향상
- 관련 파일
  - [PriceController.kt](src/main/kotlin/com/shoptest/api/price/controller/PriceController.kt)
  - [PriceService.kt](src/main/kotlin/com/shoptest/api/price/service/PriceService.kt)
  - [ProductQueryRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepository.kt)
  - [ProductQueryRepositoryImpl.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepositoryImpl.kt)
  - [ProductRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductRepository.kt)

---

#### - 2.단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
- 관련 DTO
  - [CheapestTotalPriceByBrandResponse.kt](src/main/kotlin/com/shoptest/api/price/dto/CheapestTotalPriceByBrandResponse.kt)
- 로직 설명
  - 상품을 보유한 카테고리 수가 전체 카테고리 수와 동일한 브랜드 ID만 추출하여, 각 브랜드 ID에 대해 카테고리별 MIN(price) 상품 가격을 한 번에 가져옵니다. 
  - 브랜드별로 카테고리별 가격들을 묶고, 총합 기준 MIN(price)인 브랜드를 선별합니다.
- 관련 파일
  - [PriceController.kt](src/main/kotlin/com/shoptest/api/price/controller/PriceController.kt)
  - [PriceService.kt](src/main/kotlin/com/shoptest/api/price/service/PriceService.kt)
  - [ProductQueryRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepository.kt)
  - [ProductQueryRepositoryImpl.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepositoryImpl.kt)
  - [ProductRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductRepository.kt)

---

#### - 3.카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
- 관련 DTO
  - [MaxMinPriceByCategoryResponse.kt](src/main/kotlin/com/shoptest/api/price/dto/MaxMinPriceByCategoryResponse.kt)
- 로직 설명
  - 해당 카테고리에서 MAX(price) 값을 먼저 구하고 해당 카테고리에서 MIN(price) 값을 구합니다.
  - 최고가 및 최저가가 각각 같은 서로 다른 브랜드의 카테고리의 상품이 존재할 수 있기에 각각 리스트로 반환합니다.
- 관련 파일
  - [PriceController.kt](src/main/kotlin/com/shoptest/api/price/controller/PriceController.kt)
  - [PriceService.kt](src/main/kotlin/com/shoptest/api/price/service/PriceService.kt)
  - [ProductQueryRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepository.kt)
  - [ProductQueryRepositoryImpl.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductQueryRepositoryImpl.kt)
  - [ProductRepository.kt](src/main/kotlin/com/shoptest/domain/product/repository/ProductRepository.kt)

---

#### - 4. 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API
- 브랜드 관련 DTO
  - [BrandCreateRequest.kt](src/main/kotlin/com/shoptest/api/brand/dto/BrandCreateRequest.kt)
  - [BrandResponse.kt](src/main/kotlin/com/shoptest/api/brand/dto/BrandResponse.kt)
  - [BrandUpdateRequest.kt](src/main/kotlin/com/shoptest/api/brand/dto/BrandUpdateRequest.kt)
- 로직설명
  - 브랜드 관련 CUD 로직 구현


- 상품 관련 DTO
  - [ProductCreateRequest.kt](src/main/kotlin/com/shoptest/api/product/dto/ProductCreateRequest.kt)
  - [ProductResponse.kt](src/main/kotlin/com/shoptest/api/product/dto/ProductResponse.kt)
  - [ProductUpdateRequest.kt](src/main/kotlin/com/shoptest/api/product/dto/ProductUpdateRequest.kt)
- 로직설명
  - 상품 관련 CUD 로직 구현

# 4. 테스트 방법

### 통합테스트
```shell
./gradlew test
```

### API 호출 테스트
curl을 사용하여 호출 테스트를 진행합니다.

jq가 설치된 상태라면 파이프라인으로 `| jq`를 붙여서 가독성을 높여주세요

#### 1. 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
```shell
curl -X GET http://localhost:8080/api/v1/price/cheapest
```

#### 2. 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
```shell
curl -X GET http://localhost:8080/api/v1/price/cheapest-brand
```

#### 3. 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
```shell
# 혹시 해당 명렁어가 정상동작하지 않는 경우 인코딩 이슈일 수 있습니다.
# 그런경우, 바로 아래의 브라우저에서 아래의 url로 호출해주세요.
curl -G http://localhost:8080/api/v1/price/max-min \
     --data-urlencode "category=상의"

# 브라우저에서 아래 url로 호출
http://localhost:8080/api/v1/price/max-min?category=%EC%83%81%EC%9D%98
```

#### 4. 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API

브랜드 생성
```shell
curl -X POST http://localhost:8080/api/v1/brand \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Musinsa"
         }'
```

브랜드 수정
```shell
curl -X PUT http://localhost:8080/api/v1/brand/1 \
     -H "Content-Type: application/json" \
     -d '{
           "name": "UpdatedBrandName"
         }'
```

브랜드 삭제
```shell
curl -X DELETE http://localhost:8080/api/v1/brand/2
```

브랜드 조회
```shell
curl -X GET http://localhost:8080/api/v1/brand/1
```

---
상품생성
```shell
curl -X POST http://localhost:8080/api/v1/products \
     -H "Content-Type: application/json" \
     -d '{
           "name": "T-Shirt",
           "price": 19900,
           "brandId": 1,
           "categoryId": 1
         }'
```

상품 수정
```shell
curl -X PUT http://localhost:8080/api/v1/products/1 \
     -H "Content-Type: application/json" \
     -d '{
           "name": "Updated Product Name",
           "price": 24900,
           "brandId": 1,
           "categoryId": 1
         }'
```

상품삭제
```shell
curl -X DELETE http://localhost:8080/api/v1/products/1
```
