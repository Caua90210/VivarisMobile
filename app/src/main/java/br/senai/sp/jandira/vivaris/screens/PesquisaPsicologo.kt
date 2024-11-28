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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.model.DataResponse
import br.senai.sp.jandira.vivaris.model.Psicologo
import br.senai.sp.jandira.vivaris.model.PsicologoPesquisa
import br.senai.sp.jandira.vivaris.model.Sexo
import br.senai.sp.jandira.vivaris.model.SexoResponse
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
        senha = "",
        telefone = dataResponse.telefone,
        foto_perfil = dataResponse.foto_perfil,
        descricao = "",
        link_instagram = dataResponse.link_instagram,
        id_sexo = dataResponse.id_sexo?.data ?: 0,
        tbl_psicologo_disponibilidade = dataResponse.tbl_psicologo_disponibilidade
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PsicologoPesquisa(controleDeNavegacao: NavHostController, isPsicologo: Boolean, profissionais: List<Psicologo> = emptyList(), isLoading: Boolean = false) {




    var searchQuery by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Filtrando a lista de psicólogos com base na consulta de pesquisa
    val filteredProfissionais = profissionais.filter { profissional ->
        profissional.nome.contains(searchQuery, ignoreCase = true)

    }
    var loadingSexos by remember { mutableStateOf(true) }
    val sexoService = RetrofitFactory(context).getSexoService()
    var sexos by remember { mutableStateOf<List<Sexo>>(emptyList()) }

//
//    LaunchedEffect(Unit) {
//        sexoService.getSexoByID().enqueue(object : Callback<SexoResponse> {
//            override fun onResponse(call: Call<SexoResponse>, response: Response<SexoResponse>) {
//                if (response.isSuccessful) {
//                    sexos = response.body()?.data ?: emptyList()
//                    Log.d("Sexos", "Sexos carregados: $sexos")
//                } else {
//                    Toast.makeText(context, "Erro ao carregar sexos: ${response.code()}", Toast.LENGTH_SHORT).show()
//                    Log.e("Erro", "Corpo da resposta: ${response.errorBody()?.string()}")
//                }
//                loadingSexos = false
//            }
//
//            override fun onFailure(call: Call<SexoResponse>, t: Throwable) {
//                Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
//                loadingSexos = false
//            }
//        })
//    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Spacer(modifier = Modifier.height(30.dp))

        // Campo de entrada de pesquisa
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text("Busque por psicológos...",
                color = Color.White,
                fontWeight = FontWeight.Bold
                )
                          },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(0xFF99BFB5),
                focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedLabelColor = MaterialTheme.colorScheme.primary,
                unfocusedLabelColor = Color.Gray
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Pesquisar",
                    tint = Color.White
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .clickable(onClick = {
                                Log.d("Card Clicked", "Profissional: ${profissional.nome}")
                                controleDeNavegacao.navigate("perfilpsicologo/${profissional.id}/${isPsicologo}")
                            }),
                        shape = RoundedCornerShape(8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = profissional.nome, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Text(text = "", style = MaterialTheme.typography.bodyMedium)
                            //Text(text = "Telefone: ${profissional.telefone}", style = MaterialTheme.typography.bodyMedium)
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

@Preview(showBackground = true)
@Composable
fun PreviewPsicologoPesquisa() {
    val navController = rememberNavController()

    // Mock de dados para a preview
    val mockPsicologos = listOf(
        Psicologo(
            id = 1,
            nome = "Dr. João Silva",
            data_nascimento = "01/01/1980",
            cip = "123456",
            cpf = "123.456.789-00",
            email = "joao.silva@example.com",
            senha = "",
            telefone = "(11) 91234-5678",
            foto_perfil = "https://example.com/foto1.jpg",
            descricao = "",
            link_instagram = "",
            id_sexo = 1,
            tbl_psicologo_disponibilidade = emptyList()
        ),
        Psicologo(
            id = 2,
            nome = "Dra. Maria Oliveira",
            data_nascimento = "02/02/1985",
            cip = "654321",
            cpf = "987.654.321-00",
            email = "maria.oliveira@example.com",
            senha = "",
            telefone = "(11) 98765-4321",
            foto_perfil = "https://example.com/foto2.jpg",
            descricao = "",
            link_instagram = "",
            id_sexo = 2,
            tbl_psicologo_disponibilidade = emptyList()
        )
    )

    // Chamando o Composable com dados mockados
    PsicologoPesquisa(controleDeNavegacao = navController, isPsicologo = true, profissionais = mockPsicologos, isLoading = false)
}