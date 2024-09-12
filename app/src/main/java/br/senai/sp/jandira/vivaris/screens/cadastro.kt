package br.senai.sp.jandira.vivaris.screens

import br.senai.sp.jandira.mytrips.model.Usuarios
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.repository.UsuarioRepository


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController



@Composable
fun Cadastro(controleDeNavegacao: NavHostController) {

    var nomeState = remember { mutableStateOf("") }
    var phoneState = remember { mutableStateOf("") }
    var emailState = remember { mutableStateOf("") }
    var dataNascimentoState = remember { mutableStateOf("") }
    var passwordState = remember { mutableStateOf("") }
    var sexoState = remember { mutableStateOf("") }

    val usuarioRepository = UsuarioRepository(LocalContext.current)

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Cadastre-se",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Red
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Nome
        OutlinedTextField(
            value = nomeState.value,
            onValueChange = { nomeState.value = it },
            label = { Text(text = "Nome Completo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Telefone
        OutlinedTextField(
            value = phoneState.value,
            onValueChange = { phoneState.value = it },
            label = { Text(text = "Telefone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // E-mail
        OutlinedTextField(
            value = emailState.value,
            onValueChange = { emailState.value = it },
            label = { Text(text = "E-mail") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Data de Nascimento
        OutlinedTextField(
            value = dataNascimentoState.value,
            onValueChange = { dataNascimentoState.value = it },
            label = { Text(text = "Data de Nascimento") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Senha
        OutlinedTextField(
            value = passwordState.value,
            onValueChange = { passwordState.value = it },
            label = { Text(text = "Senha") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Sexo
        OutlinedTextField(
            value = sexoState.value,
            onValueChange = { sexoState.value = it },
            label = { Text(text = "Sexo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botão de cadastro
        Button(
            onClick = {

                // Criar um novo objeto de usuário
                val novoUsuario = Usuarios(
                    nome = nomeState.value,
                    phone = phoneState.value,
                    email = emailState.value,
                    dataNascimento = dataNascimentoState.value,
                    password = passwordState.value,
                    sexo = sexoState.value
                )

                // Salvar o novo usuário no repositório
                usuarioRepository.salvar(novoUsuario)

                // Navegar para a tela "home" após o cadastro
                controleDeNavegacao.navigate("home")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Salvar")
        }
    }
}
