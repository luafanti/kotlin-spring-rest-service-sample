package pl.cloudbox.kotlinapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@EnableRetry
class ClientBalanceApp

fun main(args: Array<String>) {
    runApplication<ClientBalanceApp>(*args)
}
