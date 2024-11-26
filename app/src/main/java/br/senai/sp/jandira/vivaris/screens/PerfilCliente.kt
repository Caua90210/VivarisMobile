package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.ClienteResponsebyID
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun PerfilCliente(controleDeNavegacao: NavHostController, id: Int) {
    var clienteResponse by remember { mutableStateOf<ClienteResponsebyID?>(null) }
    var isLoading by remember {
        mutableStateOf(true)
    }
    var context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)

    LaunchedEffect(id) {
        val token = TokenRepository(context).getToken()
        if (token != null){
            retrofitFactory.getClienteService().getClienteById(id, token).enqueue(
                object : Callback<ClienteResponsebyID>{
                    override fun onResponse(
                        p0: Call<ClienteResponsebyID>,
                        response: Response<ClienteResponsebyID>
                    ) {
                     if (response.isSuccessful){
                         clienteResponse = response.body()
                         Log.d("Dados recebidos do cliente: ", clienteResponse.toString())

                     }else{
                         Log.d("API Error ", "Erro ao carregar cliente: ${response.errorBody()?.string()}")
                     }
                     isLoading = false
                    }

                    override fun onFailure(p0: Call<ClienteResponsebyID>, erro: Throwable) {
                        Log.e("Api Failure", "Falha ao conectar com a api: ${erro.message}")
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
    }else{
        clienteResponse?.data.let { clienteData ->
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Nome: ${clienteData?.nome}")
                Text(text = "Email: ${clienteData?.email}")
                Text(text = "${clienteData?.senha}")
                Text(text = "${clienteData?.telefone}")
                Text(text = "${clienteData?.link_instagram}")

            }

        } ?: run {
            Text(text = "CLiente não encontrado", color = Color.Red)
        }
    }





}