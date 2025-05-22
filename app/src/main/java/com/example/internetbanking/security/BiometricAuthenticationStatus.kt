package com.example.internetbanking.security

enum class BiometricAuthenticationStatus(val id: Int) {
    READY(1),
    NOT_AVAILABLE(2),
    TEMPORARY_NOTAVAILABLE(3),
    AVAILABLE_BUT_NOT_ENROLLED(4),

}