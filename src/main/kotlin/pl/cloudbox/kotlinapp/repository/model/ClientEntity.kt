package pl.cloudbox.kotlinapp.repository.model

import java.math.BigDecimal
import javax.persistence.*

@Entity
@Table(name = "client")
class ClientEntity(
    @Id val id: Long = 0,
    val firstName: String = "",
    val lastName: String = "",
    val balanceInPln: BigDecimal = BigDecimal.ZERO
)