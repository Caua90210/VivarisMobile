package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
import br.senai.sp.jandira.vivaris.security.DatabaseHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Configuracoes(controleDeNavegacao: NavHostController, clearData: () -> Unit) {

    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(58.dp)
        .border(
            width = 2.dp,
            color = Color(0xFF296856), // Cor da borda
            shape = RoundedCornerShape(10.dp)
        )

    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = Color(0x7D3E9C81),
        contentColor = Color(0xFFFFFFFF)
    )
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .background(color = Color(0xFF3E9C81))
                    .height(80.dp)
                ,
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = {
                      //  controleDeNavegacao.navigate()
                    }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back",
                            tint = Color(0xFFFFFFFFF))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = Color(0xFF3E9C81)
                )
            )

            Image(
                painter = painterResource(id = R.drawable.vivarislogo),
                contentDescription = "Logo Vivaris",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(60.dp)
                    .padding(top = 14.dp),
                contentScale = ContentScale.Fit
            )
        },
        content = { paddingValues ->


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Top
            ) {
                    Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { /* Handle 'Geral' button click */ },
                    modifier = buttonModifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Geral", textAlign = TextAlign.Start, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { /* Handle 'Privacidade e segurança' button click */ },
                    modifier = buttonModifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Privacidade e segurança", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão "Meus cartões"
                Button(
                    onClick = { controleDeNavegacao.navigate("addcartao")},
                    modifier = buttonModifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Meus cartões", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Botão "Sistema"
                Button(
                    onClick = { /* Handle 'Sistema' button click */ },
                    modifier = buttonModifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Sistema", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Button(
                    onClick = {
                        clearData()
                        controleDeNavegacao.navigate("login")
                    },
                    modifier = buttonModifier,
                    colors = buttonColors,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text("Sair da Conta", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {


        Spacer(modifier = Modifier.height(16.dp))


    }
}
//@Preview
//@Composable
//fun PreviewConfiguracoes() {
//    val navController = rememberNavController()
//
//    // Simulando a função clearData
//    val clearData = {
//        // Aqui você chamaria a função que limpa os dados
//        val dbHelper = DatabaseHelper(/* contexto aqui */)
//        dbHelper.clearData()
//    }
//
//    Configuracoes(controleDeNavegacao = navController, clearData = clearData)
//}