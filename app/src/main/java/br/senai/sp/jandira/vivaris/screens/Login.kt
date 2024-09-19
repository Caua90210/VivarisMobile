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

@Composable
fun Login(controleDeNavegacao: NavHostController) {
    var emailState = remember { mutableStateOf("") }
    var senhaState = remember { mutableStateOf("") }
    var erroState = remember { mutableStateOf(false) }
    var mensagemErroState = remember { mutableStateOf("") }

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
        Text("Logue-se", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)

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
                    onClick = {  },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF296856)),
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cliente", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = { Text("Email", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFFFFF),
                    unfocusedBorderColor = Color(0xFFFFFFFF),
                    focusedTextColor = Color.Black
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

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = senhaState.value,
                onValueChange = { senhaState.value = it },
                modifier = Modifier.fillMaxWidth(0.9f),
                label = { Text("Senha", color = Color.White) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFFFFF),
                    unfocusedBorderColor = Color(0xFFFFFFFF),
                    focusedTextColor = Color.Black
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

            Spacer(modifier = Modifier.height(28.dp))


            Button(
                onClick = {
                    if (emailState.value == "teste@gmail.com" && senhaState.value == "1234") {
                        // controleDeNavegacao!!.navigate("home")
                    } else {
                        erroState.value = true
                        mensagemErroState.value = "Usuário e senha incorretos!"
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0x4D19493B)),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(51.3.dp)
            ) {
                Text(text = "Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Não possui uma conta? ", color = Color.White)
                Text(
                    text = "Cadastre-se",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { controleDeNavegacao!!.navigate("cadastro") }
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