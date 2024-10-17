import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
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

@Composable
fun Login(controleDeNavegacao: NavHostController) {
    var emailState = remember { mutableStateOf("") }
    var senhaState = remember { mutableStateOf("") }
    var erroState = remember { mutableStateOf(false) }
    var mensagemErroState = remember { mutableStateOf("") }
    var isPsicologo = remember { mutableStateOf(false) }

    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()
    val psicologoService = retrofitFactory.getPsicologoService()

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

            Text("Logue-se", color = Color(0xFF085848), fontSize = 48.sp, fontWeight = FontWeight.Bold)

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
                        Log.d("LoginScreen", "Botão Psicólogo clicado: isPsicologo = ${isPsicologo.value}")
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF296856)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Psicólogo", color = Color.White)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = {
                        isPsicologo.value = false
                        Log.d("LoginScreen", "Botão Cliente clicado: isPsicologo = ${isPsicologo.value}")
                    },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF296856)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cliente", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
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
                    .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
            ) {
                OutlinedTextField(
                    value = senhaState.value,
                    onValueChange = {
                        senhaState.value = it
                    },
                    label = { Text("Senha", color = Color.White) },
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
                            imageVector = Icons.Filled.Lock,
                            contentDescription = "Lock Icon",
                            tint = Color(0xFFFFFFFF)
                        )
                    },
                    shape = RoundedCornerShape(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            // Parte do botão de login
            Button(
                onClick = {
                    Log.d("LoginScreen", "Botão de login clicado")
                    val loginRequest: Any = if (isPsicologo.value) {
                        // Cria um request de login para psicólogo (tipagem correta para psicólogo)
                        LoginPsicologo(email = emailState.value, senha = senhaState.value)
                    } else {
                        // Cria um request de login para cliente (tipagem correta para cliente)
                        LoginUsuario(email = emailState.value, senha = senhaState.value)
                    }

                    if (isPsicologo.value) {
                        // Login para psicólogo
                        Log.d("LoginScreen", "Tentando login como psicólogo")
                        psicologoService.psicologoLogin(loginRequest as LoginPsicologo).enqueue(object : Callback<PsicologoResponse> {


                            override fun onResponse(
                                p0: Call<PsicologoResponse>,
                                response: Response<PsicologoResponse>
                            ) {
                                Log.d("LoginScreen", "Resposta recebida: ${response.code()}")

                                if (response.isSuccessful) {
                                    val psicologoResponse = response.body()
                                    Log.d("LoginScreen", "Corpo da resposta (Psicólogo): ${psicologoResponse}")

                                    if (psicologoResponse?.status_code == 200) {
                                        val psicologo = psicologoResponse.data
                                        if (psicologo != null) {
                                            controleDeNavegacao.navigate("home/${psicologo.id}/true")
                                            Log.d("LoginScreen", "Navegando para home com ID: ${psicologo.id}")
                                        } else {
                                            erroState.value = true
                                            mensagemErroState.value = "Erro ao obter os dados do psicólogo!"
                                            Log.e("LoginScreen", "Erro: psicólogo não encontrado!")
                                        }
                                    } else {
                                        erroState.value = true
                                        mensagemErroState.value = "Erro: ${psicologoResponse?.message ?: "Erro desconhecido"}"
                                        Log.e("LoginScreen", "Erro: ${psicologoResponse?.message}")
                                    }
                                } else {
                                    // Lidar com erros de resposta
                                    if (response.code() == 404) {
                                        erroState.value = true
                                        mensagemErroState.value = "Usuário não encontrado!"
                                        Log.e("LoginSreen", "Erro 404: Usuário não encontrado ")
                                    } else {
                                        erroState.value = true
                                        mensagemErroState.value = "Erro ao tentar fazer login"
                                        Log.e("LoginSreen", "Erro no login: ${response.code()}")
                                    }
                                }
                            }

                            override fun onFailure(p0: Call<PsicologoResponse>, t: Throwable) {
                                erroState.value = true
                                mensagemErroState.value = "Erro: ${t.localizedMessage}"
                                Log.e("LoginScreen", "Falha na conexão: ${t.localizedMessage}")
                            }
                        })
                    } else {
                        // Login para cliente
                        Log.d("LoginScreen", "Tentando login como cliente")
                        clienteService.loginUsuario(loginRequest as LoginUsuario).enqueue(object : Callback<Cliente> {
                            override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                                Log.d("LoginScreen", "Resposta recebida: ${response.code()}")

                                if (response.isSuccessful) {
                                    val cliente = response.body()
                                    Log.d("LoginScreen", "Corpo da resposta (Cliente): $cliente")

                                    if (cliente != null && cliente.id != 0) {
                                        controleDeNavegacao.navigate("home/${cliente.id}/false")
                                        Log.d("LoginScreen", "Navegando para home com ID: ${cliente.id}")
                                    } else {
                                        erroState.value = true
                                        mensagemErroState.value = "Erro ao obter os dados do cliente!"
                                        Log.e("LoginScreen", "Erro ao obter os dados do cliente!")
                                    }
                                } else {
                                    if (response.code() == 404) {
                                        erroState.value = true
                                        mensagemErroState.value = "Usuário não encontrado!"
                                        Log.e("LoginScreen", "Erro 404: Usuário não encontrado!")
                                    } else {
                                        erroState.value = true
                                        mensagemErroState.value = "Usuário e senha incorretos!"
                                        Log.e("LoginScreen", "Usuário e senha incorretos!")
                                    }
                                }
                            }

                            override fun onFailure(call: Call<Cliente>, t: Throwable) {
                                erroState.value = true
                                mensagemErroState.value = "Erro: ${t.localizedMessage}"
                                Log.e("LoginScreen", "Falha na conexão: ${t.localizedMessage}")
                            }
                        })
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF296856)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Entrar", color = Color.White, fontSize = 18.sp)
            }


            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { controleDeNavegacao.navigate("cadastro") }) {
                Text("Criar uma conta", color = Color(0xFF296856), fontSize = 14.sp)
            }
        }

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
