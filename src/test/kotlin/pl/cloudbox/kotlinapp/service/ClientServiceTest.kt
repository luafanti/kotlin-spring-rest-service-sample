package pl.cloudbox.kotlinapp.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.util.ReflectionTestUtils
import pl.cloudbox.kotlinapp.dto.ClientDto
import pl.cloudbox.kotlinapp.repository.ClientRepository
import pl.cloudbox.kotlinapp.repository.model.ClientEntity
import pl.cloudbox.kotlinapp.service.exception.ClientNotFoundException
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Optional


@RunWith(MockitoJUnitRunner::class)
class ClientServiceTest {

    @InjectMocks
    lateinit var clientService: ClientService

    @Mock
    lateinit var clientRepository: ClientRepository

    @Mock
    lateinit var currencyRateService: CurrencyRateService

    @Before
    fun setUp() {
        ReflectionTestUtils.setField(clientService, "foreignCurrency", "USD")
    }

    @Test
    fun `Should return two clients with USD balance`() {
        val clientsEntities = listOf(
            ClientEntity(1, "Bob", "Test", BigDecimal(1000)),
            ClientEntity(2, "Tom", "Test", BigDecimal(999.87))
        )
        doReturn(clientsEntities).`when`(clientRepository).findAll()
        doReturn(BigDecimal(3.7)).`when`(currencyRateService).getForeignRate("USD")
        val result = clientService.getAllClients()

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0])
            .usingRecursiveComparison()
            .isEqualTo(
                ClientDto(
                    1,
                    "Bob",
                    "Test",
                    BigDecimal(1000).divide(BigDecimal(3.7), 2, RoundingMode.HALF_UP),
                    "USD"
                )
            )

        assertThat(result[1])
            .usingRecursiveComparison()
            .isEqualTo(
                ClientDto(
                    2,
                    "Tom",
                    "Test",
                    BigDecimal(999.87).divide(BigDecimal(3.7), 2, RoundingMode.HALF_UP),
                    "USD"
                )
            )
    }

    @Test
    fun `Should return single client with USD balance`() {
        val clientEntity = Optional.of(ClientEntity(1, "Bob", "Test", BigDecimal(1000)))
        doReturn(clientEntity).`when`(clientRepository).findById(1)
        doReturn(BigDecimal(3.7)).`when`(currencyRateService).getForeignRate("USD")
        val result = clientService.getClientById(1)

        assertThat(result)
            .usingRecursiveComparison()
            .isEqualTo(
                ClientDto(
                    1,
                    "Bob",
                    "Test",
                    BigDecimal(1000).divide(BigDecimal(3.7), 2, RoundingMode.HALF_UP),
                    "USD"
                )
            )
    }

    @Test(expected = ClientNotFoundException::class)
    fun `Should throw ClientNotFoundException when client not exists`() {
        clientService.getClientById(2)
    }
}