package br.senai.sp.jandira.vivaris.model

data class Disponibilidade(
    val id: Int? =null,
    val dia_semana: String,
    val horario_inicio: String,
    val horario_fim: String
)


data class DisponibilidadeResponse(
    val data: Disponibilidade,
    val status_code: Int,
    val message: String
)
