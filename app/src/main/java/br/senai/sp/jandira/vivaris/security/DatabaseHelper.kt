package br.senai.sp.jandira.vivaris.security

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.media3.common.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {



    companion object {
        const val DATABASE_NAME = "vivaris.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "user_data"
        const val COLUMN_TOKEN = "token"
        const val COLUMN_USER_ID = "user_id"
        const val COLUMN_IS_PSICOLOGO = "is_psicologo"
        const val COLUMN_USER_NAME = "user_name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_TOKEN TEXT PRIMARY KEY,
                $COLUMN_USER_ID INTEGER,
                $COLUMN_IS_PSICOLOGO INTEGER,
                $COLUMN_USER_NAME TEXT
            )
        """.trimIndent()
        Log.d("DatabaseHelper", "Criando tabela no banco de dados: $createTable")
        db.execSQL(createTable)
        
        val cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='$TABLE_NAME'", null)
        if (cursor.count == 0) {
            Log.e("DatabaseHelper", "Tabela $TABLE_NAME não foi criada")
        } else {
            Log.d("DatabaseHelper", "Tabela $TABLE_NAME criada com sucesso")
        }
        cursor.close()

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
