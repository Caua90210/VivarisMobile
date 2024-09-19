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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import br.senai.sp.jandira.vivaris.model.Cliente

@Composable
fun Cadastro(controleDeNavegacao: NavHostController) {

    var nomeState by remember { mutableStateOf("") }
    var telefoneState by remember { mutableStateOf("") }
    var emailState by remember { mutableStateOf("") }
    var dataNascimentoState by remember { mutableStateOf("") }
    var senhaState by remember { mutableStateOf("") }
    var sexoState by remember { mutableStateOf("") }
    var crpState by remember { mutableStateOf("") }
    var isPsicologoState by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF15A27A),
                        Color(0xFF67DEBC)
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
                    color = Color.White
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { isPsicologoState = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Psicólogo", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { isPsicologoState = false },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x4D19493B)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cliente", color = Color.White)
                    }
                }
            }

            item {
                OutlinedTextField(
                    value = nomeState,
                    onValueChange = { nomeState = it },
                    label = { Text("Nome Completo", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = telefoneState,
                    onValueChange = { telefoneState = it },
                    label = { Text("Telefone", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text("E-mail", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = dataNascimentoState,
                    onValueChange = { dataNascimentoState = it },
                    label = { Text("Data de Nascimento", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = senhaState,
                    onValueChange = { senhaState = it },
                    label = { Text("Senha", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            item {
                OutlinedTextField(
                    value = sexoState,
                    onValueChange = { sexoState = it },
                    label = { Text("Sexo", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFFFFFFF),
                        unfocusedBorderColor = Color(0xFFFFFFFF),
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
            }

            if (isPsicologoState) {
                item {
                    OutlinedTextField(
                        value = crpState,
                        onValueChange = { crpState = it },
                        label = { Text("CRP", color = Color.White) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFFFFFFF),
                            unfocusedBorderColor = Color(0xFFFFFFFF),
                            focusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(16.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        if (nomeState.isBlank() || telefoneState.isBlank() || emailState.isBlank() || senhaState.isBlank()) {
                            Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                        } else {
                            val cliente = Cliente(
                                nome = nomeState,
                                telefone = telefoneState,
                                email = emailState,
                                dataNascimento = dataNascimentoState,
                                senha = senhaState,
                                id_sexo = sexoState,
                                link_instagram = "",
                                foto_perfil = null,
                                cpf = "12345678900"
                            )

                            coroutineScope.launch {
                                clienteService.cadastrarCliente(cliente).enqueue(object : Callback<Cliente> {
                                    override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Cliente cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                                            controleDeNavegacao.navigate("login")
                                        } else {

                                            Toast.makeText(context, "Erro ao cadastrar cliente: ${response.code()}", Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Cliente>, t: Throwable) {

                                        Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0x4D19493B)),
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
                    modifier = Modifier
                        .fillMaxWidth(),
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
