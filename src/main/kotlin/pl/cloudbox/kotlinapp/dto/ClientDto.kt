package pl.cloudbox.kotlinapp.dto

import pl.cloudbox.kotlinapp.repository.model.ClientEntity
import java.math.BigDecimal
import java.math.RoundingMode

class ClientDto(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val balance: BigDecimal,
    val currency: String,
) {
    constructor(clientEntity: ClientEntity, currencyRate: BigDecimal, currencyCode: String) : this(
        clientEntity.id,
        clientEntity.firstName,
        clientEntity.lastName,
        clientEntity.balanceInPln.divide(currencyRate, 2 , RoundingMode.HALF_UP),
        currencyCode
    )
}
