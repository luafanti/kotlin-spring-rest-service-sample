package pl.cloudbox.kotlinapp.service

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import pl.cloudbox.kotlinapp.service.exception.IntegrationException
import java.math.BigDecimal

@Service
class NbpCurrencyRateService @Autowired constructor(
    private val restTemplate: RestTemplate
) : CurrencyRateService {

    @Value("\${currency-service.nbp.url}")
    private val nbpApiUrl: String = ""

    @Retryable(
        value = [IntegrationException::class],
        maxAttemptsExpression = "\${currency-service.nbp.retry.max-attempts:2}",
        backoff = Backoff(delayExpression = "\${currency-service.nbp.retry.delay:1000}"))
    override fun getForeignRate(currency: String): BigDecimal {
        val response: ResponseEntity<Exchange>
        try {
            response = restTemplate.getForEntity(nbpApiUrl + currency, Exchange::class.java)
        } catch (exc: Exception) {
            throw IntegrationException("Problem while calling NBP API. Exception message: ${exc.message}")
        }
        return response.body?.rates?.get(0)?.mid ?: throw IntegrationException("$currency rate unavailable in response")
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Exchange(
    val table: String,
    val currency: String,
    val rates: List<Rate>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Rate(
    val effectiveDate: String,
    val mid: BigDecimal
)