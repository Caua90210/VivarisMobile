package br.senai.sp.jandira.vivaris.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController

@Composable
fun DisponibilidadeCliente(){
    var selectedDays by remember { mutableStateOf(setOf<String>()) }


    // Mapeamento dos dias da semana
    val daysOfWeekMap = mapOf(
        "D" to "Domingo",
        "S" to "Segunda",
        "T" to "Terca",
        "Q1" to "Quarta",
        "Q2" to "Quinta",
        "F" to "Sexta",
        "S2" to "Sabado"
    )

    val daysOfWeekLetters = listOf("D", "S", "T", "Q1", "Q2", "F", "S2")

    Surface(
        modifier = Modifier.fillMaxSize()

    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(color = Color(0xFF3E9C81)),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Horários", textAlign = TextAlign.Center,
                        color = Color.White,
                         fontWeight = FontWeight.Bold,
                        fontSize = 18.sp)
                }

            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .background(color = Color(0xFFFFFFFFF))
                    .clip(RoundedCornerShape(16.dp))
                ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ){
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(text = "Horários Disponiveis", fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF3E9C81)
                        )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        daysOfWeekLetters.forEach { dayLetter ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .border(1.dp, Color(0xFF3E9C81), CircleShape)
                                    .clickable {
                                        selectedDays = if (selectedDays.contains(dayLetter)) {
                                            selectedDays.minus(dayLetter)
                                        } else {
                                            selectedDays.plus(dayLetter)
                                        }
                                    }
                                    .background(
                                        if (selectedDays.contains(dayLetter)) Color(0xFF3E9C81) else Color.Transparent,
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dayLetter,
                                    color = if (selectedDays.contains(dayLetter)) Color.White else Color(
                                        0xFF3E9C81
                                    ),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))





                }



            }
        }


    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewDisponibilidadeCliente() {

    //val navController = rememberNavController()
    DisponibilidadeCliente()//(controleDeNavegacao = navController)
}