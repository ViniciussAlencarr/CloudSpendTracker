package com.example.cloudspendtracker

import com.example.cloudspendtracker.data.local.CloudSpendEntity
import com.example.cloudspendtracker.domain.GetCloudSpendUseCase
import com.example.cloudspendtracker.presentation.CloudSpendUiState
import com.example.cloudspendtracker.presentation.CloudSpendViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CloudSpendViewModelTest {

    // Criamos um "dublê" do UseCase para não batermos na AWS de verdade durante o teste
    private val useCase = mockk<GetCloudSpendUseCase>(relaxed = true)
    private lateinit var viewModel: CloudSpendViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Troca a thread principal do Android por uma thread de teste controlada
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Quando o banco tem dados, o estado deve mudar para Success`() = runTest {
        // 1. PREPARAÇÃO (Given)
        val dadosFicticios = CloudSpendEntity(
            id = 1, month = "Abril", totalSpend = 450.75,
            budgetLimit = 500.00, currency = "USD", services = emptyList()
        )
        // Ensinamos o dublê: "Quando pedirem os dados, retorne nosso fluxo fictício"
        coEvery { useCase.invoke() } returns flowOf(dadosFicticios)

        // 2. AÇÃO (When)
        viewModel = CloudSpendViewModel(useCase)
        testDispatcher.scheduler.advanceUntilIdle() // Roda todas as coroutines pendentes

        // 3. VERIFICAÇÃO (Then)
        val estadoAtual = viewModel.uiState.value
        assertTrue(estadoAtual is CloudSpendUiState.Success)
        assertEquals(450.75, (estadoAtual as CloudSpendUiState.Success).data.totalSpend, 0.0)
    }

    @Test
    fun `Quando a sincronizacao falha e o banco esta vazio, o estado deve ser Error`() = runTest {
        // 1. PREPARAÇÃO
        coEvery { useCase.invoke() } returns flowOf(null) // Banco vazio
        coEvery { useCase.syncData() } throws Exception("Sem internet") // Falha na rede

        // 2. AÇÃO
        viewModel = CloudSpendViewModel(useCase)
        testDispatcher.scheduler.advanceUntilIdle()

        // 3. VERIFICAÇÃO
        val estadoAtual = viewModel.uiState.value
        assertTrue(estadoAtual is CloudSpendUiState.Error)
        assertEquals("Falha ao buscar dados.", (estadoAtual as CloudSpendUiState.Error).message)
    }
}