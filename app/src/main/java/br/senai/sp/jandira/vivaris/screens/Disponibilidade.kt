import android.app.TimePickerDialog
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.model.DiaSemanaRequest
import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.model.DisponibilidadeData
import br.senai.sp.jandira.vivaris.model.DisponibilidadeInfo
import br.senai.sp.jandira.vivaris.model.DisponibilidadePsicologo
import br.senai.sp.jandira.vivaris.model.DisponibilidadeResponse
import br.senai.sp.jandira.vivaris.model.PsicologoDisponibilidadeResponse
import br.senai.sp.jandira.vivaris.service.DisponibilidadeService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter





@Composable
fun DisponibilidadeScreenV3(controleDeNavegacao: NavHostController, idPsicologo: Int) {
    var selectedDays by remember { mutableStateOf(setOf<String>()) }
    var morningTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var afternoonTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var nightTimes by remember { mutableStateOf(mutableListOf<String>()) }
    var showAddTimeDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedDisponibilidade by remember { mutableStateOf<Disponibilidade?>(null) }
    var showDetailsDialog by remember { mutableStateOf(false) }
    var idsDisponibilidades by remember { mutableStateOf<List<Int>>(emptyList()) }

    var newDisponibilidades by remember { mutableStateOf<List<Disponibilidade>>(emptyList()) }

    var disponibilidadesDetalhadas by remember { mutableStateOf(mutableListOf<DisponibilidadeData>()) }

    var existingDisponibilidades by remember { mutableStateOf<Set<Disponibilidade>>(emptySet()) }


    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)
    val disponibilidadeService = retrofitFactory.getDisponibilidadeService()



    
    fun formatarHorario(horario: String): String {
        // Remover o 'Z' e substituir o espaço por 'T' para compatibilidade com LocalDateTime
        val adjustedHorario = horario.replace("Z", "").replace(" ", "T")

        // Agora, analisamos a string com o formato correto
        val dateTime = LocalDateTime.parse(adjustedHorario, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS"))

        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

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


    // Variável para armazenar as disponibilidades
    var disponibilidades by remember { mutableStateOf<List<Disponibilidade>>(emptyList()) }

    // Função para buscar detalhes das disponibilidades
    fun fetchDetalhesDisponibilidades(ids: List<Int?>) {
        ids.forEach { id ->
            if (id != null) {
                disponibilidadeService.getDisponibilidadebyId(id).enqueue(object : Callback<DisponibilidadeInfo> {
                    override fun onResponse(call: Call<DisponibilidadeInfo>, response: Response<DisponibilidadeInfo>) {
                        if (response.isSuccessful) {
                            response.body()?.data?.forEach { disponibilidade ->
                                val horarioInicioFormatado = formatarHorario(disponibilidade.horario_inicio)
                                val horarioFimFormatado = formatarHorario(disponibilidade.horario_fim)
                                val timeOfDay = determineTimeOfDay(horarioInicioFormatado)
                                val timeRange = "$horarioInicioFormatado - $horarioFimFormatado"

                                // Adiciona os horários às listas correspondentes
                                when (timeOfDay) {
                                    "Manhã" -> morningTimes.add(timeRange)
                                    "Tarde" -> afternoonTimes.add(timeRange)
                                    "Noite" -> nightTimes.add(timeRange)
                                }
                            }
                        }
                    }

                    override fun onFailure(call: Call<DisponibilidadeInfo>, t: Throwable) {
                        Log.e("DisponibilidadeScreen", "Falha ao buscar detalhes da disponibilidade: ${t.message}")
                    }
                })
            }
        }
    }


    fun fetchDisponibilidades() {
        disponibilidadeService.getDisponibilidadePsicologoById(idPsicologo).enqueue(object : Callback<PsicologoDisponibilidadeResponse> {
            override fun onResponse(call: Call<PsicologoDisponibilidadeResponse>, response: Response<PsicologoDisponibilidadeResponse>) {
                if (response.isSuccessful) {
                    val psicologoResponse = response.body()
                    psicologoResponse?.let {

                        disponibilidades = it.data.disponibilidades
                        existingDisponibilidades =
                            disponibilidades.toSet()
                        Log.d("DisponibilidadeScreen", "IDs das Disponibilidades recebidos: ${disponibilidades.map { it.id }}")
                        fetchDetalhesDisponibilidades(disponibilidades.map { it.id })
                    }
                } else {
                    Log.e("DisponibilidadeScreen", "Erro ao buscar disponibilidades: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PsicologoDisponibilidadeResponse>, t: Throwable) {
                Log.e("DisponibilidadeScreen", "Falha ao buscar disponibilidades: ${t.message}")
            }
        })
    }


    LaunchedEffect(idPsicologo) {
        fetchDisponibilidades()
    }


    fun deletarDisponibilidade(disponibilidadeId: Int, diaSemana: String) {

        val diaSemanaRequest = DiaSemanaRequest(diaSemana)


        disponibilidadeService.deleteDisponibilidade(idPsicologo, diaSemanaRequest).enqueue(object : Callback<DisponibilidadePsicologo> {
            override fun onResponse(call: Call<DisponibilidadePsicologo>, response: Response<DisponibilidadePsicologo>) {
                if (response.isSuccessful) {
                    Log.d("DisponibilidadeService", "Disponibilidade deletada com sucesso: $disponibilidadeId")

                    fetchDisponibilidades()
                    Toast.makeText(context, "Disponibilidade deletada com sucesso", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e("DisponibilidadeService", "Erro ao deletar disponibilidade: ${response.errorBody()?.string()}")
                    Toast.makeText(context, "Erro ao deletar disponibilidade", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DisponibilidadePsicologo>, t: Throwable) {
                Log.e("DisponibilidadeService", "Falha ao deletar disponibilidade: ${t.message}")
                Toast.makeText(context, "Falha ao deletar disponibilidade", Toast.LENGTH_SHORT).show()
            }
        })
    }



    Log.d("DisponibilidadeScreen", "ID do Psicólogo: $idPsicologo")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xF1F1F1F1))
    ) {

//        if (disponibilidades.isNotEmpty()) {
////            Text(
////                text = "Disponibilidades Cadastradas",
////                fontSize = 24.sp,
////                fontWeight = FontWeight.Bold,
////                color = Color(0xFF296856)
////            )
//
////            LazyColumn {
////                items(disponibilidades) { disponibilidade ->
////                    DisponibilidadeItem(disponibilidade) {
////                        selectedDisponibilidade = disponibilidade
////                        showDetailsDialog = true
////                    }
////                }
////            }
//        } else {
//            Text(text = "Nenhuma disponibilidade cadastrada", fontSize = 16.sp)
//        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(Color(0xFF3E9C81))
        ) {
            Spacer(modifier = Modifier.padding(bottom = 20.dp))
            Text(text = "Horário", textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp), color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(40.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(2000.dp)
                    .padding(bottom = 80.dp)
                    .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(16.dp)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Dias Disponíveis",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF296856)
                )

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
                                color = if (selectedDays.contains(dayLetter)) Color.White else Color(
                                    0xFF3E9C81
                                ),
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))



                DisponibilidadeHorarioSection("Manhã", morningTimes, onClick = {
                    selectedDisponibilidade = Disponibilidade(
                        dia_semana = "Manhã",
                        horario_inicio = it.split(" - ")[0],
                        horario_fim = it.split(" - ")[1]
                    )
                    showDetailsDialog = true
                })
                DisponibilidadeHorarioSection("Tarde", afternoonTimes, onClick = {
                    selectedDisponibilidade = Disponibilidade(
                        dia_semana = "Tarde",
                        horario_inicio = it.split(" - ")[0],
                        horario_fim = it.split(" - ")[1]
                    )
                    showDetailsDialog = true
                })
                DisponibilidadeHorarioSection("Noite", nightTimes, onClick = {
                    selectedDisponibilidade = Disponibilidade(
                        dia_semana = "Noite",
                        horario_inicio = it.split(" - ")[0],
                        horario_fim = it.split(" - ")[1]
                    )
                    showDetailsDialog = true
                })

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

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = {
                        if (selectedDays.isEmpty()) {
                            Toast.makeText(context, "Selecione ao menos um dia", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            val disponibilidades = mutableListOf<Disponibilidade>()


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

                                            if (existingDisponibilidades.none { it.id == disponibilidade.id }) {
                                                disponibilidades.add(disponibilidade)
                                                Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                            } else {
                                                Log.e("DisponibilidadeScreen", "Disponibilidade já existe: $disponibilidade")
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                                }
                            }


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



                                            if (existingDisponibilidades.none { it.id == disponibilidade.id }) {
                                                disponibilidades.add(disponibilidade)
                                                Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                            } else {
                                                Log.e("DisponibilidadeScreen", "Disponibilidade já existe: $disponibilidade")
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                                }
                            }


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

                                            if (existingDisponibilidades.none { it.id == disponibilidade.id }) {
                                                disponibilidades.add(disponibilidade)
                                                Log.d("DisponibilidadeScreen", "Criando Disponibilidade: $disponibilidade")
                                            } else {
                                                Log.e("DisponibilidadeScreen", "Disponibilidade já existe: $disponibilidade")
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("DisponibilidadeScreen", "Horário inválido: $timeRange")
                                }
                            }

                            disponibilidades.forEach { disponibilidade ->
                                cadastrarDisponibilidade(
                                    disponibilidade,
                                    context,
                                    disponibilidadeService,
                                    idPsicologo
                                )
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B6A4)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .align(Alignment.CenterHorizontally)

                ) {
                    Text(text = "Confirmar Disponibilidade", color = Color.White, fontSize = 16.sp)
                }


            }



        if (showAddTimeDialog) {
            AddHTimeDialog(
                onDismiss = { showAddTimeDialog = false },
                onAddTime = { startTime, endTime, timeOfDay ->
                    try {
                        if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                            when (timeOfDay) {
                                "Manhã" -> morningTimes.add("$startTime - $endTime")
                                "Tarde" -> afternoonTimes.add("$startTime - $endTime")
                                "Noite" -> nightTimes.add("$startTime - $endTime")
                            }
                            Log.d("DisponibilidadeScreen", "Horário adicionado: $startTime - $endTime em $timeOfDay")
                        } else {
                            Log.e("DisponibilidadeScreen", "Horário inválido: $startTime - $endTime")
                        }
                    } catch (e: Exception) {
                        Log.e("DisponibilidadeScreen", "Erro ao adicionar horário: ${e.message}")
                    }
                    showAddTimeDialog = false
                }

            )
        }






        if (showDetailsDialog && selectedDisponibilidade != null) {
            AlertDialog(
                onDismissRequest = { showDetailsDialog = false },
                title = { Text(text = "Disponibilidade Cadastrada") },
                text = {
                    Text(text = "Dia: ${selectedDisponibilidade!!.dia_semana}\n" +
                            "Horário: ${selectedDisponibilidade!!.horario_inicio} - ${selectedDisponibilidade!!.horario_fim}")
                },
                confirmButton = {
                    Row {

                        TextButton(onClick = { showDetailsDialog = false }) {
                            Text("CANCELAR")
                        }

                        TextButton(onClick = {

                        }) {
                            Text(text = "Deletar")
                        }

                    }
                }
            )
        }
    }
}


@Composable
fun DisponibilidadeItem(disponibilidade: Disponibilidade, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
            .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Dia: ${disponibilidade.dia_semana}", fontWeight = FontWeight.Bold)
            Text(text = "Horário: ${disponibilidade.horario_inicio} - ${disponibilidade.horario_fim}")
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





    disponibilidadeService.cadastrarDisponibilidade(disponibilidade).enqueue(object : Callback<DisponibilidadeResponse> {
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


@Composable
fun DisponibilidadeHorarioSection(title: String, times: List<String>, onClick: (String) -> Unit) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3E9C81)
        )

        Spacer(modifier = Modifier.height(8.dp))

        times.forEach { time ->
            Text(
                text = time,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(Color(0xFFE0E0E0), shape = RoundedCornerShape(8.dp))
                    .clickable { onClick(time) },
                textAlign = TextAlign.Center,
                fontSize = 16.sp
            )
        }
    }
}

@Composable
fun AddHTimeDialog(
    onDismiss: () -> Unit,
    onAddTime: (String, String, String) -> Unit
) {
    var horarioInicio by remember { mutableStateOf("") }
    var horarioFim by remember { mutableStateOf("") }
    var timeOfDay by remember { mutableStateOf("") }
    var showTimePickerInicio by remember { mutableStateOf(false) }
    var showTimePickerFim by remember { mutableStateOf(false) }

    if (showTimePickerInicio) {
        TimePickerDialog(
            LocalContext.current,
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, 0)
                horarioInicio = formattedTime
                timeOfDay = determineTimeOfDay(formattedTime)
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
                val formattedTime = String.format("%02d:%02d:%02d", selectedHour, selectedMinute, 0)
                horarioFim = formattedTime
                showTimePickerFim = false
            },
            LocalTime.now().hour,
            LocalTime.now().minute,
            true
        ).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Adicionar Horário", color = Color(0xFF3E9C81)) },
        text = {
            Column {
                Button(onClick = { showTimePickerInicio = true }) {
                    Text(text = if (horarioInicio.isEmpty()) "Selecionar Horário Início" else horarioInicio)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = { showTimePickerFim = true }) {
                    Text(text = if (horarioFim.isEmpty()) "Selecionar Horário Fim" else horarioFim)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onAddTime(horarioInicio, horarioFim, timeOfDay)
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

fun determineTimeOfDay(startTime: String): String {
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss") 
    val start = LocalTime.parse(startTime, timeFormatter)

    return when {
        start.isBefore(LocalTime.of(12, 0)) -> "Manhã"
        start.isBefore(LocalTime.of(18, 0)) -> "Tarde"
        else -> "Noite"
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
fun PreviewDisponibilidadeScreenV3() {
    val navController = rememberNavController()
    DisponibilidadeScreenV3(controleDeNavegacao = navController, idPsicologo = 1)
}

