package pl.cloudbox.kotlinapp.endpoint

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import pl.cloudbox.kotlinapp.service.exception.ClientNotFoundException
import pl.cloudbox.kotlinapp.service.exception.IntegrationException

@ControllerAdvice
class RestExceptionHandler : ResponseEntityExceptionHandler() {
    @ExceptionHandler(value = [(ClientNotFoundException::class)])
    fun handleClientNotFoundException(exc: ClientNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exc.message)
    }

    @ExceptionHandler(value = [(IntegrationException::class)])
    fun handleIntegrationException(exc: IntegrationException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exc.message)
    }
}