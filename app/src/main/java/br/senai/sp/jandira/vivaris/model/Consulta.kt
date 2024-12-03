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
  val id: Int,
  val data_consulta: String,
  val valor: Int,
  val avaliacao: String,
  val id_cliente: Int,
  val id_psicologo: Int

)