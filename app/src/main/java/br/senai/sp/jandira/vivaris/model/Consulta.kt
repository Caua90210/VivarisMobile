package br.senai.sp.jandira.vivaris.model

data class AgendamentoRequest(
    val id_psicologo: Int,
    val id_cliente: Int,
    val data_consulta: String // Formato ISO 8601
)

data class session(
    val  id_consulta: Int,
    val  id_cliente: Int
)

data class consultaResponse(
    val data: consulta,
    val status_code: Int
)

data class consulta(
    val id: Int = 0,
    val dataConsulta: String,
    val valor: Int = 0,
    val avaliacao: Avaliacao,
    val cliente: Cliente,
    val psicologo: Psicologo
)

data class Avaliacao(
    val id: Int,
    val avaliacao: String,
    val cliente: Cliente
)