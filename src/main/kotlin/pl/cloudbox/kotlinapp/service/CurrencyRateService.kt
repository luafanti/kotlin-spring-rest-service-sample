package pl.cloudbox.kotlinapp.service

import java.math.BigDecimal

interface CurrencyRateService {

    fun getForeignRate(currency: String): BigDecimal
}