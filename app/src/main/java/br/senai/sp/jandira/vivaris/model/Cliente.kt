package br.senai.sp.jandira.vivaris.model

import android.media.Image

data class Cliente(
    val id: Int = 0,
    val nome: String,
    val telefone: String,
    val email: String,
    val data_nascimento: String,
    val senha: String,
    val id_sexo: Int,
    val link_instagram: String? =null,
    val foto_perfil: String? = null,
    val cpf: String,
  //  val id_preferencias: List<Int>,
 //   val preferencias: List<Preferencias>? = null
)

data class ClienteResponse(
    val user: Cliente,
    val status_code: Int,
    val message: String
)

data class ClienteResponsebyID(
    val data: Cliente,
    val status_code: Int
)

