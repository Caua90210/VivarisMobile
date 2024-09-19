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

@Composable
fun Preferencias(controleDeNavegacao: NavHostController?) {
    var preferenciasList by remember { mutableStateOf(listOf<Preferencias>()) }

    // Simulação de dados (remover quando você integrar o Retrofit)
    preferenciasList = listOf(
        Preferencias(id = 1, nome = "Rick Sanchez", cor = Color.Red.toString()),
        Preferencias(id = 2, nome = "Morty Smith", cor = Color.Blue.toString()),
        Preferencias(id = 3, nome = "Summer Smith", cor = Color.Green.toString())
    )

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
                text = "Preferências",
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
                    CharacterCard(preferencias = preferencias, controleDeNavegacao)
                }
            }
        }
    }
}

@Composable
fun CharacterCard(preferencias: Preferencias?, controleDeNavegacao: NavHostController?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = { controleDeNavegacao?.navigate("cadastro/${preferencias?.id}") }
            )
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB00D0D)),
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
                    color = Color(0xFFFFEB3B)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CharacterCardPreview() {
    val exemploPreferencias = Preferencias(
        id = 1,
        nome = "Rick Sanchez",
        cor = Color.Red.toString()
    )
    CharacterCard(preferencias = exemploPreferencias, controleDeNavegacao = null)
}

@Preview(showSystemUi = true)
@Composable
private fun PreferenciasPreview() {
    // Simulação de uma navegação nula
    Preferencias(controleDeNavegacao = null)
}
