package com.example.cloudspendtracker.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cloudspendtracker.data.local.CloudSpendEntity

@Composable
fun CloudSpendScreen(viewModel: CloudSpendViewModel) {
    // Escuta o fluxo de estado da ViewModel
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(title = { Text("FinOps Dashboard") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()) {
            when (uiState) {
                is CloudSpendUiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                is CloudSpendUiState.Error -> {
                    Text(
                        text = (uiState as CloudSpendUiState.Error).message,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is CloudSpendUiState.Success -> {
                    val data = (uiState as CloudSpendUiState.Success).data
                    DashboardContent(data)
                }
            }
        }
    }
}

@Composable
fun DashboardContent(data: CloudSpendEntity) {
    Column(modifier = Modifier.padding(16.dp)) {
        // Card de Gasto Total
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Gastos de ${data.month}", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${data.currency} ${data.totalSpend}",
                    style = MaterialTheme.typography.headlineLarge,
                    color = if (data.totalSpend > data.budgetLimit) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Limite: ${data.budgetLimit}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Por Serviço", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        // Lista de Serviços
        LazyColumn {
            items(data.services) { service ->
                ListItem(
                    headlineContent = { Text(service.name) },
                    trailingContent = { Text("${data.currency} ${service.cost}") }
                )
                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
            }
        }
    }
}