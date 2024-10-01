import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.ClienteResponse
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.model.SexoResponse
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun converterStringParaDate(data: String): Date? {
    val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return try {
        sdf.parse(data)
    } catch (e: ParseException) {
        Log.e("Cadastro", "Erro ao converter data: ${e.message}")
        null
    }
}

fun formatarDataParaEnviar(data: Date): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sdf.format(data)
}




fun formatarData(data: String): String? {
    return try {
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = formatoEntrada.parse(data)
        formatoSaida.format(date)
    } catch (e: ParseException) {
        Log.e("Erro de Formatação", "Erro ao formatar data: ${e.message}")
        null
    }
}






@Composable
fun Cadastro(controleDeNavegacao: NavHostController) {
    var nomeState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dataNascimentoState by remember { mutableStateOf("") }
    var senhaState by remember { mutableStateOf("") }
    var cpfState by remember { mutableStateOf("") }
    var crpState by remember { mutableStateOf("") }
   var preferenciaSelecionada by remember { mutableStateOf(1) }
    var isPsicologoState by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()
    val sexoService = retrofitFactory.getSexoService()
    var sexos by remember { mutableStateOf<List<Sexo>>(emptyList()) }
    var loadingSexos by remember { mutableStateOf(true) }


    val psicologoService = retrofitFactory.getPsicologoService()

    LaunchedEffect(Unit) {
        sexoService.getSexo().enqueue(object : Callback<SexoResponse> {
            override fun onResponse(call: Call<SexoResponse>, response: Response<SexoResponse>) {
                if (response.isSuccessful) {
                    sexos = response.body()?.data ?: emptyList()
                    Log.d("Sexos", "Sexos carregados: $sexos")
                } else {
                    Toast.makeText(context, "Erro ao carregar sexos: ${response.code()}", Toast.LENGTH_SHORT).show()
                    Log.e("Erro", "Corpo da resposta: ${response.errorBody()?.string()}")
                }
                loadingSexos = false
            }

            override fun onFailure(call: Call<SexoResponse>, t: Throwable) {
                Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                loadingSexos = false
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFF),
                        Color(0xF1F3F3)
                    )
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Cadastre-se",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF3E9C81)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { isPsicologoState = true },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isPsicologoState) Color(0xFF22AF87) else Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Psicólogo", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { isPsicologoState = false },
                        colors = ButtonDefaults.buttonColors(containerColor = if (!isPsicologoState) Color(0xFF22AF87) else Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cliente", color = Color.White)
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = nomeState,
                    onValueChange = { nomeState = it },
                    label = { Text("Nome Completo", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = telefoneState,
                    onValueChange = { telefoneState = it },
                    label = { Text("Telefone", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White // Para a cor do cursor
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("Email", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White // Para a cor do cursor
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = dataNascimentoState,
                    onValueChange = { input ->
                        if (input.length <= 10) {
                            dataNascimentoState = input
                        }
                    },
                    label = { Text("Data de Nascimento (dd/MM/yyyy)", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White // Para a cor do cursor
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}


            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = senhaState,
                    onValueChange = { senhaState = it },
                    label = { Text("Senha", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White // Para a cor do cursor
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                ) {
                OutlinedTextField(
                    value = cpfState,
                    onValueChange = { cpfState = it },
                    label = { Text("CPF", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFAACFBE),
                        unfocusedBorderColor = Color(0xFFAACFBE),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }}


            if (isPsicologoState) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
                    ) {
                    OutlinedTextField(
                        value = crpState,
                        onValueChange = { crpState = it },
                        label = { Text("CRP", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAACFBE),
                            unfocusedBorderColor = Color(0xFFAACFBE),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }}

            item {
                Button(
                    onClick = {

                        if (nomeState.isBlank() || telefoneState.isBlank() || emailState.isBlank() ||
                            senhaState.isBlank() || cpfState.isBlank() || (isPsicologoState && crpState.isBlank())) {
                            Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (isPsicologoState) {

                            val psicologo = Psicologo(
                                nome = nomeState,
                                telefone = telefoneState,
                                email = emailState,
                                data_nascimento = dataNascimentoState,
                                senha = senhaState,
                                id_sexo = 1,
                                cpf = cpfState,
                                cip = crpState,
                                link_instagram = null.toString(),
                                foto_perfil = null,
                                descricao = "teste"

                            )


                            coroutineScope.launch {
                                psicologoService.cadastrarPsicologo(psicologo).enqueue(object : Callback<Psicologo> {
                                    override fun onResponse(call: Call<Psicologo>, response: Response<Psicologo>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Psicólogo cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                            controleDeNavegacao.navigate("login")
                                        } else {
                                            Toast.makeText(context, "Erro ao cadastrar psicólogo: ${response.code()}", Toast.LENGTH_SHORT).show()
                                            Log.e("Cadastro", "Erro ao cadastrar: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<Psicologo>, t: Throwable) {
                                        Toast.makeText(context, "Erro: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        Log.e("Cadastro", "Falha na chamada: ${t.localizedMessage}")
                                    }
                                })
                            }
                        } else {

                            val cliente = Cliente(
                                nome = nomeState,
                                telefone = telefoneState,
                                email = emailState,
                                data_nascimento = dataNascimentoState,
                                senha = senhaState,
                                id_sexo = 1,
                                cpf = cpfState,
                                link_instagram = null,
                                foto_perfil = null,
                                id_preferencias = emptyList()
                            )

                            coroutineScope.launch {
                                clienteService.cadastrarCliente(cliente).enqueue(object : Callback<ClienteResponse> {
                                    override fun onResponse(call: Call<ClienteResponse>, response: Response<ClienteResponse>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                                            // Acesse o ID do usuário corretamente a partir da resposta
                                            val clienteID = response.body()?.user?.id // Acesse o ID do usuário aqui
                                            Log.d("Cadastro", "Cliente ID: $clienteID")

                                            if (clienteID != null) {
                                                controleDeNavegacao.navigate("preferencias/$clienteID")
                                            } else {
                                                Toast.makeText(context, "ID do cliente não disponível", Toast.LENGTH_SHORT).show()
                                                Log.e("Cadastro", "ID do cliente é null")
                                            }
                                        } else {
                                            Toast.makeText(context, "Erro ao cadastrar cliente: ${response.code()}", Toast.LENGTH_SHORT).show()
                                            Log.e("Cadastro", "Erro ao cadastrar: ${response.errorBody()?.string()}")
                                        }
                                    }

                                    override fun onFailure(call: Call<ClienteResponse>, t: Throwable) {
                                        Toast.makeText(context, "Erro: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                                        Log.e("Cadastro", "Falha na chamada: ${t.localizedMessage}")
                                    }
                                })
                            }



                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22AF87)),
                    shape = RoundedCornerShape(13.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(51.3.dp)
                ) {
                    Text(text = "Salvar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }




            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Já possui uma conta? ", color = Color.White)
                    Text(
                        text = "Faça login",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { controleDeNavegacao.navigate("login") }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCadastro() {
    val navController = rememberNavController()
    Cadastro(controleDeNavegacao = navController)
}
