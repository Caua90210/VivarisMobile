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


data class DisponibilidadeInfo(
    val status_code: Int,
    val data: List<DisponibilidadeData>
)

data class DisponibilidadeData(
    val dia_semana: String,
    val horario_inicio: String,
    val horario_fim: String
)


data class PsicologoData(
    val id: Int,
    val nome: String,
    val email: String,
    val telefone: String,
    val disponibilidades: List<Disponibilidade>
)

data class PsicologoDisponibilidadeResponse(
    val data: PsicologoData,
    val status_code: Int
)
