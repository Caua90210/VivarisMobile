import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.model.PreferenciaCliente
import br.senai.sp.jandira.vivaris.model.Preferencias
import br.senai.sp.jandira.vivaris.model.PreferenciasResponse
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PreferenciasScreen(
    navController: NavHostController,
    clienteId: Int
) {
    var preferenciasList by remember { mutableStateOf(emptyList<Preferencias>()) }
    var selectedPreferencias by remember { mutableStateOf(mutableListOf<Int>()) }
    var loading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    var isRegistering by remember { mutableStateOf(false) }
    val retrofitService = RetrofitFactory().getPreferenciasService()
    var isSubmitting by remember { mutableStateOf(false) }



    Log.d("PreferenciasScreen", "Iniciando a tela de preferências para o cliente ID: $clienteId")

    LaunchedEffect(Unit) {
        Log.d("PreferenciasScreen", "Buscando todas as preferências...")
        retrofitService.getAllPreferencias().enqueue(object : Callback<PreferenciasResponse> {
            override fun onResponse(call: Call<PreferenciasResponse>, response: Response<PreferenciasResponse>) {
                Log.d("PreferenciasScreen", "Resposta recebida: ${response.code()}")

                if (response.isSuccessful) {
                    preferenciasList = response.body()?.data ?: emptyList()
                    Log.d("Preferencias", "Preferências carregadas: $preferenciasList")
                } else {
                    showToast(context, "Erro: ${response.code()}")
                    Log.e("PreferenciasScreen", "Erro ao carregar preferências: ${response.errorBody()?.string()}")
                }
                loading = false
                Log.d("PreferenciasScreen", "Carregamento concluído, loading setado para false.")
            }

            override fun onFailure(call: Call<PreferenciasResponse>, t: Throwable) {
                showToast(context, "Erro: ${t.message}")
                Log.e("PreferenciasScreen", "Falha ao buscar preferências: ${t.localizedMessage}")
                loading = false
            }
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .height(200.dp)
            .background(Color(0xF1f1f1f1))
            // Define os cantos arredondados
          //  .padding(16.dp) // Adiciona padding interno para evitar que o conteúdo toque nas bordas
    ) {

    Column(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp)). height(200.dp).background(Color(0xFF3E9C81)) ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.width(30.dp))
        Image(
            painter = painterResource(id = R.drawable.vivarislogo),
            contentDescription = "Logo Vivaris",
            modifier = Modifier
                .fillMaxWidth()
                .size(100.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = "Para melhor experiência, diga-nos, por que procura a Vivaris?",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), // Espaçamento interno para o texto
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }



    if (loading) {
        Log.d("PreferenciasScreen", "Mostrando indicador de carregamento...")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Log.d("PreferenciasScreen", "Exibindo a lista de preferências.")
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween // Garante que o botão fique no final da tela
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f), // Ajusta para que a lista use o espaço restante
                contentPadding = PaddingValues(8.dp)
            ) {
                items(preferenciasList) { preferencia ->
                    PreferenciasCard(preferencia) { preferenciaId ->
                        if (!selectedPreferencias.contains(preferenciaId)) {
                            selectedPreferencias.add(preferenciaId)
                        } else {
                            selectedPreferencias.remove(preferenciaId)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Espaçamento antes do botão

            Button(
                onClick = {
                    if (selectedPreferencias.isNotEmpty()) {
                        // Chama a função de cadastro de preferências
                        cadastrarPreferencia(selectedPreferencias, clienteId, navController, context, onComplete = {
                            // Navegação para a tela de login
                            navController.navigate("login")
                        }, onRegisterChange = { isRegistering = it })
                    } else {
                        showToast(context, "Selecione pelo menos uma preferência.")
                    }
                },
                enabled = !isRegistering,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(height = 61.dp)
                    .padding(horizontal = 40.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00796B))
            ) {
                Text("Iniciar Jornada", fontSize = 18.sp, color = Color.White)
            }


            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { navController.navigate("login") },
                    modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Text("Pular esta etapa", color = Color(0xFF085848), fontSize = 16.sp,textAlign = TextAlign.Center)
                    Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                }
            }

            Spacer(modifier = Modifier.height(120.dp))

        }}
    }
}




@Composable
fun PreferenciasGrid(preferencias: List<Preferencias>, onSelect: (Int) -> Unit) {
    Log.d("PreferenciasGrid", "Preferências recebidas: $preferencias")
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Definindo 2 colunas
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp) // Espaçamento ao redor da grade
    ) {
        items(preferencias) { preferencia ->
            PreferenciasCard(preferencia) { onSelect(preferencia.id) }
        }
    }
}

@Composable
fun PreferenciasCard(preferencia: Preferencias, onSelect: (Int) -> Unit) {
    var isSelected by remember { mutableStateOf(false) } // Estado para controlar se a preferência está selecionada


    Log.d(
        "PreferenciasCard",
        if (isSelected) "Preferência selecionada: ${preferencia.nome}, ID: ${preferencia.id}"
        else "Preferência deselecionada: ${preferencia.nome}, ID: ${preferencia.id}"
    )

    val backgroundColor = if (isSelected) {
        preferencia.cor.toColor().copy(alpha = 0.8f) // Escurece a cor quando selecionado
    } else {
       Color.Gray // Cor normal quando não selecionado
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(120.dp)
            .width(160.dp)
            .clickable {
                isSelected = !isSelected // Alterna o estado de seleção
                onSelect(preferencia.id) // Chama o callback com o ID da preferência
            }
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor), // Aplica a cor de fundo
        shape = RoundedCornerShape(12.dp)
    ) {
        // Centraliza o conteúdo do card
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp), // Espaçamento interno do card
            contentAlignment = Alignment.Center // Centraliza o texto no card
        ) {
            Text(
                text = preferencia.nome,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White // Cor do texto
            )
        }
    }
}




fun cadastrarPreferencia(
    listaPreferencias: List<Int>, // Ajustado para uma lista de IDs de preferências
    idCliente: Int,
    navController: NavHostController,
    context: Context,
    onRegisterChange: (Boolean) -> Unit,
    isRegistering: Boolean = false,
    onComplete: () -> Unit
) {


    if (isRegistering) return // Evita múltiplos cliques enquanto está registrando

    onRegisterChange(true)

    val preferenciaCliente = PreferenciaCliente(
        id_cliente = idCliente,
        preferencias = listaPreferencias
    )

    // Log para verificar os dados que estão sendo enviados
    Log.d("CadastroPreferencia", "Cliente ID: $idCliente, Preferências: $listaPreferencias")

    // Chamada ao serviço Retrofit
    RetrofitFactory().getPreferenciasService().cadastrarPreferenciaCliente(preferenciaCliente)
        .enqueue(object : retrofit2.Callback<PreferenciasResponse> {
            override fun onResponse(
                call: Call<PreferenciasResponse>,
                response: Response<PreferenciasResponse>
            ) {

                onRegisterChange(false)
                Log.d("CadastroPreferencia", "Resposta da tentativa de cadastro: ${response.code()}")

                if (response.isSuccessful) {
                    // Preferências cadastradas com sucesso
                    showToast(context, "Preferências cadastradas com sucesso!")
                    Log.d("CadastroPreferencia", "Preferências cadastradas com sucesso!")
                    navController.navigate("login")
                    onComplete()
                } else {
                    // Verifica o código de resposta e redireciona se for 500 ou 404
                    when (response.code()) {
                        500 -> {
                            Log.e("CadastroPreferencia", "Erro 500: Erro interno do servidor.")
                          //  showToast(context, "Erro interno do servidor. Redirecionando...")
                            navController.navigate("login")
                        }
                        404 -> {
                            Log.e("CadastroPreferencia", "Erro 404: Recurso não encontrado.")
                        //    showToast(context, "Recurso não encontrado. Redirecionando...")
                            navController.navigate("login")
                        }
                        else -> {
                            Log.e("CadastroPreferencia", "Erro: ${response.code()} - ${response.errorBody()?.string()}")
                            showToast(context, "Erro ao cadastrar as preferências: ${response.errorBody()?.string()}")
                        }

                    }
                }

            }

            override fun onFailure(call: Call<PreferenciasResponse>, t: Throwable) {

                onRegisterChange(false)
                // Log de falha na requisição
                Log.e("CadastroPreferencia", "Falha: ${t.localizedMessage}")
                showToast(context, "Erro: ${t.localizedMessage}")
            }
        })
}


// Função para mostrar Toast
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    Log.d("Toast", message)
}

// Função de extensão para converter String em Color
fun String.toColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Log.e("ColorConversion", "Erro ao converter a cor: ${this}, Exception: ${e.localizedMessage}")
        Color.Gray
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPreferenciasScreen() {
    val navController = rememberNavController()
    val clienteId = 1

    Log.d("PreviewPreferenciasScreen", "Exibindo a tela de preferências para pré-visualização.")
    PreferenciasScreen(navController = navController, clienteId = clienteId)
}
