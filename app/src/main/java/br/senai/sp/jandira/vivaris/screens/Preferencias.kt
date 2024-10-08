import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
    val retrofitService = RetrofitFactory().getPreferenciasService()

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

    if (loading) {
        Log.d("PreferenciasScreen", "Mostrando indicador de carregamento...")
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        Log.d("PreferenciasScreen", "Exibindo a lista de preferências.")
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Selecione suas Preferências",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00796B),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PreferenciasGrid(preferenciasList) { preferenciaId ->
                if (!selectedPreferencias.contains(preferenciaId)) {
                    selectedPreferencias.add(preferenciaId)
                    Log.d("PreferenciasScreen", "Preferência selecionada: $preferenciaId")
                } else {
                    selectedPreferencias.remove(preferenciaId)
                    Log.d("PreferenciasScreen", "Preferência removida: $preferenciaId")
                }
                Log.d("PreferenciasScreen", "Preferências selecionadas: $selectedPreferencias")
            }

            Spacer(modifier = Modifier.height(16.dp)) // Este espaçador pode ser ajustado

            Button(
                onClick = {
                    Log.d("PreferenciasScreen", "Botão 'Iniciar Jornada' clicado.")
                    if (selectedPreferencias.isNotEmpty()) {
                        cadastrarPreferencia(selectedPreferencias, clienteId, navController, context)
                    } else {
                        showToast(context, "Selecione pelo menos uma preferência.")
                        Log.d("PreferenciasScreen", "Nenhuma preferência selecionada.")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), // Adicionando um padding vertical
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red) // Mudando a cor do botão
            ) {
                Text("Iniciar Jornada", fontSize = 18.sp, color = Color.White) // Mudando a cor do texto
            }
        }
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
    Log.d("PreferenciasCard", "Criando cartão para a preferência: ${preferencia.nome}, ID: ${preferencia.id}")
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(preferencia.id) }
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00796B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Card(
                modifier = Modifier.size(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = preferencia.cor.toColor())
            ) {
                Box(modifier = Modifier.fillMaxSize())
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = preferencia.nome,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

fun cadastrarPreferencia(
    listaPreferencias: List<Int>, // Ajustado para uma lista de IDs de preferências
    idCliente: Int,
    navController: NavHostController,
    context: Context
) {
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
                Log.d("CadastroPreferencia", "Resposta da tentativa de cadastro: ${response.code()}")

                if (response.isSuccessful) {
                    // Preferências cadastradas com sucesso
                    showToast(context, "Preferências cadastradas com sucesso!")
                    Log.d("CadastroPreferencia", "Preferências cadastradas com sucesso!")
                    navController.navigate("login")
                } else {
                    // Log de erro caso a resposta não seja bem-sucedida
                    Log.e("CadastroPreferencia", "Erro: ${response.code()} - ${response.errorBody()?.string()}")
                    showToast(context, "Erro ao cadastrar as preferências: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PreferenciasResponse>, t: Throwable) {
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
