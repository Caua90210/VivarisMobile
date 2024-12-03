package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import br.senai.sp.jandira.vivaris.service.PagamentoService


@Composable
fun PagamentoScreen(sessionId: String, pagamentoService: PagamentoService) {
    var status by remember { mutableStateOf("Carregando...") }
    var errorMessage by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(sessionId) {
        try {
            val response = pagamentoService.getPaymentStatus(sessionId)
            status = when (response.status) {
                "paid" -> "Pagamento Concluído"
                "unpaid" -> "Pagamento Não Realizado"
                else -> "Status: ${response.status}"
            }
        } catch (e: Exception) {
            errorMessage = "Erro ao obter status: ${e.message}"
            Log.e("PaymentStatus", "Erro ao buscar status", e)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, style = MaterialTheme.typography.bodyMedium)
        } else {
            Text(
                text = status,
                style = MaterialTheme.typography.titleLarge,
                color = if (status == "Pagamento Concluído") Color.Green else Color.Gray
            )
        }
    }
}

