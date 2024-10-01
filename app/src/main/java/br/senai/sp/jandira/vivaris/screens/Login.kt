import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.model.Cliente
import br.senai.sp.jandira.vivaris.model.LoginUsuario
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

    val retrofitFactory = RetrofitFactory()
    val clienteService = retrofitFactory.getClienteService()

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
                    onClick = { },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF296856)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Psicólogo", color = Color.White)
                }

                Spacer(modifier = Modifier.width(12.dp))

                Button(
                    onClick = { },
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
                onValueChange = { emailState.value = it },
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
            )}

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFAACFBE), shape = RoundedCornerShape(16.dp)) // Cor de fundo do campo
            ) {
            OutlinedTextField(
                value = senhaState.value,
                onValueChange = { senhaState.value = it },
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
            )}

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = {
                    val loginRequest = LoginUsuario(email = emailState.value, senha = senhaState.value)
                    clienteService.loginUsuario(loginRequest).enqueue(object : Callback<Cliente> {
                        override fun onResponse(call: Call<Cliente>, response: Response<Cliente>) {
                            Log.i("dados: ", loginRequest.email)
                            if (response.isSuccessful) {
                                val cliente = response.body()
                                if (cliente != null) {
                                    controleDeNavegacao.navigate("home/${cliente.id}")
                                } else {
                                    erroState.value = true
                                    mensagemErroState.value = "Erro ao obter os dados do usuário!"
                                }
                            } else {
                                erroState.value = true
                                mensagemErroState.value = "Usuário e senha incorretos!"
                            }
                        }

                        override fun onFailure(call: Call<Cliente>, t: Throwable) {
                            erroState.value = true
                            mensagemErroState.value = "Erro: ${t.localizedMessage}"
                        }
                    })
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF22AF87)),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(51.3.dp)
            ) {
                Text(text = "Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            if (erroState.value) {
                Text(text = mensagemErroState.value, color = Color.Red, modifier = Modifier.padding(8.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Não possui uma conta? ", color = Color(0xFF085848))
                Text(
                    text = "Cadastre-se",
                    color = Color(0xFF085848),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { controleDeNavegacao.navigate("cadastro") }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLogin() {
    val navController = rememberNavController()
    Login(navController)
}
