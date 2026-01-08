package org.example.database


import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val URL = "jdbc:mysql://localhost:3306/aplikasi_mobile1"
    private const val USER = "root"
    private const val PASS = ""

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASS)
    }
}
