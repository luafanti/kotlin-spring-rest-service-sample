package pl.cloudbox.kotlinapp.endpoint

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import pl.cloudbox.kotlinapp.dto.ClientDto
import pl.cloudbox.kotlinapp.service.ClientService

@RestController
class ClientEndpoint @Autowired constructor(
    private val clientService: ClientService
) {

    @GetMapping(value = ["/clients"])
    fun getAllClients(): ResponseEntity<List<ClientDto>> =
        ResponseEntity.ok(clientService.getAllClients())

    @GetMapping(value = ["/clients/{clientId}"])
    fun getClientById(
        @PathVariable("clientId") clientId: Long
    ): ResponseEntity<ClientDto> = ResponseEntity.ok(clientService.getClientById(clientId))
}