package br.senai.sp.jandira.vivaris.model


data class DisponibilidadePsicologo(
    val disponibilidade: Int,      // ID da disponibilidade
    val id_psicologo: Int,            // ID do psicólogo
    val status: String = "Livre"              // Status da disponibilidade (pode ser um campo adicional)
)

