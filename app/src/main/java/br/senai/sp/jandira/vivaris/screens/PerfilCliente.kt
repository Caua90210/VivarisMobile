package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Cliente
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


                Spacer(modifier = Modifier.height(40.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    verticalAlignment = Alignment.CenterVertically ,
                    horizontalArrangement = Arrangement.SpaceBetween
                    // Centraliza os itens verticalmente
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Voltar",
                        modifier = Modifier
                            .size(32.dp)
                            .clickable {
                                controleDeNavegacao.popBackStack()
                            },
                        tint = Color(0xFF52B693) // Verde
                    )

                    Text(
                        text = "Seu perfil",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF15A27A),
                        modifier = Modifier
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                            .padding(end = 40.dp)
                        ,
                        textAlign = TextAlign.Center,
                        fontSize = 32.sp
                    )
                }


                Spacer(modifier = Modifier.height(40.dp))
                // Card for Name
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(2.dp, Color(0xFF15A27A), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Nome:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15A27A)
                        )
                        Text(text = clienteData.nome ?: "N/A")
                    }
                }

                // Card for Email
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(2.dp, Color(0xFF15A27A), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Email:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15A27A)
                        )
                        Text(text = clienteData.email ?: "N/A")
                    }
                }

                // Card for Telefone
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(2.dp, Color(0xFF15A27A), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Telefone:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15A27A)
                        )
                        Text(text = clienteData.telefone ?: "N/A")
                    }
                }

                // Card for Instagram
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .border(2.dp, Color(0xFF15A27A), RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Instagram:",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF15A27A)
                        )
                        Text(text = clienteData.link_instagram ?: "Não informado")
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
                    textAlign = TextAlign.Center,
                    color = Color.Red
                )
            }
        }
    }
}
