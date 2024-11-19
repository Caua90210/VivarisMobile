package br.senai.sp.jandira.vivaris.model

data class LoginUsuario(
    val email: String,
    val senha: String

)

data class LoginResponse(
    val cliente: ClienteData,
    val status_code: Int,
    val token: String
)

data class ClienteData(
    val usuario: Cliente,
    val preferencias_usuario: List<List<Preferencia>>,
    val status: Int
)

data class Preferencia(
    val id: Int,
    val nome: String,
    val cor: String
)
