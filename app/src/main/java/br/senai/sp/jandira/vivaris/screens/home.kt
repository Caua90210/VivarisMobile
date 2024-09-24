package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home(controleDeNavegacao: NavHostController, userId: Int) {
    val nomeUsuario = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(true) }
    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()

    // Fetch user data
    LaunchedEffect(userId) {
        clienteService.getClienteById(userId).enqueue(object : Callback<Cliente> {
            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                if (response.isSuccessful) {
                    nomeUsuario.value = response.body()?.nome ?: "Nome não encontrado"
                } else {
                    Log.e("Home", "Erro ao buscar usuário: ${response.code()}")
                }
                loading.value = false
            }

            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                Log.e("Home", "Falha na chamada: ${t.message}")
                loading.value = false
            }
        })
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (loading.value) {
            Text("Carregando...")
        } else {
            Text("Bem-vindo, ${nomeUsuario.value}!", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
