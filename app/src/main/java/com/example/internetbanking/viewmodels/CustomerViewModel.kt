package com.example.internetbanking.viewmodels

import androidx.lifecycle.ViewModel
import com.example.internetbanking.data.CustomerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CustomerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CustomerUiState())
    val uiState: StateFlow<CustomerUiState> = _uiState.asStateFlow()
}