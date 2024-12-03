package br.senai.sp.jandira.vivaris.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.senai.sp.jandira.vivaris.R
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
fun PsicologoPesquisa(controleDeNavegacao: NavHostController, isPsicologo: Boolean) {
    val profissionais = remember { mutableStateListOf<Psicologo>() }
    var searchQuery by remember { mutableStateOf("") }
    var selectedSexo by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    // Filtrando a lista de psicólogos com base na consulta de pesquisa
    val filteredProfissionais = profissionais.filter { profissional ->
        profissional.nome.contains(searchQuery, ignoreCase = true) && (selectedSexo == null || profissional.id_sexo.toString() == selectedSexo)
    }

    LaunchedEffect(Unit) {
        isLoading = true
        val psicologoService = RetrofitFactory(context).getPsicologoService()
        val token = TokenRepository(context).getToken()

        if (token != null) {
            psicologoService.getAllPsicologos(token).enqueue(object : Callback<PsicologoPesquisa> {
                override fun onResponse(call: Call<PsicologoPesquisa>, response: Response<PsicologoPesquisa>) {
                    if (response.isSuccessful) {
                        val psicologos = response.body()?.data?.data?.map { mapToPsicologo(it) } ?: emptyList()
                        profissionais.addAll(psicologos)
                        Log.d("Psicologos", "Psicologos carregados: $psicologos")
                    } else {
                        Log.e("Erro", "Erro ao carregar psicologos: ${response.code()}")
                        Toast.makeText(context, "Erro ao carregar psicologos: ${response.code()}", Toast.LENGTH_SHORT).show()
                    }
                    isLoading = false
                }

                override fun onFailure(call: Call<PsicologoPesquisa>, t: Throwable) {
                    Log.e("Erro", "Falha na chamada da API: ${t.message}")
                    Toast.makeText(context, "Erro: ${t.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
            })
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4F8)) // Cor de fundo suave
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(24.dp))
        // Barra de pesquisa e botão de filtro
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Voltar",
                modifier = Modifier
                    .size(32.dp)
                    .clickable {
                        controleDeNavegacao.popBackStack()
                    },
                tint = Color(0xFF52B693) // Verde
            )

            Spacer(modifier = Modifier.width(8.dp))

            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                placeholder = {
                    Text("Busque por psicólogos...", color = Color.Gray, fontWeight = FontWeight.Bold)
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color(0xFFE8F5E9), // Verde claro
                    focusedIndicatorColor = Color(0xFF52B693),
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedLabelColor = Color(0xFF52B693),
                    unfocusedLabelColor = Color.Gray
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Pesquisar",
                        tint = Color(0xFF52B693)
                    )
                }
            )

            // Botão de filtro
            IconButton(
                onClick = { /* Ação para abrir o filtro */ },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFE8F5E9))
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filtrar",
                    tint = Color(0xFF52B693)
                )
            }
        }

        // Exibição de carregamento
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF52B693))
            }
        } else {
            if (filteredProfissionais.isEmpty()) {
                // Mensagem quando não há resultados
                Text(
                    text = "Nenhum psicólogo encontrado.",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(filteredProfissionais) { profissional ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable(onClick = {
                                    Log.d("Card Clicked", "Profissional: ${profissional.nome}")
                                    controleDeNavegacao.navigate("perfilpsicologo/${profissional.id}?isPsicologo=$isPsicologo")
                                }),
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Nome do psicólogo
                                Text(
                                    text = profissional.nome,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF52B693) // Verde para o texto
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Foto do psicólogo (se disponível)
                                profissional.foto_perfil?.let { foto ->
                                    Image(
                                        painter = rememberAsyncImagePainter(foto),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(120.dp)
                                            .clip(RoundedCornerShape(60.dp)) // Imagem circular
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

