package pl.cloudbox.kotlinapp.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.cloudbox.kotlinapp.dto.ClientDto
import pl.cloudbox.kotlinapp.repository.ClientRepository
import pl.cloudbox.kotlinapp.service.exception.ClientNotFoundException


@Service
class ClientService @Autowired constructor(
    private val clientRepository: ClientRepository,
    private val currencyRateService: CurrencyRateService
) {

    @Value("\${currency-service.foreign-currency}")
    private val foreignCurrency: String = ""

    fun getAllClients(): List<ClientDto> {
        val clients = clientRepository.findAll()
        val currencyRate = currencyRateService.getForeignRate(foreignCurrency)
        val clientsDto = ArrayList<ClientDto>()
        clients.forEach { clientEntity ->
            clientsDto.add(ClientDto(clientEntity, currencyRate, foreignCurrency))
        }
        return clientsDto
    }

    fun getClientById(id: Long): ClientDto {
        val client = clientRepository.findById(id).orElseThrow { ClientNotFoundException("Not found client with id $id") }
        val currencyRate = currencyRateService.getForeignRate(foreignCurrency)
        return ClientDto(client, currencyRate, foreignCurrency)
    }
}