import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.DataResponse
import br.senai.sp.jandira.vivaris.model.LoginPsicologo
import br.senai.sp.jandira.vivaris.model.LoginUsuario
import br.senai.sp.jandira.vivaris.model.PsicologoResponse
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import br.senai.sp.jandira.vivaris.model.LoginResponse
import br.senai.sp.jandira.vivaris.security.TokenManager
import br.senai.sp.jandira.vivaris.security.TokenRepository


@Composable
fun Login(controleDeNavegacao: NavHostController) {
    var emailState = remember { mutableStateOf("") }
    var senhaState = remember { mutableStateOf("") }
    var erroState = remember { mutableStateOf(false) }
    var mensagemErroState = remember { mutableStateOf("") }
    var isPsicologo = remember { mutableStateOf(false) }
    var senhaVisivel = remember { mutableStateOf(false) }
    var isLoggingIn by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)
    val clienteService = retrofitFactory.getClienteService()
    val psicologoService = retrofitFactory.getPsicologoService()

    val tokenRepository = TokenRepository(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFFFFFFF),
                        Color(0xFFF1F3F3)
                    )
                )
            )
    ) {
        AnimatedVisibility(
            visible = !isLoggingIn, // Controla a visibilidade da tela com base no estado de login
            enter = fadeIn(),        // Animação de fade-in ao entrar
            exit = fadeOut()         // Animação de fade-out ao sair
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(87.dp))

                    Image(
                        painter = painterResource(id = R.drawable.vivarislogo),
                        contentDescription = "Logo Vivaris",
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(100.dp),
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        "Logue-se",
                        color = Color(0xFF085848),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {

                        Button(
                            onClick = {
                                isPsicologo.value = true
                                Log.d(
                                    "LoginScreen",
                                    "Botão Psicólogo clicado: isPsicologo = ${isPsicologo.value}"
                                )
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isPsicologo.value) Color(0xFF296856) else Color(
                                    0xFF618773
                                ),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Psicólogo")
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Button(
                            onClick = {
                                isPsicologo.value = false
                                Log.d(
                                    "LoginScreen",
                                    "Botão Cliente clicado: isPsicologo = ${isPsicologo.value}"
                                )
                            },
                            shape = RoundedCornerShape(50),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (!isPsicologo.value) Color(0xFF296856) else Color(
                                    0xFF618773
                                ),
                                contentColor = Color.White
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cliente", color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFAACFBE),
                                shape = RoundedCornerShape(16.dp)
                            ) // Cor de fundo do campo
                    ) {
                        OutlinedTextField(
                            value = emailState.value,
                            onValueChange = {
                                emailState.value = it
                                Log.d("LoginScreen", "Email alterado: ${emailState.value}")
                            },
                            label = { Text("Email", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAACFBE),
                                unfocusedBorderColor = Color(0xFFAACFBE),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White // Para a cor do cursor
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Email,
                                    contentDescription = "Email Icon",
                                    tint = Color(0xFFFFFFFF)
                                )
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Color(0xFFAACFBE),
                                shape = RoundedCornerShape(16.dp)
                            ) // Cor de fundo do campo
                    ) {
                        OutlinedTextField(
                            value = senhaState.value,
                            onValueChange = {
                                senhaState.value = it
                            },
                            label = { Text("Senha", color = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            visualTransformation = if (senhaVisivel.value) VisualTransformation.None else PasswordVisualTransformation(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFFAACFBE),
                                unfocusedBorderColor = Color(0xFFAACFBE),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White // Para a cor do cursor
                            ),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Lock Icon",
                                    tint = Color(0xFFFFFFFF)
                                )
                            },
                            trailingIcon = {
                                IconButton(onClick = {
                                    senhaVisivel.value =
                                        !senhaVisivel.value // Alterna a visibilidade da senha
                                }) {
                                    Icon(
                                        imageVector = if (senhaVisivel.value) Icons.Filled.Visibility else Icons.Filled.VisibilityOff, // Corrigido para incluir ambos os ícones
                                        contentDescription = if (senhaVisivel.value) "Esconder senha" else "Mostrar senha",
                                        tint = Color(0xFFFFFFFF)
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                    // Parte do botão de login

                    fun saveLoginData(
                        context: Context,
                        token: String,
                        userId: Int,
                        isPsicologo: Boolean,
                        userName: String
                    ) {
                        val tokenRepository = TokenRepository(context)
                        tokenRepository.saveUserData(token, userId, isPsicologo, userName)
                    }

                    // Função auxiliar para tratar erros de login
                    fun handleLoginError(response: Response<*>) {
                        when (response.code()) {
                            404 -> {
                                erroState.value = true
                                mensagemErroState.value = "Usuário não encontrado!"
                            }
                            else -> {
                                erroState.value = true
                                mensagemErroState.value = "Erro ao tentar fazer login"
                            }
                        }
                    }

                    fun fazerLogin(
                        context: Context,
                        email: String,
                        senha: String,
                        isPsicologo: Boolean
                    ) {
                        // Cria o request de login com base no tipo de usuário (psicólogo ou cliente)
                        val loginRequest: Any = if (isPsicologo) {
                            LoginPsicologo(email = email, senha = senha)
                        } else {
                            LoginUsuario(email = email, senha = senha)
                        }

                        // Verifica se é psicólogo ou cliente e faz a requisição apropriada
                        if (isPsicologo) {
                            // Login para psicólogo
                            psicologoService.psicologoLogin(loginRequest as LoginPsicologo)
                                .enqueue(object : Callback<PsicologoResponse> {
                                    override fun onResponse(
                                        p0: Call<PsicologoResponse>,
                                        response: Response<PsicologoResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val psicologoResponse = response.body()
                                            if (psicologoResponse?.status_code == 200) {
                                                val psicologo = psicologoResponse.data
                                                if (psicologo != null) {
                                                    val token = psicologoResponse.token
                                                    if (token != null) {
                                                        // Salva os dados do psicólogo
                                                        saveLoginData(
                                                            context = context,
                                                            token = token,
                                                            userId = psicologo.id,
                                                            isPsicologo = true,
                                                            userName = psicologo.nome
                                                        )
                                                    }
                                                    controleDeNavegacao.navigate("home/${psicologo.id}/true/${psicologo.nome}")
                                                } else {
                                                    erroState.value = true
                                                    mensagemErroState.value = "Erro ao obter os dados do psicólogo!"
                                                }
                                            } else {
                                                erroState.value = true
                                                mensagemErroState.value = "Erro: ${psicologoResponse?.message}"
                                            }
                                        } else {
                                            handleLoginError(response)
                                        }
                                    }

                                    override fun onFailure(p0: Call<PsicologoResponse>, t: Throwable) {
                                        erroState.value = true
                                        mensagemErroState.value = "Erro: ${t.localizedMessage}"
                                    }
                                })
                        } else {
                            // Login para cliente
                            clienteService.loginUsuario(loginRequest as LoginUsuario)
                                .enqueue(object : Callback<LoginResponse> {
                                    override fun onResponse(
                                        call: Call<LoginResponse>,
                                        response: Response<LoginResponse>
                                    ) {
                                        if (response.isSuccessful) {
                                            val loginResponse = response.body()
                                            if (loginResponse != null && loginResponse.cliente.usuario.id != 0) {
                                                val token = loginResponse.token
                                                if (token != null) {
                                                    // Salva os dados do cliente
                                                    saveLoginData(
                                                        context = context,
                                                        token = token,
                                                        userId = loginResponse.cliente.usuario.id,
                                                        isPsicologo = false,
                                                        userName = loginResponse.cliente.usuario.nome
                                                    )
                                                }
                                                controleDeNavegacao.navigate("home/${loginResponse.cliente.usuario.id}/false/${loginResponse.cliente.usuario.nome}")
                                            } else {
                                                erroState.value = true
                                                mensagemErroState.value = "Erro ao obter os dados do cliente!"
                                            }
                                        } else {
                                            handleLoginError(response)
                                        }
                                    }

                                    override fun onFailure(p0: Call<LoginResponse>, t: Throwable) {
                                        erroState.value = true
                                        mensagemErroState.value = "Erro: ${t.localizedMessage}"
                                    }
                                })
                        }
                    }

                    Button(
                        onClick = {
                            Log.d("LoginScreen", "Botão de login clicado")
                            // Logando os dados de entrada
                            Log.d("LoginScreen", "Tentando login com Email: ${emailState.value} e Senha: ${senhaState.value.replace(Regex("."), "*")}")

                            // Chama a função para fazer o login
                            fazerLogin(
                                context = context,
                                email = emailState.value,
                                senha = senhaState.value,
                                isPsicologo = isPsicologo.value
                            )
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF22AF87)),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                            .height(58.dp)
                    ) {
                        Text("Entrar", color = Color.White, fontSize = 24.sp)
                    }






                    Spacer(modifier = Modifier.height(80.dp))

                    Column {
                        Text("Não possui uma conta?", color = Color(0xFF085848), fontSize = 18.sp)
                        TextButton(
                            onClick = { controleDeNavegacao.navigate("cadastro") },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(
                                "Cadastre-se",
                                color = Color(0xFF085848),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                }

                Box(modifier = Modifier.fillMaxSize()) { // Envolva o Snackbar em um Box
                    if (erroState.value) {
                        Snackbar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            action = {
                                TextButton(onClick = { erroState.value = false }) {
                                    Text("Fechar", color = Color.White)
                                }
                            },
                            containerColor = Color(0xFF296856),
                            contentColor = Color.White,
                        ) {
                            Text(mensagemErroState.value)
                        }
                    }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    // Use a NavHostController for navigation
    val navController = rememberNavController()
    Login(controleDeNavegacao = navController)
}
