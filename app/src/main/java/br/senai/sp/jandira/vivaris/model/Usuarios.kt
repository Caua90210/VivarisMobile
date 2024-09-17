package br.senai.sp.jandira.vivaris.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String,
    val telefone: String,
    val email: String,
    val dataNascimento: String,
    val senha: String,
    val sexo: String,
    val tipo: String, // "cliente" ou "psicologo"
    val crp: String? = null, // Apenas para psicólogos
    val isPsicologo: Boolean
)
