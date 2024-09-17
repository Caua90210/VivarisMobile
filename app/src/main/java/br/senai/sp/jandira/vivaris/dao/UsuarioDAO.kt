import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.senai.sp.jandira.vivaris.model.Cliente

@Dao
interface UsuarioDAO {

    @Insert
    fun inserir(usuario: Cliente): Long

    @Query("SELECT * FROM usuarios WHERE email = :email AND senha = :senha")
    suspend fun obterUsuarioPorCredenciais(email: String, senha: String): Cliente?

    @Query("SELECT * FROM usuarios WHERE id = :id")
    suspend fun obterUsuarioPorId(id: Int): Cliente?

    @Query("SELECT * FROM usuarios WHERE tipo = :tipo")
    suspend fun obterUsuariosPorTipo(tipo: String): List<Cliente>
}
