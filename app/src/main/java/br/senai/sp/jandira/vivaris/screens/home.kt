package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home(controleDeNavegacao: NavHostController, userId: Int, isPsicologo: Boolean, nomeUsuario: String) {
    val loading = remember { mutableStateOf(true) }
    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()

    // Fetch user data apenas se o nome não foi passado
    LaunchedEffect(userId) {
        if (nomeUsuario.isEmpty()) {
            clienteService.getClienteById(userId).enqueue(object : Callback<Cliente> {
                override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                    if (response.isSuccessful) {
                        val nome = response.body()?.nome ?: "Nome não encontrado"
                        Log.d("Home", "Nome do usuário: $nome")
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
        } else {
            Log.d("Home", "Nome do usuário passado: $nomeUsuario")
            loading.value = false
        }
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
            // Bem-vindo
            Text(
                "Bom Dia, $nomeUsuario!",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Consultas do dia
//            Text("Consultas Hoje", style = MaterialTheme.typography.titleLarge)
//            Spacer(modifier = Modifier.height(8.dp))
//            Row(
//                horizontalArrangement = Arrangement.SpaceEvenly,
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                repeat(4) { // Apenas para exemplo, repita 4 vezes para criar consultas fictícias
//                    ConsultaCard("Teste", "10:30")
//                }
//            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isPsicologo) {


                // Botões inferiores
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureButton("Blog", R.drawable.blog)
                    FeatureButton("Criar Grupo", R.drawable.grupos)
                    FeatureButton("Prontuários", R.drawable.prontuario)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureButton("Eventos em Live", R.drawable.live)
                    FeatureButton("Lembrete", R.drawable.bell)
                    FeatureButton("Meus Chats", R.drawable.chat)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Botão para psicólogo
                if (isPsicologo) {
                    Button(
                        onClick = {
                            controleDeNavegacao.navigate("disponibilidade/$userId")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFF3E9C81))

                    ) {
                        Text(text = "Ir para Disponibilidade", fontSize = 16.sp)
                    }
                }
            }else{
                // Botões inferiores
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureButton("Blog", R.drawable.blog)
                    FeatureButton("Gráfico de Humor", R.drawable.graficohumor)
                    FeatureButton("Diário", R.drawable.diario)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FeatureButton("Trilhas de Meditação", R.drawable.meditacao)
                    FeatureButton("Lembrete", R.drawable.bell)
                    FeatureButton("Meus Chats", R.drawable.chat)
                }
            }
        }
    }
}

@Composable
fun FeatureButton(label: String, icon: Int) {
    // Mudar o tamanho dos botões
    Box(
        modifier = Modifier
            .size(100.dp) // Tamanho desejado para o botão
            .clickable { }
            .background(color = Color(0xFF9DEFD4), shape = MaterialTheme.shapes.medium) // Cor de fundo
            .padding(8.dp), // Espaçamento interno
        contentAlignment = Alignment.Center // Centraliza o conteúdo
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified // Isso fará com que o ícone use a cor padrão
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 12.sp)
        }
    }
}


