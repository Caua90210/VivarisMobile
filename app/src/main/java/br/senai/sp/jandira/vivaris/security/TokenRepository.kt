package br.senai.sp.jandira.vivaris.security

import android.content.ContentValues
import android.content.Context

class TokenRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun saveUserData(token: String, userId: Int, isPsicologo: Boolean, userName: String) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_TOKEN, token)
            put(DatabaseHelper.COLUMN_USER_ID, userId)
            put(DatabaseHelper.COLUMN_IS_PSICOLOGO, if (isPsicologo) 1 else 0)
            put(DatabaseHelper.COLUMN_USER_NAME, userName)
        }

        val result = db.insert(DatabaseHelper.TABLE_NAME, null, values)
        if (result == -1L) {
            println("Erro ao inserir os dados.")
        }
        db.close()
    }


    fun getToken(): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_TOKEN),
            null, null, null, null, null
        )
        var token: String? = null
        if (cursor.moveToFirst()) {
            token = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TOKEN))
        }
        cursor.close()
        db.close()
        return token
    }



    fun getUserId(): Int? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_USER_ID),
            null, null, null, null, null
        )
        var userId: Int? = null
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID))
        }
        cursor.close()
        db.close()
        return userId
    }


    fun getIsPsicologo(): Boolean {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_IS_PSICOLOGO),
            null, null, null, null, null
        )
        var isPsicologo = false
        if (cursor.moveToFirst()) {
            isPsicologo = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_IS_PSICOLOGO)) == 1
        }
        cursor.close()
        db.close()
        return isPsicologo
    }


    fun getUserName(): String? {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            arrayOf(DatabaseHelper.COLUMN_USER_NAME),
            null, null, null, null, null
        )
        var userName: String? = null
        if (cursor.moveToFirst()) {
            userName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME))
        }
        cursor.close()
        db.close()
        return userName
    }


    fun clearData() {
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM ${DatabaseHelper.TABLE_NAME}")
        db.close()
    }
}
