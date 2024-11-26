package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.senai.sp.jandira.vivaris.model.DataResponse
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoPesquisa
import br.senai.sp.jandira.vivaris.security.TokenRepository
import br.senai.sp.jandira.vivaris.service.RetrofitFactory
import coil.compose.rememberAsyncImagePainter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun mapToPsicologo(dataResponse: DataResponse): Psicologo {
    return Psicologo(
        id = dataResponse.id,
        nome = dataResponse.nome,
        data_nascimento = dataResponse.data_nascimento,
        cip = dataResponse.cip,
        cpf = dataResponse.cpf,
        email = dataResponse.email,
        senha = "", // Defina o valor adequado ou obtenha de outro lugar
        telefone = dataResponse.telefone,
        foto_perfil = dataResponse.foto_perfil,
        descricao = "", // Defina o valor adequado ou obtenha de outro lugar
        link_instagram = dataResponse.link_instagram,
        id_sexo = dataResponse.id_sexo?.data ?: 0,
        tbl_psicologo_disponibilidade = dataResponse.tbl_psicologo_disponibilidade
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsicologoPesquisa(controleDeNavegacao: NavHostController) {
    var profissionais by remember { mutableStateOf(listOf<Psicologo>()) }
    var isLoading by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current
    val retrofitFactory = RetrofitFactory(context)

    LaunchedEffect(Unit) {
        val token = TokenRepository(context).getToken()
        if (token != null) {
            retrofitFactory.getPsicologoService().getAllPsicologos(token).enqueue(object : Callback<PsicologoPesquisa> {
                override fun onResponse(call: Call<PsicologoPesquisa>, response: Response<PsicologoPesquisa>) {
                    if (response.isSuccessful) {
                        response.body()?.let { responseData ->
                            Log.d("API Response", "Dados recebidos: ${responseData.data.data}") // Acessando a lista de psicólogos
                            profissionais = responseData.data.data.map { dataResponse ->
                                mapToPsicologo(dataResponse)
                            }
                        } ?: run {
                            Log.e("API Error", "Resposta sem corpo")
                            Toast.makeText(context, "Resposta vazia", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("API Error", "Erro ao carregar: ${response.errorBody()?.string()}")
                        Toast.makeText(context, "Erro ao carregar profissionais: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<PsicologoPesquisa>, t: Throwable) {
                    Log.e("API Failure", "Falha na requisição: ${t.message}")
                    Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            })
        }
    }

    // Filtrando a lista de psicólogos com base na consulta de pesquisa
    val filteredProfissionais = profissionais.filter { profissional ->
        profissional.nome.contains(searchQuery, ignoreCase = true)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        // Campo de entrada de pesquisa
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Pesquisar psicólogos...") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.White,
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar",
                    tint = Color.Gray
                )
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn {
                items(filteredProfissionais) { profissional ->
                    Card(
                        modifier = Modifier.padding(8.dp)
                            .clickable(onClick = {
                            Log.d("Card Clicked", "Profissional: ${profissional.nome}")
                                controleDeNavegacao.navigate("perfilpsicologo/${profissional.id}")
                        }),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = profissional.nome, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(text = "Email: ${profissional.email}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Telefone: ${profissional.telefone}", style = MaterialTheme.typography.bodyMedium)
                            Text(text = "Data de Nascimento: ${profissional.data_nascimento}", style = MaterialTheme.typography.bodyMedium)
                            profissional.foto_perfil?.let { foto ->
                                Image(
                                    painter = rememberAsyncImagePainter(foto),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(100.dp)
                                        .padding(top = 8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}