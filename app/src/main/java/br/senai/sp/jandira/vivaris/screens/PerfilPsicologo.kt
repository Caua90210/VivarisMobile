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
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoResponsebyID
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import coil.compose.rememberAsyncImagePainter
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
        } ?: run {
            Text(text = "Psicólogo não encontrado", color = Color.Red)
        }
    }
}