package br.senai.sp.jandira.vivaris.model

data class Disponibilidade(
    val id: Int = 0,
    val dia_semana: String,
    val horario_inicio: String,
    val horario_fim: String
)


