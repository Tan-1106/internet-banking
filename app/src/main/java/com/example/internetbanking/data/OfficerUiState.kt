package com.example.internetbanking.data

import java.math.BigDecimal

data class OfficerUiState(
    val officer: User = User(),
    val profitableRates: BigDecimal = BigDecimal.ZERO,
    val ratesChangeTimestamp: Long = 0L,
    val customerToEdit: Customer = Customer()
)