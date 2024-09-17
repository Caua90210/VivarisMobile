import android.content.Context
import br.senai.sp.jandira.vivaris.dao.VivarisDB
import br.senai.sp.jandira.vivaris.model.Usuario

class UsuarioRepository(context: Context){
    private val db = VivarisDB.getBancoDeDados(context).UsuarioDAO()


    fun salvar(usuario: Usuario): Long{
        return db.inserir(usuario)
    }

}
