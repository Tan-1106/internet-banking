package com.example.internetbanking.data

import java.math.BigDecimal

data class OfficerUiState(
    val officer: User = User(),
    val profitableRates: BigDecimal = BigDecimal.ZERO,
    val customerToEdit: Customer = Customer()
)