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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
                .padding(horizontal = 16.dp)
                .padding(top = 10.dp),
            verticalAlignment = Alignment.Top
        ) {
            Column (
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start
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
                modifier = Modifier
                    .fillMaxSize()
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

                Spacer(modifier = Modifier.height(70.dp))

                if (!isPsicologo){

                    Button(
                        onClick = { /*TODO*/ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBEBDA)),
                        shape = RoundedCornerShape(10.dp)

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Busque por psicológos...",
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Start
                                )

                            Icon(
                                painter = painterResource(id = R.drawable.searcg),
                                contentDescription = "Ícone de busca",
                                tint = Color.Gray, // Cor do ícone
                                modifier = Modifier.size(24.dp) // Tamanho do ícone
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(5.dp))
                }


               //

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF15A27A)) // Corrigido aqui
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "Como você está hoje?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color(0xFFFCFCF1))
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(

                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.starstruck),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp) // Definindo o tamanho da imagem
                                )


                                Image(
                                    painter = painterResource(id = R.drawable.sorrindo),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp) // Definindo o tamanho da imagem
                                )


                                Image(
                                    painter = painterResource(id = R.drawable.neutro),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp) // Definindo o tamanho da imagem
                                )


                                Image(
                                    painter = painterResource(id = R.drawable.desapontado),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp) // Definindo o tamanho da imagem
                                )


                                Image(
                                    painter = painterResource(id = R.drawable.chorando),
                                    contentDescription = "",
                                    modifier = Modifier.size(40.dp) // Definindo o tamanho da imagem
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                RadioButton(
                                    selected = ( false),
                                    onClick = {  }
                                )
                                RadioButton(
                                    selected = ( false),
                                    onClick = {  }
                                )
                                RadioButton(
                                    selected = ( false),
                                    onClick = {  }
                                )
                                RadioButton(
                                    selected = ( false),
                                    onClick = {  }
                                )
                                RadioButton(
                                    selected = ( false),
                                    onClick = {  }
                                )
                            }

                            Button(onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(9.dp),
                                colors = ButtonDefaults.buttonColors(Color(0xFF25856A))

                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Gostaria de falar sobre?")
                                    Icon(
                                        painter = painterResource(id = R.drawable.edit),
                                        contentDescription = "Ícone de busca",
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(5.dp))

                        }
                    }
                }

                if (isPsicologo) {



                    // Botões inferiores
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        FeatureButton("Blog", R.drawable.blog,
                            onClick = { /* Ação */ }, fullWidth = false,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
                        FeatureButton("Criar Grupo", R.drawable.grupos,
                            onClick = { /* Ação */ }, fullWidth = false,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
                        FeatureButton("Prontuários", R.drawable.prontuario,
                            onClick = { /* Ação */ }, fullWidth = false,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
                    }



                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FeatureButton("Eventos em Live", R.drawable.live,
                            onClick = { /* Ação */ }, fullWidth = true,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
//                        FeatureButton("Lembrete", R.drawable.bell)

                    }

               //

                    // Botão para psicólogo
//                    if (isPsicologo) {
//                        Button(
//                            onClick = {
//
//                            },
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .height(50.dp),
//                            colors = ButtonDefaults.buttonColors(Color(0xFF3E9C81))
//
//                        ) {
//                            Text(text = "Ir para Disponibilidade", fontSize = 16.sp)
//                        }
//                    }


                } else {
                    // Botões inferiores
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FeatureButton("Blog", R.drawable.blog, onClick = { /* Ação */ }, fullWidth = false,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
                        FeatureButton("Gráfico de Humor", R.drawable.graficohumor, onClick = {  }, fullWidth = false,
                            isBold = true,
                            textSize = 10.sp,
                            textColor = Color(0xFF296856)
                            )
                        FeatureButton("Diário", R.drawable.diario,onClick = { /* Ação */ }, fullWidth = false,
                            isBold = true,
                            textSize = 16.sp,
                            textColor = Color(0xFF296856))
                    }     // Spacer(modifier = Modifier.height(32.dp))
                    FeatureButton("Trilhas de Meditação", R.drawable.meditacao, onClick = { /* Ação */ },
                        fullWidth = true,
                        isBold = true,
                        textSize = 16.sp,
                        textColor = Color(0xFF296856))


                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.fillMaxWidth()
                    ) {


                    }

                }

            }






            //Spacer(modifier = Modifier.height(32.dp))
            FeatureButton(label = "Lembrete", icon = R.drawable.bell, onClick = { /* Ação */ },
                fullWidth = true,
                isBold = true,
                textSize = 16.sp,
                textColor = Color(0xFF296856))
          //  Spacer(modifier = Modifier.height(32.dp))

            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ){
                FeatureButton("Meus Chat`s", R.drawable.chat,
                    onClick = { /* Ação */ }, fullWidth = false,
                    isBold = true,
                    textSize = 16.sp,
                    textColor = Color(0xFF296856))
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
                        .padding(8.dp)
                        .clickable(onClick = {
                            if (isPsicologo) {
                                controleDeNavegacao.navigate("disponibilidade/$userId")
                            } else {
                                controleDeNavegacao.navigate("disponibilidadeCliente")
                            }
                        }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.agendamento),
                        contentDescription = "Agendamento",
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
        Image(
            painter = painterResource(id = R.drawable.gadets),
            contentDescription = "Logo Vivaris",
            modifier = Modifier
                .size(250.dp)
                .offset(x = -35.dp, y = 578.dp), // Desloca a imagem 16 dp para a direita
            contentScale = ContentScale.Fit
        )



    }
}

@Composable
fun FeatureButton(
    label: String,
    icon: Int,
    fullWidth: Boolean,
    onClick: () -> Unit = { },
    textColor: Color,
    textSize: TextUnit,
    isBold: Boolean,

) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .then(if (fullWidth) Modifier.fillMaxWidth() else Modifier.size(115.dp))
            .padding(8.dp), // Espaçamento interno
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCCEBDA)), // Cor de fundo
        shape = MaterialTheme.shapes.large // Forma do botão
    ) {
        Column(
           horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = label,
                modifier = Modifier.size(40.dp),
                tint = Color.Unspecified // Isso fará com que o ícone use a cor padrão
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = textSize,
                color = textColor,
                fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal // Define o peso da fonte
            )
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


