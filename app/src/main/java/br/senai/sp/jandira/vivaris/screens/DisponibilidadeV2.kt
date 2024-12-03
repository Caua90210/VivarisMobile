package br.senai.sp.jandira.vivaris.screens

import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.util.Log
import android.widget.Toast
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
import br.senai.sp.jandira.vivaris.model.DisponibilidadePsicologo
import br.senai.sp.jandira.vivaris.model.DisponibilidadeResponse
import br.senai.sp.jandira.vivaris.service.DisponibilidadeService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val diasSemana = listOf("Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado")

@SuppressLint("UnrememberedMutableState")
@Composable
fun DisponibilidadeScreenV4(controleDeNavegacao: NavHostController, idPsicologo: Int) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showAddTimeDialog by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var disponibilidades by remember { mutableStateOf(mutableListOf<Disponibilidade>()) }

    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)
    val disponibilidadeService = retrofitFactory.getDisponibilidadeService()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(color = Color(0xFFF1F1F1))
    ) {
        Text(
            text = "Selecione uma data:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF296856)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CalendarView(selectedDate = mutableStateOf(selectedDate)) { date ->
            selectedDate = date
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Disponibilidades para ${selectedDate}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        DisponibilidadeList(disponibilidades)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showAddTimeDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Adicionar Disponibilidade")
        }

        if (showAddTimeDialog) {
            AddTimeDialog(
                onDismiss = { showAddTimeDialog = false },
                onAddTime = { start, end ->
                    if (start.isNotEmpty() && end.isNotEmpty()) {
                        val newDisponibilidade = Disponibilidade(
                            dia_semana = diasSemana[selectedDate.dayOfWeek.value % 7],
                            horario_inicio = start,
                            horario_fim = end
                        )
                        disponibilidades.add(newDisponibilidade)
                        Log.d("DisponibilidadeScreen", "Disponibilidade adicionada: $newDisponibilidade")
                        cadastrarDisponibilidade(newDisponibilidade, context, disponibilidadeService, idPsicologo)
                    }
                    showAddTimeDialog = false
                }
            )
        }
    }
}

@Composable
fun CalendarView(selectedDate: MutableState<LocalDate>, onDateSelected: (LocalDate) -> Unit) {
    val currentMonth = selectedDate.value .month
    val currentYear = selectedDate.value.year
    val firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1)
    val lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth())

    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
    val totalDays = lastDayOfMonth.dayOfMonth

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                selectedDate.value = selectedDate.value.minusMonths(1)
            }) {
                Text(text = "<", fontSize = 24.sp)
            }

            Text(
                text = "${currentMonth.name} $currentYear",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = {
                selectedDate.value = selectedDate.value.plusMonths(1)
            }) {
                Text(text = ">", fontSize = 24.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            diasSemana.forEach { dia ->
                Text(
                    text = dia.take(3),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }

        Column {
            for (i in 0 until firstDayOfWeek) {
                Spacer(modifier = Modifier.height(8.dp))
            }

            var day = 1
            while (day <= totalDays) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (i in 0 until 7) {
                        if (day <= totalDays) {
                            val date = LocalDate.of(currentYear, currentMonth, day)
                            val isSelected = date == selectedDate.value
                            Text(
                                text = day.toString(),
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(8.dp)
                                    .background(if (isSelected) Color(0xFF3E9C81) else Color.Transparent, shape = RoundedCornerShape(8.dp))
                                    .clickable { onDateSelected(date) },
                                color = if (isSelected) Color.White else Color.Black,
                                textAlign = TextAlign.Center
                            )
                            day++
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DisponibilidadeList(disponibilidades: List<Disponibilidade>) {
    Column {
        if (disponibilidades.isEmpty()) {
            Text(text = "Nenhuma disponibilidade cadastrada.", fontSize = 16.sp)
        } else {
            disponibilidades.forEach { disponibilidade ->
                Text(
                    text = "${disponibilidade.dia_semana}: ${disponibilidade.horario_inicio} - ${disponibilidade.horario_fim}",
                    modifier = Modifier.padding(8.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun AddTimeDialog(onDismiss: () -> Unit, onAddTime: (String, String) -> Unit) {
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFim by remember { mutableStateOf(false) }

    if (showTimePickerInicio) {
        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                startTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, 0)
                showTimePickerInicio = false
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            true
        ).show()
    }

    if (showTimePickerFim) {
        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                endTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, 0)
                showTimePickerFim = false
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            true
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Adicionar Horário") },
        text = {
            Column {
                Button(onClick = { showTimePickerInicio = true }) {
                    Text(text = if (startTime.isEmpty()) "Selecionar Horário Início" else startTime)
                }
                Spacer(modifier = Modifier .height(8.dp))
                Button(onClick = { showTimePickerFim = true }) {
                    Text(text = if (endTime.isEmpty()) "Selecionar Horário Fim" else endTime)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAddTime(startTime, endTime)
                onDismiss()
            }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

fun cadastrarDisponibilidade(
    disponibilidade: Disponibilidade,
    context: android.content.Context,
    disponibilidadeService: DisponibilidadeService,
    idPsicologo: Int
) {
    Log.d("DisponibilidadeService", "ID do Psicólogo: $idPsicologo")
    Log.d("DisponibilidadeService", "Dados da Disponibilidade: $disponibilidade")

    disponibilidadeService.cadastrarDisponibilidade(disponibilidade).enqueue(object :
        Callback<DisponibilidadeResponse> {
        override fun onResponse(
            call: Call<DisponibilidadeResponse>,
            response: Response<DisponibilidadeResponse>
        ) {
            if (response.isSuccessful) {
                val disponibilidadeCadastrada = response.body()
                Log.d("DisponibilidadeService", "Disponibilidade cadastrada com sucesso: $disponibilidadeCadastrada")
                Toast.makeText(context, "Disponibilidade cadastrada com sucesso", Toast.LENGTH_SHORT).show()

                disponibilidadeCadastrada?.let {
                    val id = it.data.id
                    if (id != null) {
                        cadastrarDisponibilidadePsicologo(
                            id,
                            idPsicologo,
                            disponibilidadeService,
                            context
                        )
                    }
                }
            } else {
                Log.e("DisponibilidadeService", "Erro ao cadastrar disponibilidade: ${response.errorBody()?.string()}")
                Toast.makeText(context, "Erro ao cadastrar disponibilidade: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<DisponibilidadeResponse>, t: Throwable) {
            Log.e("DisponibilidadeService", "Falha ao cadastrar disponibilidade: ${t.message}")
            Toast.makeText(context, "Falha ao cadastrar disponibilidade: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })
}

fun cadastrarDisponibilidadePsicologo(
    disponibilidadeId: Int,
    idPsicologo: Int,
    disponibilidadeService: DisponibilidadeService,
    context: android.content.Context
) {
    Log.d("CadastrarDisponibilidade", "disponibilidadeId: $disponibilidadeId, idPsicologo: $idPsicologo")

    val disponibilidadePsicologo = DisponibilidadePsicologo(
        disponibilidade = disponibilidadeId,
        id_psicologo = idPsicologo,
        status = "Livre"
    )

    disponibilidadeService.postDisponibilidadePsicologo(idPsicologo, disponibilidadePsicologo)
        .enqueue(object : Callback<DisponibilidadePsicologo> {
            override fun onResponse(call: Call<DisponibilidadePsicologo>, response: Response<DisponibilidadePsicologo>) {
                if (response.isSuccessful) {
                    Toast.makeText(context, "Disponibilidade associada com sucesso", Toast.LENGTH_SHORT).show()
                    Log.d("DisponibilidadePsicologo", "Associação realizada com sucesso: ${response.body()}")
                } else {
                    Log.e("DisponibilidadePsicologo", "Erro ao associar: ${response.code()} - ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Erro ao associar disponibilidade", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DisponibilidadePsicologo>, t: Throwable) {
                Log.e("DisponibilidadePsicologo", "Falha na associação: ${t.message}")
                Toast.makeText(context, "Falha na associação da disponibilidade", Toast.LENGTH_SHORT).show()
            }
        })
}

@Preview(showBackground = true)
@Composable
fun PreviewDisponibilidadeScreenV4() {
    val navController = rememberNavController()
    DisponibilidadeScreenV4(controleDeNavegacao = navController, idPsicologo = 1)
}