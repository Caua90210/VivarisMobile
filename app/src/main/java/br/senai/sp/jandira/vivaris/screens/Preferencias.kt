package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Preferencias
import br.senai.sp.jandira.vivaris.model.PreferenciasResponse
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PreferenciasScreen(controleDeNavegacao: NavHostController?) {
    var preferenciasList by remember { mutableStateOf(emptyList<Preferencias>()) }
    val scope = rememberCoroutineScope()
    val retrofitFactory = RetrofitFactory()
    val preferenciasService = retrofitFactory.getPreferenciasService()
    val context = LocalContext.current
    var loadingPreferencias by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        preferenciasService.getAllPreferencias().enqueue(object : Callback<PreferenciasResponse> {
            override fun onResponse(call: Call<PreferenciasResponse>, response: Response<PreferenciasResponse>) {
                if (response.isSuccessful) {
                    preferenciasList = response.body()?.data ?: emptyList()
                    Log.d("Preferencias", "Preferências carregadas: $preferenciasList")
                } else {
                    Toast.makeText(context, "Erro ao carregar preferências: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("Erro", "Corpo da resposta: ${response.errorBody()?.string()}")
                }
                loadingPreferencias = false
            }

            override fun onFailure(call: Call<PreferenciasResponse>, t: Throwable) {
                Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                loadingPreferencias = false
            }
        })
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

fun String.toColor(): Color {
    return Color(android.graphics.Color.parseColor(this))
}

@Composable
fun PreferenciasCard(preferencias: Preferencias?, controleDeNavegacao: NavHostController?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { controleDeNavegacao?.navigate("cadastro/${preferencias?.id}") })
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00796B)), // Cor padrão
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            Card(
                modifier = Modifier.size(100.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = (preferencias?.cor ?: "#808080").toColor()) // Convertendo string para cor
            ) {
               
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

@Preview(showSystemUi = true)
@Composable
private fun PreferenciasScreenPreview() {
    PreferenciasScreen(controleDeNavegacao = null)
}
