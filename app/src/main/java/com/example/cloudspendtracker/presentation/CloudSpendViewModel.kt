package com.example.cloudspendtracker.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudspendtracker.domain.GetCloudSpendUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // Avisa o Hilt que esta é uma ViewModel especial
class CloudSpendViewModel @Inject constructor(
    private val getCloudSpendUseCase: GetCloudSpendUseCase
) : ViewModel() {

    // O estado interno (mutável) e o estado público (somente leitura) para o Compose
    private val _uiState = MutableStateFlow<CloudSpendUiState>(CloudSpendUiState.Loading)
    val uiState: StateFlow<CloudSpendUiState> = _uiState.asStateFlow()

    init {
        observeCloudSpend()
        syncWithCloud()
    }

    private fun observeCloudSpend() {
        viewModelScope.launch {
            // Fica "escutando" o banco de dados infinitamente
            getCloudSpendUseCase().collect { entity ->
                if (entity != null) {
                    _uiState.value = CloudSpendUiState.Success(entity)
                }
            }
        }
    }

    private fun syncWithCloud() {
        viewModelScope.launch {
            try {
                getCloudSpendUseCase.syncData()
            } catch (e: Exception) {
                // Se der erro de rede e o banco estiver vazio, mostra erro
                if (_uiState.value is CloudSpendUiState.Loading) {
                    _uiState.value = CloudSpendUiState.Error("Falha ao buscar dados.")
                }
            }
        }
    }
}