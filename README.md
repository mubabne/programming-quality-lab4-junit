# Лаборатори №4: Нэгжийн тестийн эхлэл — JUnit 5

Ч. Мөнхбаяр — B242270045
- Хичээл: F.CSA313 — Программ хангамжийн чанарын баталгаа ба тест
- JUnit: `5.10.2`, maven-surefire-plugin: `3.2.5`

## Хувилбарууд

```text
$ java -version
openjdk version "21.0.11" 2026-04-21
OpenJDK Runtime Environment (build 21.0.11+10-1-24.04.2-Ubuntu)
OpenJDK 64-Bit Server VM (build 21.0.11+10-1-24.04.2-Ubuntu, mixed mode, sharing)
```

```text
$ mvn -version
Apache Maven 3.9.11 (3e54c93a704957b63ee3494413a2b544fd3d825b)
Maven home: /opt/maven
Java version: 21.0.11, vendor: Ubuntu, runtime: /usr/lib/jvm/java-21-openjdk-amd64
Default locale: en_US, platform encoding: UTF-8
OS name: "linux", version: "6.18.44-fc-v64", arch: "amd64", family: "unix"
```

## Төслийн бүтэц

```text
pom.xml
src/main/java/mn/edu/must/sqat/GradeCalculator.java
src/test/java/mn/edu/must/sqat/GradeCalculatorTest.java
results/mvn-test.txt
results/mvn-test-mutant.txt
```

`GradeCalculator` нь хичээлийн үнэлгээний бүтцээр ажиллана:

- `letterGrade(score)` — 90+ → A, 80–89 → B, 70–79 → C, 60–69 → D, 60-аас доош → F. Оноо 0–100 хүрээнээс гарах эсвэл `NaN` бол `IllegalArgumentException` шиднэ.
- `totalScore(att, lab, quiz1, quiz2, exam)` — ирц (10), лаб ба бие даалт (40), сорил 1 (10), сорил 2 (10), шалгалт (30)-ын нийлбэр. Аль нэг нь сөрөг эсвэл дээд хязгаараасаа хэтэрвэл `IllegalArgumentException` шиднэ.

## Тест ажиллуулах

```bash
mvn test 2>&1 | tee results/mvn-test.txt
```

`target/` нь `.gitignore`-д орсон тул шалгахдаа зөвхөн `results/` доторх гаралтыг харна.

## Тестүүд

`GradeCalculatorTest` дотор нийт **14 тестийн метод** байна: 10 нь `@Test`, 4 нь `@ParameterizedTest`. Бүгд Arrange–Act–Assert бүтэцтэй, `@DisplayName`-ээр монгол нэртэй.

| Метод | Төрөл | Юуг шалгадаг |
|---|---|---|
| `yesonAravA` | `@Test` | 90 → A (хязгаар) |
| `aGiinDoorB` | `@Test` | 89.99 → B (хязгаар) |
| `yerdiinUtguud` | `@Test` | 95, 85, 75, 65, 30 → A, B, C, D, F (`assertAll`) |
| `tentsehHyazgaar` | `@Test` | 60 → D, 59.99 → F |
| `huree0ba100` | `@Test` | 0 → F, 100 → A |
| `sorogOnoo` | `@Test` | -1 → `assertThrows` + алдааны мессеж |
| `hetersenOnoo` | `@Test` | 101 → `assertThrows` |
| `niilberZuv` | `@Test` | 10 + 40 + 10 + 10 + 30 = 100 |
| `irtsSorog` | `@Test` | att = -5 → `assertThrows` |
| `labHetersen` | `@Test` | lab = 41 → `assertThrows` + алдааны мессеж |
| `letterGradeHyazgaaruud` | `@ParameterizedTest` + `@CsvSource` | 11 хязгаарын утга (95, 90, 89.99, 80, 79.99, 70, 69.99, 60, 59.99, 0, 100) |
| `letterGradeBuruuOrolt` | `@ParameterizedTest` + `@ValueSource` | -1, -0.01, 100.01, 101, NaN |
| `totalScoreZuvNiilber` | `@ParameterizedTest` + `@CsvSource` | 5 хүчинтэй хослол, бутархай оноо орсон |
| `totalScoreBuruuOrolt` | `@ParameterizedTest` + `@CsvSource` | 5 хэсэг тус бүрийн сөрөг ба хэтэрсэн утга (10 мөр) |

Surefire нь `@CsvSource`/`@ValueSource`-ийн мөр бүрийг тусдаа тест гэж тоолдог. Тиймээс 14 метод байхад [results/mvn-test.txt](results/mvn-test.txt)-ийн сүүлийн мөр:

```text
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

Тооцоо: 10 `@Test` + 11 + 5 + 5 + 10 = **41**.

## Мутаци (санаатай унагаах)

`letterGrade` доторх `score >= 90` нөхцөлийг `score > 90` болгож дахин ажиллуулсан. Гаралт: [results/mvn-test-mutant.txt](results/mvn-test-mutant.txt).

```text
Tests run: 41, Failures: 2, Errors: 0, Skipped: 0
BUILD FAILURE
```

| Унасан тест | Мэдээлэл |
|---|---|
| `yesonAravA` — «90 оноо яг A дүн байх ёстой» | `expected: <A> but was: <B>` |
| `letterGradeHyazgaaruud` `[2]` — «90.0 оноо → A» | `expected: <A> but was: <B>` |

Үүний дараа `>= 90`-ийг буцааж засаад `mvn test`-ийг дахин ажиллуулахад 41 тест бүгд ногоон болсон.

## Дүгнэлт

Нийт 14 тестийн метод бичсэн бөгөөд `results/mvn-test.txt`-д 41 тест ажиллаж, бүгд амжилттай болсон. Мутацийн үед `>= 90`-ийг `> 90` болгоход яг 90 оноог шалгадаг хоёр тест л унасан, бусад 39 тест нь ногоон хэвээр үлдсэн. Энэ нь хязгаарын утгыг тусгайлан шалгаагүй бол ийм жижиг алдааг огт илрүүлэхгүй гэдгийг харуулсан. Хамгийн сонирхолтой нь `totalScoreBuruuOrolt` тест байсан, учир нь заавар зөвхөн att = -5, lab = 41 гэсэн жишээ өгсөн ч таван хэсэг тус бүрийг тусад нь сөрөг ба хэтэрсэн утгаар шалгасан. Ингэснээр нэг хэсгийн шалгалтыг санамсаргүй орхивол яг аль хэсэг дээр алдсан нь шууд харагдана. Мөн `NaN`-ийг тусгайлан шалгахгүй бол `NaN < 0` ч, `NaN > 100` ч худал тул `letterGrade` нь exception шидэхгүйгээр F буцаана, тиймээс `Double.isNaN` шалгалт нэмж, `letterGradeBuruuOrolt`-д `NaN`-ийг оруулсан. Pass болсон тест заавал зөв тест биш, харин хязгар дээр унаж чаддаг тест л үнэ цэнэтэй гэдгийг энэ лабаас ойлголоо.
