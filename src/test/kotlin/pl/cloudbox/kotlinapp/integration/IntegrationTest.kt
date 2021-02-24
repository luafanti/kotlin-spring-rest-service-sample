package pl.cloudbox.kotlinapp.integration

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.TestPropertySource

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = ["classpath:application-test.yml"])
class IntegrationTest(@Autowired val restTemplate: TestRestTemplate) {

	@Test
	fun `Should return 200 at all clients path`() {
		assertThat(restTemplate.getForEntity("/clients", Any::class.java).statusCode).isEqualTo(HttpStatus.OK)
	}

	@Test
	fun `Should return 200 at client with id path`() {
		assertThat(restTemplate.getForEntity("/clients/1", String::class.java).statusCode).isEqualTo(HttpStatus.OK)
		assertThat(restTemplate.getForEntity("/clients/3", String::class.java).statusCode).isEqualTo(HttpStatus.OK)
	}

	@Test
	fun `Should return 404 when client not exists`() {
		assertThat(restTemplate.getForEntity("/clients/99", String::class.java).statusCode).isEqualTo(HttpStatus.NOT_FOUND)
	}

	@Test
	fun `Should return 400 when client id is not a number`() {
		assertThat(restTemplate.getForEntity("/clients/foo", String::class.java).statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
	}
}
