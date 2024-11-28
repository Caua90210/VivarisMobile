package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.Disponibilidade
import br.senai.sp.jandira.vivaris.model.DisponibilidadeInfo
import br.senai.sp.jandira.vivaris.model.DisponibilidadeResponse
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoDisponibilidadeResponse
import br.senai.sp.jandira.vivaris.model.PsicologoResponsebyID
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import coil.compose.rememberAsyncImagePainter
import determineTimeOfDay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilPsicologo(controleDeNavegacao: NavHostController, id: Int, isPsicologo: Boolean){
    var psicologoResponse by remember { mutableStateOf<PsicologoResponsebyID?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)


    //Get de psicolo por id
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


    //Chamada API de disponibilidades
    val disponibilidadeService = retrofitFactory.getDisponibilidadeService()
    var disponibilidades by remember { mutableStateOf<List<Disponibilidade>>(emptyList()) }
    var existingDisponibilidades by remember { mutableStateOf<Set<Disponibilidade>>(emptySet()) }
    var disponibilidadeResponse by remember { mutableStateOf<DisponibilidadeResponse?>(null) }


   //Funcao para chamar detalhes da disponibilidade
    fun fetchDetalhesDisponibilidades(ids: List<Int?>) {
        ids.forEach { id ->
            if (id != null) {
                disponibilidadeService.getDisponibilidadebyId(id).enqueue(object : Callback<DisponibilidadeInfo> {
                    override fun onResponse(call: Call<DisponibilidadeInfo>, response: Response<DisponibilidadeInfo>) {
                        if (response.isSuccessful) {
                            val detalhesDaDisponibilidade = response.body()?.data
                            }else{
                                Log.d("Disponibilidades", "Erro: Sem disponibilidades")
                        }
                    }

                    override fun onFailure(call: Call<DisponibilidadeInfo>, t: Throwable) {
                        Log.e("DisponibilidadeScreen", "Falha ao buscar detalhes da disponibilidade: ${t.message}")
                    }
                })
            }
        }
    }

    // Função pra buscar disponibilidaes
    fun fetchDisponibilidades() {
        disponibilidadeService.getDisponibilidadePsicologoById(id).enqueue(object : Callback<PsicologoDisponibilidadeResponse> {
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




    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        if(isPsicologo){

        }
        psicologoResponse?.data?.professional?.let { psicologoData ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Nome: ${psicologoData.nome}", style = MaterialTheme.typography.titleMedium)
                Text(text = "Email: ${psicologoData.email}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Telefone: ${psicologoData.telefone}", style = MaterialTheme.typography.bodyMedium)
                Text(text = "Data de Nascimento: ${psicologoData.data_nascimento}", style = MaterialTheme.typography.bodyMedium)
                psicologoData.foto_perfil?.let { foto ->
                    Image(
                        painter = rememberAsyncImagePainter(foto),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp)
                    )
                }

            }
            
            disponibilidadeResponse?.let { disponibilidadeData ->
                Column(
                    modifier = Modifier.padding(16.dp)
                ){
                    Text(text = "Data: ${disponibilidadeData.data.horario_inicio}")
                    
                }
                
            }
            
        } ?: run {
            Text(text = "Psicólogo não encontrado", color = Color.Red)
        }
    }
}