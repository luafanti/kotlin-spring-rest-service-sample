package pl.cloudbox.kotlinapp.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.client.ExpectedCount
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers.method
import org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo
import org.springframework.test.web.client.response.MockRestResponseCreators.withStatus
import org.springframework.web.client.RestTemplate
import pl.cloudbox.kotlinapp.config.RestTemplateClient
import pl.cloudbox.kotlinapp.service.exception.IntegrationException
import java.math.BigDecimal
import java.net.URI


@RunWith(SpringRunner::class)
@ContextConfiguration(classes = [RestTemplateClient::class, NbpCurrencyRateService::class])
class NbpCurrencyRateServiceTest {

    @Autowired
    lateinit var restTemplate: RestTemplate

    @Autowired
    lateinit var currencyRateService: CurrencyRateService

    private lateinit var mockServer: MockRestServiceServer
    private val mapper = ObjectMapper()

    @Before
    fun setUp() {
        ReflectionTestUtils.setField(currencyRateService, "nbpApiUrl", "http://api.nbp.pl/api/exchangerates/rates/A/")
        mockServer = MockRestServiceServer.createServer(restTemplate)
    }

    @Test
    fun `Should return currency rate if NBP API response with success`() {
        val exchange = Exchange("A", "USD", listOf(Rate("2020-02-25", BigDecimal(3.7))))
        mockServer.expect(
            ExpectedCount.once(),
            requestTo(URI("http://api.nbp.pl/api/exchangerates/rates/A/USD"))
        )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(mapper.writeValueAsString(exchange))
            )

        val currencyRate = currencyRateService.getForeignRate("USD")
        mockServer.verify()
        Assertions.assertThat(currencyRate).isEqualTo(BigDecimal(3.7))
    }

    @Test(expected = IntegrationException::class)
    fun `Should throw IntegrationException if currency not available in response from NBP API`() {
        mockServer.expect(
            ExpectedCount.once(),
            requestTo(URI("http://api.nbp.pl/api/exchangerates/rates/A/USD"))
        )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_JSON)
            )

        currencyRateService.getForeignRate("USD")
        mockServer.verify()
    }

    @Test(expected = IntegrationException::class)
    fun `Should throw IntegrationException if NBP API return non success HTTP code`() {
        mockServer.expect(
            ExpectedCount.once(),
            requestTo(URI("http://api.nbp.pl/api/exchangerates/rates/A/USD"))
        )
            .andExpect(method(HttpMethod.GET))
            .andRespond(
                withStatus(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
            )

        currencyRateService.getForeignRate("USD")
        mockServer.verify()
    }
}