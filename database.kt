import java.sql.Connection
import java.sql.DriverManager

object Database {
    private const val URL = "jdbc:mysql://localhost:3306/nilai"
    private const val USER = "root"
    private const val PASS = "" // isi kalau pakai password

    fun getConnection(): Connection {
        return DriverManager.getConnection(URL, USER, PASS)
    }
}
