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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

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
                        Color(0xFFAAF2A3), // Verde claro
                        Color(0xFF57C57A)  // Verde mais escuro
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Alternador entre Psicólogo e Cliente
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { /* Psicólogo */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF57C57A))
                ) {
                    Text("Psicólogo", color = Color.White)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = { /* Cliente */ },
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(Color(0xFF57C57A))
                ) {
                    Text("Cliente", color = Color.White)
                }
            }

            // Campo de E-mail
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Email") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF57C57A),
                    unfocusedBorderColor = Color(0xFFA09C9C),
                    focusedTextColor = Color.Black
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = "Email Icon",
                        tint = Color(0xFF57C57A)
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de Senha
            OutlinedTextField(
                value = senhaState.value,
                onValueChange = { senhaState.value = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Senha") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF57C57A),
                    unfocusedBorderColor = Color(0xFFA09C9C),
                    focusedTextColor = Color.Black
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        contentDescription = "Lock Icon",
                        tint = Color(0xFF57C57A)
                    )
                },
                shape = RoundedCornerShape(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botão de Entrar
            Button(
                onClick = {
                    if (emailState.value == "teste@gmail.com" && senhaState.value == "1234") {
                       // controleDeNavegacao!!.navigate("home")
                    } else {
                        erroState.value = true
                        mensagemErroState.value = "Usuário e senha incorretos!"
                    }
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF57C57A)),
                shape = RoundedCornerShape(13.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Entrar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Link de cadastro
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Não possui uma conta? ", color = Color.Gray)
                Text(
                    text = "Cadastre-se",
                    color = Color(0xFF57C57A),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { controleDeNavegacao!!.navigate("cadastro") }
                )
            }
        }
    }
}

