# Client balance service
#### REST service returning client with balance in foreign currency 

Execute all tests

`mvn clean test 
`


Start app on default port 8080

`mvn spring-boot:run
`

API resources

```
curl http://localhost:8080/clients
curl http://localhost:8080/clients/1
```


### What has been done and why

- Due to the simplicity, the application has been divided into 3 layers (endpoint, service, dao)
- For `CurrencyRateService` I decided to use the interface because there is a chance that we will get the exchange rate from another source in the future. By using an interface, we will be able to easily use a different implementation
- For `ClientService` I don't feel that we need now or in the future more than one implementation 
- For in-memory database I decided to use H2 database. Reasons why: easy to configure, easy to initialize mock data, good for testing
- For REST client I decided to use RestTemplate. It isn't the most convenient to use, but it is simple enough and doesn't require additional libraries
- `ControllerAdvice` is used for global error handling. IMHO it's convenient solution for CRUDs or micro apps
- REST call to NBP API is wrapped with retry patter which comes from `spring-retry`. Base parameters of retry (attempts, delay) is configured using spring boot properties file
- Couple of tests has been added. `ClientService` was covered with unit test with help of Mockito. In `NbpCurrencyRateServiceTest` there are integration tests with mocked external service
- In `IntegrationTest` we can find some kind of e2e test which has dedicated properties file and also sql initialization script
- As an extra, I decided to add ArchUnit library with few test samples. It's a powerful lib which can validate dependencies between packages, classes and layers using unit tests. 
- By mistake, I started adding tests based on the JUnit4. I did not waste time rewriting tests on JUnit5, so I had to add `junit-vintage-engine` to run tests with JUnit4
- The code has been checked using SonarLint. Unfortunately, the number of rules for Kotlin is quite poor compared to Java

### What could be improved

- Replace blocking `Spring MVC` with reactive `Spring WebFlux`. We will probably not achieve a full non-blocking stack here due to use JPA and sql database. To get full non-blocking stack I guess that we should replace H2 with Mongo or Redis. I see that spring provide this kind of reactive libraries for no-sql databases. https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-data-redis-reactive 
- I am not sure about dealing with balance fields. I have applied one kind of rounding at the DTO level, but maybe it should be done in other way. It's required more research :)
- Use `FeignClient` instead `RestTemplate`. Feign is a declarative and convenient library especially when our application works in microservices ecosystems