package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Composable
fun MenuItem(text: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}

@Composable
fun Home(controleDeNavegacao: NavHostController, userId: Int, isPsicologo: Boolean, nomeUsuario: String) {
    val loading = remember { mutableStateOf(true) }
    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()
    val showMenu = remember { mutableStateOf(false) }



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


    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, top = 5.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column (
                modifier = Modifier.padding(12.dp), // Adiciona espaçamento ao redor da coluna
                verticalArrangement = Arrangement.Top, // Alinha o conteúdo no topo
                horizontalAlignment = Alignment.Start // Alinha o conteúdo à esquerda
            ) {
                Text(
                    text = "Bom Dia,",
                    style = TextStyle(
                        color = Color(0xFF1DA580),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Text(
                    text = nomeUsuario,
                    style = TextStyle(
                        color = Color(0xFF1DA580),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
            }

            Image(
                painter = painterResource(id = R.drawable.vivarislogo),
                contentDescription = "Logo Vivaris",
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Fit
            )

            Row(
                modifier = Modifier.fillMaxSize()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = { /*TODO: implement notification icon click*/ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.notificacao),
                        contentDescription = "Notification",
                        tint = Color(0xFF1DA580),
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))
                
                IconButton(onClick = { showMenu.value = !showMenu.value }) {
                    Icon(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Hamburger",
                        tint = Color(0xFF1DA580),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }



        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {


            if (showMenu.value) {
                DropdownMenu(
                    expanded = showMenu.value,
                    onDismissRequest = { showMenu.value = false },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(16.dp)
                    ) {
                        MenuItem(text = "Meus Grupos", icon = Icons.Default.People) {
                            //  controleDeNavegacao.navigate("meusGrupos")
                        }
                        MenuItem(text = "Posts Curtidos", icon = Icons.Default.HeartBroken) {
                            //    controleDeNavegacao.navigate("postsCurtidos")
                        }
                        MenuItem(text = "Minhas preferências", icon = Icons.Default.Settings) {
                            // controleDeNavegacao.navigate("preferencias")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MenuItem(text = "Editar Perfil", icon = Icons.Default.Edit) {
                            //    controleDeNavegacao.navigate("editarPerfil")
                        }
                        MenuItem(text = "Configurações", icon = Icons.Default.Settings) {
                            controleDeNavegacao.navigate("configuracoes")
                        }
                        MenuItem(text = "Denúncia", icon = Icons.Default.Report) {
                            //    controleDeNavegacao.navigate("denuncia")
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        MenuItem(text = "FAQ", icon = Icons.Default.Chat) {
                            //    controleDeNavegacao.navigate("faq")
                        }
                    }
                }
            }

            if (loading.value) {
                Text("Carregando...")
            } else {
                // Bem-vindo
//                Text(
//                    "Bom Dia, $nomeUsuario!",
//                    style = MaterialTheme.typography.headlineMedium,
//                    modifier = Modifier.align(Alignment.Start)
//                )

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
                } else {
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
            Spacer(modifier = Modifier.height(90.dp))


        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF3E9C81))
                .height(80.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.Bottom,
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.inicio),
                    contentDescription = "inicio",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Ínicio", fontSize = 12.sp, color = Color.White)
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.agendamento),
                    contentDescription = "inicio",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Agendamento", fontSize = 12.sp, color = Color.White)
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.chatbot),
                    contentDescription = "inicio",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Chatbot", fontSize = 12.sp, color = Color.White)
            }


            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.perfil),
                    contentDescription = "perfil",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Perfil", fontSize = 12.sp, color = Color.White)
            }
        }}



    }
}



@Composable
fun FeatureButton(label: String, icon: Int) {
    // Mudar o tamanho dos botões
    Box(
        modifier = Modifier
            .size(100.dp) // Tamanho desejado para o botão
            .clickable { }
            .background(
                color = Color(0xFF9DEFD4),
                shape = MaterialTheme.shapes.medium
            ) // Cor de fundo
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



@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun HomePreview() {
    // Crie um NavHostController para passar para a função Home
    val navController = rememberNavController()

    // Chame a função Home com parâmetros de exemplo
    Home(
        controleDeNavegacao = navController,
        userId = 1,
        isPsicologo = true,
        nomeUsuario = "João da Silva"
    )
}


