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
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.toSize
import br.senai.sp.jandira.vivaris.R


// Função para formatar a data
fun formatarData(data: String): String? {
    return try {
        val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dataFormatada = formatoEntrada.parse(data)
        formatoSaida.format(dataFormatada!!)
    } catch (e: Exception) {
        null // Retorna null se a data não for válida
    }
}

fun formatarEntradaData(input: String): String {
    // Remove todos os caracteres que não são dígitos
    val apenasDigitos = input.replace(Regex("[^\\d]"), "")
    val formato = StringBuilder()

    // Limita a entrada a 8 dígitos (ddMMyyyy)
    val limite = minOf(apenasDigitos.length, 8)

    for (i in 0 until limite) {
        formato.append(apenasDigitos[i])
        // Adiciona a barra após o dia (2 dígitos) e o mês (4 dígitos)
        if (i == 1 || i == 3) {
            formato.append("/")
        }
    }

    // Verifica se a entrada tem exatamente 10 caracteres (dd/MM/yyyy)
    if (formato.length == 10) {
        // Valida a data
        val dia = formato.substring(0, 2).toIntOrNull() ?: return ""
        val mes = formato.substring(3, 5).toIntOrNull() ?: return ""
        val ano = if (formato.length == 10) formato.substring(6, 10).toIntOrNull() else null

        // Verifica se a data é válida
        if (ano != null && ano in 1900..2100 && mes in 1..12) {
            val diasNoMes = arrayOf(31, if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
            if (dia in 1..diasNoMes[mes - 1]) {
                return formato.toString() // Retorna a data formatada
            }
        }
    }

    return formato.toString() // Retorna o formato atual, mesmo que não seja uma data válida
}


@Composable
fun teste(controleDeNavegacao: NavHostController) {
}


@OptIn(ExperimentalMaterial3Api::class)
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
    var sexoSelecionado by remember { mutableStateOf<Sexo?>(null) }
    var isSenhaVisible by remember { mutableStateOf(false) }
    var isConfirmarSenhaVisible by remember { mutableStateOf(false) }
    var confirmarSenhaState by remember { mutableStateOf("") }
  //  var cursorPosition by remember { mutableStateOf(0) }
    //val datePickerState = rememberDatePickerState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val retrofitFactory = RetrofitFactory(context)
    val clienteService = retrofitFactory.getClienteService()
    val sexoService = retrofitFactory.getSexoService()
    var sexos by remember { mutableStateOf<List<Sexo>>(emptyList()) }
    var loadingSexos by remember { mutableStateOf(true) }
    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    var expanded by remember { mutableStateOf(false) }


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
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.vivarislogo),
                    contentDescription = "Logo Vivaris",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp),
                    contentScale = ContentScale.Fit
                )

            }

            item {
                Text(
                    text = "Cadastre-se",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF22AF87)
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { isPsicologoState = true },
                        colors = ButtonDefaults.buttonColors(containerColor = if (isPsicologoState) Color(0xFF296856) else Color(0xFF618773)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Psicólogo", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { isPsicologoState = false },
                        colors = ButtonDefaults.buttonColors(containerColor = if (!isPsicologoState) Color(0xFF296856) else Color(0xFF618773)),
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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        shape = RoundedCornerShape(16.dp))
                }}

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp))
                ) {
                    OutlinedTextField(
                        value = dataNascimentoState,
                        onValueChange = { input ->
                            // Formata a entrada da data
                            dataNascimentoState = formatarEntradaData(input)
                        },
                        label = { Text("Data de Nascimento (dd/MM/yyyy)", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAACFBE),
                            unfocusedBorderColor = Color(0xFFAACFBE),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White // Para a cor do cursor
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = "Selecione o Sexo",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 12.dp) // Espaçamento abaixo do título
                        )

                        if (loadingSexos) {
                            Text(
                                text = "Carregando Sexos...",
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.CenterHorizontally) // Centraliza o texto "Carregando"
                            )
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 12.dp), // Espaçamento acima das opções
                                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally) // Espaçamento entre os botões e centraliza
                            ) {
                                sexos.forEach { sexo ->
                                    Button(
                                        onClick = { sexoSelecionado = sexo },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = if (sexoSelecionado == sexo) Color(0xFF58A27C) else Color(0xFFE8F5E9), // Altera a cor dependendo da seleção
                                            contentColor = if (sexoSelecionado == sexo) Color.White else Color.Black // Altera a cor do texto
                                        ),
                                        shape = RoundedCornerShape(50), // Botões arredondados
                                        modifier = Modifier
                                            .height(38.dp) // Altura dos botões
                                            .widthIn(min = 30.dp ) // Largura mínima dos botões
                                    ) {
                                        Text(text = sexo.sexo)
                                    }
                                }
                            }
                        }
                    }
                }
            }


            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp))
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
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (isSenhaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isSenhaVisible = !isSenhaVisible }) {
                                Icon(
                                    imageVector = if (isSenhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isSenhaVisible) "Ocultar Senha" else "Mostrar Senha",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp))
                ) {
                    OutlinedTextField(
                        value = confirmarSenhaState,
                        onValueChange = { confirmarSenhaState = it },
                        label = { Text("Confirmar Senha", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFAACFBE),
                            unfocusedBorderColor = Color(0xFFAACFBE),
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            cursorColor = Color.White
                        ),
                        shape = RoundedCornerShape(16.dp),
                        visualTransformation = if (isConfirmarSenhaVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isConfirmarSenhaVisible = !isConfirmarSenhaVisible }) {
                                Icon(
                                    imageVector = if (isConfirmarSenhaVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                    contentDescription = if (isConfirmarSenhaVisible) "Ocultar Senha" else "Mostrar Senha",
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }
            }

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
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
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
                        if (senhaState != confirmarSenhaState) {
                            Toast.makeText(context, "As senhas não coincidem", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        val dataNascimentoFormatada = formatarData(dataNascimentoState)
                        if (dataNascimentoFormatada == null) {
                            Toast.makeText(context, "Data de nascimento inválida", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (isPsicologoState) {

                            val psicologo = Psicologo(
                                nome = nomeState,
                                telefone = telefoneState,
                                email = emailState,
                                data_nascimento = dataNascimentoFormatada,
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
                                data_nascimento = dataNascimentoFormatada,
                                senha = senhaState,
                                id_sexo = 1,
                                cpf = cpfState,
                                link_instagram = null,
                                foto_perfil = null

                            )

                            coroutineScope.launch {
                                clienteService.cadastrarCliente(cliente).enqueue(object : Callback<ClienteResponse> {
                                    override fun onResponse(call: Call<ClienteResponse>, response: Response<ClienteResponse>) {
                                        if (response.isSuccessful) {
                                     //       Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()


                                            val clienteID = response.body()?.user?.id
                                            Log.d("Cadastro", "Cliente ID: $clienteID")

                                            if (clienteID != null) {
                                                controleDeNavegacao.navigate("preferencias/$clienteID")
                                            } else {
                                                Toast.makeText(context, "ID do cliente não disponível", Toast.LENGTH_SHORT).show()
                                                Log.e("Cadastro", "ID do cliente é null")
                                            }
                                        } else {
                                            Log.d("Cadastro", "Dados enviados: $cliente")
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
                    Text(text = "Já possui uma conta? ", color = Color(0xFF296856))
                    Text(
                        text = "Faça login",
                        color = Color(0xFF296856),
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