package br.senai.sp.jandira.vivaris.model

data class Disponibilidade(
    val id: Int? =null,
    val dia_semana: String,
    val horario_inicio: String,
    val horario_fim: String
){
    override fun hashCode(): Int {
        // Use o operador Elvis para garantir que não haja NullPointerException
        return (dia_semana?.hashCode() ?: 0) * 31 + (horario_inicio?.hashCode() ?: 0) * 31 + (horario_fim?.hashCode() ?: 0)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Disponibilidade) return false

        return dia_semana == other.dia_semana &&
                horario_inicio == other.horario_inicio &&
                horario_fim == other.horario_fim
    }
}


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


