package pl.cloudbox.kotlinapp.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import pl.cloudbox.kotlinapp.repository.model.ClientEntity

@Repository
interface ClientRepository : CrudRepository<ClientEntity, Long>