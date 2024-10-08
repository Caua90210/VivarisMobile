package br.senai.sp.jandira.vivaris.model


data class DisponibilidadePsicologo(
    val disponibilidade_id: Int,      // ID da disponibilidade
    val id_psicologo: Int,            // ID do psicólogo
    val status: String                // Status da disponibilidade (pode ser um campo adicional)
)

