package com.example.internetbanking.security

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity

class BiometricAuthenticator(
    val context: Context
) {
    private lateinit var promptInfor: BiometricPrompt.PromptInfo
    private val biometricManager = BiometricManager.from(context)
    private lateinit var biometricPrompt: BiometricPrompt
    fun isBiometricAuthAvilable(): BiometricAuthenticationStatus {
        return when (biometricManager.canAuthenticate(BIOMETRIC_STRONG)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAuthenticationStatus.READY
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> BiometricAuthenticationStatus.NOT_AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAuthenticationStatus.TEMPORARY_NOTAVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED
            else -> BiometricAuthenticationStatus.NOT_AVAILABLE

        }
    }

    fun promptBiometricAuth(
        title: String,
        subtitle: String,
        negativeButtonText: String,
        fragmentActivity: FragmentActivity,
        onSucess: (result: BiometricPrompt.AuthenticationResult) -> Unit,
        onFailed: () -> Unit,
        onError: (errorCode: Int, errorString: String) -> Unit
    ) {
        when (isBiometricAuthAvilable()) {
            BiometricAuthenticationStatus.NOT_AVAILABLE -> {
                onError(
                    BiometricAuthenticationStatus.NOT_AVAILABLE.id,
                    "Not available for this device"
                )
                return
            }

            BiometricAuthenticationStatus.TEMPORARY_NOTAVAILABLE -> {
                onError(
                    BiometricAuthenticationStatus.TEMPORARY_NOTAVAILABLE.id,
                    "Temporary not available"
                )
                return
            }

            BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED -> {
                onError(
                    BiometricAuthenticationStatus.AVAILABLE_BUT_NOT_ENROLLED.id,
                    "Available but not enrolled"
                )
                return
            }

            else -> Unit
        }
        biometricPrompt = BiometricPrompt(
            fragmentActivity,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSucess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    onError(errorCode, errString.toString())
                }


                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    onFailed()
                }
            }
        )
        promptInfor = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setNegativeButtonText(negativeButtonText)
//            .setConfirmationRequired(true)
//            .setAllowedAuthenticators(BIOMETRIC_STRONG)
            .build()
        biometricPrompt.authenticate(promptInfor)
    }
}