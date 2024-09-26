package br.senai.sp.jandira.vivaris

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.service.DisponibilidadeService
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun DisponibilidadeScreen(controleDeNavegacao: NavHostController) {
    var diaSemana by remember { mutableStateOf("") }
    var horarioInicio by remember { mutableStateOf("") }
    var horarioFim by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }


    val retrofitFactory = RetrofitFactory()
    val context = LocalContext.current
    val disponibilidadeService = retrofitFactory.getDisponibilidadeService()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = diaSemana,
            onValueChange = { diaSemana = it },
            label = { Text("Dia da Semana") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = horarioInicio,
            onValueChange = { horarioInicio = it },
            label = { Text("Horário de Início (HH:mm:ss)") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = horarioFim,
            onValueChange = { horarioFim = it },
            label = { Text("Horário de Fim (HH:mm:ss)") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        val coroutineScope = rememberCoroutineScope()
        Button(
            onClick = {


                if (diaSemana.isBlank() || horarioInicio.isBlank() || horarioFim.isBlank()) {
                    Toast.makeText(context, "Todos os campos são obrigatórios", Toast.LENGTH_SHORT).show()
                    return@Button
                }


                val disponibilidade = Disponibilidade(
                    dia_semana = diaSemana,
                    horario_inicio = horarioInicio,
                    horario_fim = horarioFim
                )

                Log.d("CADASTRO DISPONIBILIDADE", "Dados que foram enviados:  ${disponibilidade}" )

                coroutineScope.launch {
                    disponibilidadeService.cadastrarDisponibilidade(disponibilidade).enqueue(object : Callback<Disponibilidade>{
                        override fun onResponse(
                            call: Call<Disponibilidade>,
                            response: Response<Disponibilidade>
                        ) {
                            if(response.isSuccessful){
                                Toast.makeText(context, "Disponibilidade cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Erro ao cadastrar disponibilidade ${response.code()}", Toast.LENGTH_SHORT).show()
                                Log.e("Cadastro disponibilidade ", "Erro ao cadastar: ${response.errorBody()?.string()}")
                            }
                        }

                        override fun onFailure(p0: Call<Disponibilidade>, erro: Throwable) {
                            Toast.makeText(context, "Erro: ${erro.localizedMessage}", Toast.LENGTH_SHORT).show()

                        }
                    })
                }


            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0x4D19493B)),
            shape = RoundedCornerShape(13.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(51.3.dp)
        ) {
            Text(text = "Salvar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (message.isNotEmpty()) {
            Text(text = message)
        }
    }
}

