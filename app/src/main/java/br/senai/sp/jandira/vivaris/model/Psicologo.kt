package br.senai.sp.jandira.vivaris.model

import android.media.Image

// Model para Psicólogo
data class Psicologo(
    val id: Int = 0,
    val nome: String,
    val data_nascimento: String, // Se está retornando string com formato de data
    val cip: String,
    val cpf: String,
    val email: String,
    val senha: String, // A senha é mesmo retornada na resposta da API? Se não, remova daqui
    val telefone: String,
    val foto_perfil: String?, // Null pode ser o caso
    val descricao: String,
    val link_instagram: String?,
    val id_sexo: Int // Pode ser referenciado como um ID
)

// Substituindo 'DataResponse' para refletir a estrutura JSON corretamente
data class DataResponse(
    val id: Int,
    val nome: String,
    val telefone: String,
    val data_nascimento: String, // Formato de string para datas
    val foto_perfil: String?,
    val cip: String,
    val email: String,
    val link_instagram: String?,
    val tbl_sexo: SexoResponse // Essa é a parte do sexo, já que ele contém id e descrição
)


// Resposta final da API de login
data class PsicologoResponse(
    val data: DataResponse?, // O corpo de dados principal
    val status_code: Int,
    val message: String? // Pode conter uma mensagem de erro ou sucesso
)
