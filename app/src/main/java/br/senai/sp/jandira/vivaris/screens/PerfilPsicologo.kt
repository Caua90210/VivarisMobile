package br.senai.sp.jandira.vivaris.screens

import android.app.DatePickerDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.AgendamentoRequest
import br.senai.sp.jandira.vivaris.model.PsicologoResponsebyID
import br.senai.sp.jandira.vivaris.model.consultaResponse
import br.senai.sp.jandira.vivaris.model.session
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PerfilPsicologo(controleDeNavegacao: NavHostController, id: Int, isPsicologo: Boolean, idCliente: Int) {
    var psicologoResponse by remember { mutableStateOf<PsicologoResponsebyID?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedDate by remember { mutableStateOf("") }
    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)
    val scope = rememberCoroutineScope()

    Log.e("id do cliente", "$idCliente")

    LaunchedEffect(id) {
        val token = TokenRepository(context).getToken()
        if (token != null) {
            retrofitFactory.getPsicologoService().getPsicologById(id, token).enqueue(
                object : Callback<PsicologoResponsebyID> {
                    override fun onResponse(call: Call<PsicologoResponsebyID>, response: Response<PsicologoResponsebyID>) {
                        if (response.isSuccessful) {
                            psicologoResponse = response.body()
                        } else {
                            Log.e("API Error", "Erro ao carregar psicólogo: ${response.errorBody()?.string()}")
                            Toast.makeText(context, "Erro ao carregar psicólogo", Toast.LENGTH_SHORT).show()
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<PsicologoResponsebyID>, t: Throwable) {
                        Log.e("API Failure", "Falha na requisição: ${t.message}")
                        Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                        isLoading = false
                    }
                }
            )
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF52B693))
        }
    } else {
        psicologoResponse?.data?.professional?.let { psicologoData ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color(0xFFF0F0F0))
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { controleDeNavegacao.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Voltar", tint = Color(0xFF52B693))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Perfil Psicólogo", style = MaterialTheme.typography.titleLarge.copy(color = Color(0xFF52B693)))
                }

                Spacer(modifier = Modifier.height(16.dp))

                psicologoData.foto_perfil?.let { foto ->
                    Image(
                        painter = rememberAsyncImagePainter(foto),
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(60.dp))
                            .align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "${psicologoData.nome}",
                    style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF52B693)),
                    modifier = Modifier.padding(bottom = 4.dp),
                    fontSize = 24.sp
                )
                Text(text = "Email: ${psicologoData.email}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Telefone: ${psicologoData.telefone}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Data de Nascimento: ${formatDate(psicologoData.data_nascimento)}", style = MaterialTheme.typography.bodyMedium)

                Spacer(modifier = Modifier.height(16.dp))

                psicologoData.descricao?.let { descricao ->
                    Text(
                        text = "Descrição: $descricao",
                        style = MaterialTheme.typography.bodyMedium.copy(color = Color(0xFF333333)),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                if (psicologoData.tbl_psicologo_disponibilidade.isNotEmpty()) {
                    Text(
                        text = "Disponibilidades livres:",
                        style = MaterialTheme.typography.titleMedium.copy(color = Color(0xFF52B693)),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    psicologoData.tbl_psicologo_disponibilidade.forEach { disponibilidade ->
                        var showAgendarButton by remember { mutableStateOf(false) }

                        // Card para cada disponibilidade
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.White)
                                .shadow(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "${disponibilidade.tbl_disponibilidade.dia_semana} - ${disponibilidade.tbl_disponibilidade.horario_inicio} a ${disponibilidade.tbl_disponibilidade.horario_fim}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                Button(
                                    onClick = {
                                        selectedDate = "${disponibilidade.tbl_disponibilidade.dia_semana} ${disponibilidade.tbl_disponibilidade.horario_inicio}"
                                        showAgendarButton = true
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp)
                                        .clip(RoundedCornerShape(8.dp)),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B693))
                                ) {
                                    Text(text = "Selecionar", color = Color.White)
                                }
                            }
                        }

                        if (showAgendarButton) {
                            Button(
                                onClick = {
                                    val datePickerDialog = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            // Formatar a data selecionada
                                            val selectedDay = dayOfMonth.toString().padStart(2, '0')
                                            val selectedMonth = (month + 1).toString().padStart(2, '0') // Mês começa em 0
                                            val selectedYear = year.toString()

                                            // Combinar a data selecionada com o horário de início da disponibilidade
                                            val selectedDateTime = "$selectedYear-$selectedMonth-$selectedDay ${disponibilidade.tbl_disponibilidade.horario_inicio}"

                                            // Criar o objeto de requisição
                                            val request = AgendamentoRequest(
                                                id_psicologo = id,
                                                id_cliente = idCliente,
                                                data_consulta = selectedDateTime // Usar a data formatada
                                            )




                                            val token = TokenRepository(context).getToken() // Obtenha o token

                                            if (token != null) {
                                                retrofitFactory.getConsultaService().agendarConsulta(request, token).enqueue(object : Callback<consultaResponse> {

                                                    override fun onResponse(
                                                        p0: Call<consultaResponse>,
                                                        response: Response<consultaResponse>
                                                    ) {

                                                        if (response.isSuccessful) {
                                                            Toast.makeText(context, "Consulta agendada com sucesso!", Toast.LENGTH_SHORT).show()
                                                            Log.d("Dados", "${response.body()?.data}")
                                                            val requestSession = response.body()?.data?.id?.let {
                                                                session(
                                                                    id_consulta = it,
                                                                    id_cliente = idCliente
                                                                )
                                                            }
                                                            if (requestSession != null) {
                                                                retrofitFactory.getConsultaService().criarSession(token, requestSession).enqueue(object : Callback<session>{
                                                                    override fun onResponse(
                                                                        p0: Call<session>,
                                                                        response: Response<session>
                                                                    ) {
                                                                        if(response.isSuccessful){
                                                                            Log.d("Teste session", "${response.body()?.id_consulta}")
                                                                        }else{
                                                                            val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                                                                            Log.e("Erro", "Erro: $errorMessage")
                                                                        }
                                                                    }

                                                                    override fun onFailure(
                                                                        p0: Call<session>,
                                                                        erro: Throwable
                                                                    ) {
                                                                        Log.e("API Failure", "Falha na requisição: ${erro.message}")
                                                                    }

                                                                })
                                                            }
                                                        } else {
                                                            val errorMessage = response.errorBody()?.string() ?: "Erro desconhecido"
                                                            Toast.makeText(context, "Erro ao agendar consulta: $errorMessage", Toast.LENGTH_SHORT).show()
                                                            Log.d("Erro", "Erro: $errorMessage")
                                                        }
                                                    }

                                                    override fun onFailure(
                                                        p0: Call<consultaResponse>,
                                                        erro: Throwable
                                                    ) {
                                                        Toast.makeText(context, "Erro: ${erro.message}", Toast.LENGTH_SHORT).show()
                                                        Log.e("API Failure", "Falha na requisição: ${erro.message}")
                                                    }
                                                })
                                            } else {
                                                Toast.makeText(context, "Token não encontrado", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH),
                                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                                    )
                                    datePickerDialog.show()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF52B693))
                            ) {
                                Text(text = "Selecionar Data", color = Color.White)
                            }
                        }
                    }
                } else {
                    Text(text = "Não há disponibilidades cadastradas.", style = MaterialTheme.typography.bodyMedium)
                }
            } ?: run {
                Text(text = "Erro ao carregar as informações do psicólogo", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

// Função para formatar a data
fun formatDate(dateString: String?): String {
    return if (dateString != null) {
        try {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = format.parse(dateString)
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            outputFormat.format(date)
        } catch (e: Exception) {
            dateString
        }
    } else {
        "Data não disponível"
    }}