package br.senai.sp.jandira.vivaris.model


data class Psicologo(
    val id: Int = 0,
    val nome: String,
    val data_nascimento: String,
    val cip: String,
    val cpf: String,
    val email: String,
    val senha: String,
    val telefone: String,
    val foto_perfil: String?,
    val descricao: String,
    val link_instagram: String?,
    val id_sexo: Int
)


data class DataResponse(
    val id: Int,
    val nome: String,
    val telefone: String,
    val data_nascimento: String,
    val foto_perfil: String?,
    val cip: String,
    val email: String,
    val link_instagram: String?,
    val tbl_sexo: SexoResponse
)



data class PsicologoResponse(
    val data: DataResponse?,
    val status_code: Int,
    val message: String?,
    val token: String
)
