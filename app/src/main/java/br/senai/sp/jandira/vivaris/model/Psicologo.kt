package br.senai.sp.jandira.vivaris.model


data class Psicologo(
    val id: Int = 0,
    val nome: String,
    val data_nascimento: String,
    val cip: String,
    val cpf: String,
    val email: String,
    val senha: String,
    val telefone: String,
    val foto_perfil: String?,
    val descricao: String,
    val link_instagram: String?,
    val id_sexo: Any,
    val tbl_psicologo_disponibilidade: List<TblPsicologoDisponibilidade> // Lista de disponibilidades do psicólogo
)

data class PsicologoResponsebyID(
    val data: PsicologoData2,
    val status_code: Int
)

data class PsicologoData2(
    val professional: Psicologo // O psicólogo em si
)

// Para a resposta completa que a API retorna
data class PsicologoPesquisa(
    val data: DataWrapper, // Agora é um objeto que contém a lista
    val status_code: Int
)

data class DataWrapper(
    val data: List<DataResponse> // Lista de psicólogos
)

data class DataResponse(
    val id: Int,
    val nome: String,
    val telefone: String,
    val data_nascimento: String,
    val foto_perfil: String?,
    val cip: String,
    val cpf: String,
    val email: String,
    val link_instagram: String?,
    val id_sexo: SexoResponse,
    val tbl_psicologo_disponibilidade: List<TblPsicologoDisponibilidade> // Lista de disponibilidades
)
data class PsicologoResponse(
    val data: DataResponse?,  // Pode ser nulo caso não exista um psicólogo
    val status_code: Int,
    val message: String?,
    val token: String?  // Token de autenticação (caso seja necessário)
)


data class TblPsicologoDisponibilidade(
    val id: Int,
    val tbl_disponibilidade: TblDisponibilidade
)

data class TblDisponibilidade(
    val dia_semana: String,
    val horario_inicio: String,
    val horario_fim: String,
    val id: Int
)



