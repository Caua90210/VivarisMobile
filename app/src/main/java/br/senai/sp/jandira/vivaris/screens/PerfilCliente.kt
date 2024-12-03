package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.ClienteResponse
import br.senai.sp.jandira.vivaris.model.ClienteResponsebyID
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PerfilCliente(controleDeNavegacao: NavHostController, id: Int) {
    var cliente by remember { mutableStateOf<Cliente?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)
    val clienteService = retrofitFactory.getClienteService()

    LaunchedEffect(id) {
        val token = TokenRepository(context).getToken()
        if (token != null) {
            clienteService.getClienteById(id, token).enqueue(object : Callback<ClienteResponsebyID> {
                override fun onResponse(call: Call<ClienteResponsebyID>, response: Response<ClienteResponsebyID>) {
                    if (response.isSuccessful) {
                        cliente = response.body()?.data
                    } else {
                        Log.e("PerfilCliente", "Erro ao buscar cliente: ${response.code()}")
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<ClienteResponsebyID>, t: Throwable) {
                    Log.e("PerfilCliente", "Falha na chamada: ${t.message}")
                    isLoading = false
                }
            })
        } else {
            isLoading = false
            Log.e("PerfilCliente", "Token não encontrado")
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        cliente?.let { clienteData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Perfil do Cliente",
                )

                Box(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.Start
                    ) {

                        Text(
                            text = "Nome:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                            )

                        Text(text = clienteData.nome ?: "N/A",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Email
                        Text(
                            text = "Email:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = clienteData.email ?: "N/A",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Telefone
                        Text(
                            text = "Telefone:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = clienteData.telefone ?: "N/A",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Instagram
                        Text(
                            text = "Instagram:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        Text(
                            text = clienteData.link_instagram ?: "Não informado",
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }
                }
            }
        } ?: run {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cliente não encontrado.",
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


