import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.service.DisponibilidadeService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DisponibilidadeScreenV3(controleDeNavegacao: NavHostController) {
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var morningTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var afternoonTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var nightTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var showAddTimeDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory()
    val disponibilidadeService = retrofitFactory.getDisponibilidadeService()

    // Mapeamento dos dias da semana
    val daysOfWeekMap = mapOf(
        "D" to "Domingo",
        "S" to "Segunda",
        "T" to "Terça",
        "Q1" to "Quarta",
        "Q2" to "Quinta",
        "F" to "Sexta",
        "S2" to "Sábado"
    )

    val daysOfWeekLetters = listOf("D", "S", "T", "Q1", "Q2", "F", "S2")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(Color(0xFF3E9C81))
    ) {
        Text(text = "Horário", textAlign = TextAlign.Center, modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold )
    }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
           //.background(Color(0xFFFFFFFF))
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top

    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Text(text = "Dias Disponíveis", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF296856))

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xFF))

            ) {
                daysOfWeekLetters.forEach { dayLetter ->
                    Button(
                        onClick = {
                            if (selectedDays.contains(dayLetter)) {
                                selectedDays = selectedDays.minus(dayLetter)
                            } else {
                                selectedDays = selectedDays.plus(dayLetter)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedDays.contains(dayLetter)) Color(0xFF52B6A4) else Color.Transparent
                        ),

                        ) {
                        Text(text = dayLetter)
                    }
                }
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Seções de horários
        DisponibilidadeHorarioSection("Manhã", morningTimes)
        DisponibilidadeHorarioSection("Tarde", afternoonTimes)
        DisponibilidadeHorarioSection("Noite", nightTimes)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showAddTimeDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B6A4)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Adicionar Horário", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedDays.isEmpty()) {
                    Toast.makeText(context, "Selecione ao menos um dia", Toast.LENGTH_SHORT).show()
                } else {
                    val selectedDaysFullNames = selectedDays.map { dayLetter ->
                        daysOfWeekMap[dayLetter]
                    }.joinToString(",")

                    val disponibilidades = mutableListOf<Disponibilidade>()
                    morningTimes.forEach { timeRange ->
                        val (start, end) = timeRange.split(" - ")
                        disponibilidades.add(Disponibilidade(selectedDaysFullNames, start, end))
                    }
                    afternoonTimes.forEach { timeRange ->
                        val (start, end) = timeRange.split(" - ")
                        disponibilidades.add(Disponibilidade(selectedDaysFullNames, start, end))
                    }
                    nightTimes.forEach { timeRange ->
                        val (start, end) = timeRange.split(" - ")
                        disponibilidades.add(Disponibilidade(selectedDaysFullNames, start, end))
                    }

                    disponibilidades.forEach { disponibilidade ->
                        cadastrarDisponibilidade(disponibilidade, context, disponibilidadeService)
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B6A4)),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(text = "Confirmar disponibilidade", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }

    if (showAddTimeDialog) {
        AddHorarioDialog(
            onDismiss = { showAddTimeDialog = false },
            onConfirm = { horarioInicio, horarioFim ->
                showAddTimeDialog = false

                // Converte as strings de horário para LocalTime
                val startTime = LocalTime.parse(horarioInicio, DateTimeFormatter.ofPattern("HH:mm"))
                val endTime = LocalTime.parse(horarioFim, DateTimeFormatter.ofPattern("HH:mm"))

                // Defina o formato desejado para "H:mm:ss"
                val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

                // Obtém a hora inicial
                val startHour = startTime.hour

                when {
                    startHour in 6..11 -> {
                        morningTimes.add("${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}")
                    }
                    startHour in 12..17 -> {
                        afternoonTimes.add("${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}")
                    }
                    startHour in 18..23 -> {
                        nightTimes.add("${startTime.format(timeFormatter)} - ${endTime.format(timeFormatter)}")
                    }
                    else -> Toast.makeText(context, "Horário inválido", Toast.LENGTH_SHORT).show()
                }
            }
        )
    }
}

fun cadastrarDisponibilidade(disponibilidade: Disponibilidade, context: android.content.Context, disponibilidadeService: DisponibilidadeService) {
    disponibilidadeService.cadastrarDisponibilidade(disponibilidade).enqueue(object : Callback<Disponibilidade> {
        override fun onResponse(call: Call<Disponibilidade>, response: Response<Disponibilidade>) {
            if (response.isSuccessful) {
                Toast.makeText(context, "Disponibilidade cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Erro ao cadastrar disponibilidade: ${response.code()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<Disponibilidade>, t: Throwable) {
            Toast.makeText(context, "Erro: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    })
}

@Composable
fun AddHorarioDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var horarioInicio by remember { mutableStateOf("") }
    var horarioFim by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Adicionar Horário") },
        text = {
            Column {
                TextField(
                    value = horarioInicio,
                    onValueChange = { horarioInicio = it },
                    label = { Text("Horário de Início (HH:mm)") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = horarioFim,
                    onValueChange = { horarioFim = it },
                    label = { Text("Horário de Fim (HH:mm)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(horarioInicio, horarioFim) }) {
                Text("Criar Horário")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Fechar")
            }
        }
    )
}

@Composable
fun DisponibilidadeHorarioSection(periodo: String, times: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {

        Text(text = periodo, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(
            0xFF3E9C81
        )
        )

        if (times.isEmpty()) {
            Text(text = "Nenhum horário disponível", fontSize = 16.sp, color = Color.Gray)
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                times.forEach { time ->
                    Card(
                        modifier = Modifier.padding(4.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(Color(0xFFE3F2FD)),
                    ) {
                        Text(
                            text = time,
                            modifier = Modifier.padding(8.dp),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDisponibilidadeScreenV3() {
    DisponibilidadeScreenV3(controleDeNavegacao = rememberNavController())
}


