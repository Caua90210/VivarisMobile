import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.model.DisponibilidadePsicologo
import br.senai.sp.jandira.vivaris.model.DisponibilidadeResponse
import br.senai.sp.jandira.vivaris.service.DisponibilidadeService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun DisponibilidadeScreenV3(controleDeNavegacao: NavHostController, idPsicologo: Int) {
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
        "T" to "Terca",
        "Q1" to "Quarta",
        "Q2" to "Quinta",
        "F" to "Sexta",
        "S2" to "Sabado"
    )

    val daysOfWeekLetters = listOf("D", "S", "T", "Q1", "Q2", "F", "S2")

    // Log do ID do psicólogo
    Log.d("DisponibilidadeScreen", "ID do Psicólogo: $idPsicologo")

    Column(
        modifier = Modifier.fillMaxSize()
            .background(color = Color(0xF1F1F1F1))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF3E9C81))
        ) {
            Text(text = "Horário", textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(650.dp)
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Dias Disponíveis", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF296856))

            Spacer(modifier = Modifier.height(16.dp))

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
                            color = if (selectedDays.contains(dayLetter)) Color.White else Color(0xFF3E9C81),
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
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
                    .padding(horizontal = 20.dp)
            ) {
                Text(text = "Adicionar Horário", color = Color.White, fontSize = 16.sp)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(200.dp))

            Button(
                onClick = {
                    if (selectedDays.isEmpty()) {
                        Toast.makeText(context, "Selecione ao menos um dia", Toast.LENGTH_SHORT).show()
                    } else {
                        val disponibilidades = mutableListOf<Disponibilidade>()

                        // Adicionar disponibilidade para os horários da manhã
                        morningTimes.forEach { timeRange ->
                            val (start, end) = timeRange.split(" - ")
                            if (start.isNotEmpty() && end.isNotEmpty()) {
                                selectedDays.forEach { dayLetter ->
                                    val diaCompleto = daysOfWeekMap[dayLetter]
                                    if (diaCompleto != null) {
                                        val disponibilidade = Disponibilidade(
                                            dia_semana = diaCompleto,
                                            horario_inicio = start,
                                            horario_fim = end
                                        )
                                        disponibilidades.add(disponibilidade)

                                        Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                    }
                                }
                            } else {
                                Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                            }
                        }

                        // Repetir o mesmo para afternoonTimes e nightTimes...
                        afternoonTimes.forEach { timeRange ->
                            val (start, end) = timeRange.split(" - ")
                            if (start.isNotEmpty() && end.isNotEmpty()) {
                                selectedDays.forEach { dayLetter ->
                                    val diaCompleto = daysOfWeekMap[dayLetter]
                                    if (diaCompleto != null) {
                                        val disponibilidade = Disponibilidade(
                                            dia_semana = diaCompleto,
                                            horario_inicio = start,
                                            horario_fim = end
                                        )
                                        disponibilidades.add(disponibilidade)

                                        Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                    }
                                }
                            } else {
                                Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                            }
                        }

                        // Repetir para os horários da noite
                        nightTimes.forEach { timeRange ->
                            val (start, end) = timeRange.split(" - ")
                            if (start.isNotEmpty() && end.isNotEmpty()) {
                                selectedDays.forEach { dayLetter ->
                                    val diaCompleto = daysOfWeekMap[dayLetter]
                                    if (diaCompleto != null) {
                                        val disponibilidade = Disponibilidade(
                                            dia_semana = diaCompleto,
                                            horario_inicio = start,
                                            horario_fim = end
                                        )
                                        disponibilidades.add(disponibilidade)

                                        Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                    }
                                }
                            } else {
                                Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                            }
                        }

                        disponibilidades.forEach { disponibilidade ->
                            cadastrarDisponibilidade(disponibilidade, context, disponibilidadeService, idPsicologo)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B6A4)),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 42.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp)
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
}

fun cadastrarDisponibilidade(
    disponibilidade: Disponibilidade,
    context: android.content.Context,
    disponibilidadeService: DisponibilidadeService,
    idPsicologo: Int
) {
    Log.d("DisponibilidadeService", "ID do Psicólogo: $idPsicologo")
    Log.d("DisponibilidadeService", "Dados da Disponibilidade: $disponibilidade")

    // Chamada para cadastrar a disponibilidade
    disponibilidadeService.cadastrarDisponibilidade(disponibilidade).enqueue(object : Callback<DisponibilidadeResponse> {
        override fun onResponse(
            call: Call<DisponibilidadeResponse>,
            response: Response<DisponibilidadeResponse>
        ) {
            if (response.isSuccessful) {
                val disponibilidadeCadastrada = response.body()

                // Log e Toast para notificar o sucesso
                Log.d("DisponibilidadeService", "Disponibilidade cadastrada com sucesso: $disponibilidadeCadastrada")
                Toast.makeText(context, "Disponibilidade cadastrada com sucesso", Toast.LENGTH_SHORT).show()

                // Verificar se a disponibilidade foi cadastrada corretamente antes de continuar
                disponibilidadeCadastrada?.let {
                   val id = it.data.id
                        // Chamar a função para cadastrar a relação do psicólogo com a disponibilidade
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
                // Log e Toast para notificar o erro
                Log.e("DisponibilidadeService", "Erro ao cadastrar disponibilidade: ${response.errorBody()?.string()}")
                Toast.makeText(context, "Erro ao cadastrar disponibilidade: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }

        override fun onFailure(call: Call<DisponibilidadeResponse>, t: Throwable) {
            // Log e Toast para notificar a falha
            Log.e("DisponibilidadeService", "Falha ao cadastrar disponibilidade: ${t.message}")
            Toast.makeText(context, "Falha ao cadastrar disponibilidade: ${t.message}", Toast.LENGTH_SHORT).show()
        }
    })

}

// Função para associar o psicólogo à disponibilidade
fun cadastrarDisponibilidadePsicologo(
    disponibilidadeId: Int,
    idPsicologo: Int,
    disponibilidadeService: DisponibilidadeService,
    context: android.content.Context
) {
    // Verificar os parâmetros recebidos
    Log.d("CadastrarDisponibilidade", "disponibilidadeId: $disponibilidadeId, idPsicologo: $idPsicologo")

    // Criar objeto DisponibilidadePsicologo
    val disponibilidadePsicologo = DisponibilidadePsicologo(
        disponibilidade = disponibilidadeId,
        id_psicologo = idPsicologo,
        status = "Livre" // ou outro status, se necessário
    )

    // Chamar o serviço para associar o psicólogo à disponibilidade
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


@Composable
fun DisponibilidadeHorarioSection(title: String, times: List<String>) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        times.forEach { time ->
            Text(text = time, fontSize = 16.sp)
        }
    }
}

@Composable
fun AddHorarioDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var horarioInicio by remember { mutableStateOf("") }
    var horarioFim by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Adicionar Horário") },
        text = {
            Column {
                TextField(
                    value = horarioInicio,
                    onValueChange = { horarioInicio = it },
                    label = { Text("Horário Início (HH:mm)") }
                )
                TextField(
                    value = horarioFim,
                    onValueChange = { horarioFim = it },
                    label = { Text("Horário Fim (HH:mm)") }
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(horarioInicio, horarioFim) }) {
                Text("Adicionar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
