package com.example.cloudspendtracker.presentation

import com.example.cloudspendtracker.data.local.CloudSpendEntity

sealed interface CloudSpendUiState {
    data object Loading : CloudSpendUiState
    data class Success(val data: CloudSpendEntity) : CloudSpendUiState
    data class Error(val message: String) : CloudSpendUiState
}