package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Preferencias
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import kotlinx.coroutines.launch

@Composable
fun PreferenciasScreen(controleDeNavegacao: NavHostController?) {
    var preferenciasList by remember { mutableStateOf(listOf<Preferencias>()) }
    val scope = rememberCoroutineScope()

    // Consumir a API para buscar preferências
    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val call = RetrofitFactory().getPreferenciasService().getAllPreferencias()
                val response = call.execute() // Usar execute() para chamadas síncronas
                if (response.isSuccessful) {
                    preferenciasList = response.body() ?: emptyList()
                } else {
                    println("Erro: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                println("Exceção: ${e.message}")
            }
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFFFFF)),
        color = Color(0xFFFFFFFF)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Escolha suas preferências",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF000000),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(preferenciasList) { preferencias ->
                    PreferenciasCard(preferencias = preferencias, controleDeNavegacao)
                }
            }
        }
    }
}

@Composable
fun PreferenciasCard(preferencias: Preferencias?, controleDeNavegacao: NavHostController?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { controleDeNavegacao?.navigate("cadastro/${preferencias?.id}") }
            )
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00796B)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier.size(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = (preferencias?.cor ?: Color.Gray) as Color)
            ) {
                // Aqui você pode adicionar uma imagem ou outro conteúdo, se necessário
            }
            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = preferencias?.nome ?: "Sem nome",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreferenciasCardPreview() {
    val exemploPreferencias = Preferencias(
        id = 1,
        nome = "Terapia Cognitiva",
        cor = Color.Red.toString()
    )
    PreferenciasCard(preferencias = exemploPreferencias, controleDeNavegacao = null)
}

@Preview(showSystemUi = true)
@Composable
private fun PreferenciasScreenPreview() {
    PreferenciasScreen(controleDeNavegacao = null)
}
