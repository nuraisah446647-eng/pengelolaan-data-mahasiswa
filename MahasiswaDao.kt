package org.example.dao

import org.example.database.Database
import org.example.model.Mahasiswa
import java.sql.ResultSet

class MahasiswaDao {

    fun insert(m: Mahasiswa): Boolean {
        return try {
            val sql = """
            INSERT INTO mahasiswa
            (nim,nama,jurusan,semester,tugas1,tugas2,tugas3,tugas4,tugas5,uts,uas,ipk,grade)
            VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)
        """.trimIndent()

            val conn = Database.getConnection()
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, m.nim)
            stmt.setString(2, m.nama)
            stmt.setString(3, m.jurusan)
            stmt.setString(4, m.semester)
            stmt.setDouble(5, m.tugas1)
            stmt.setDouble(6, m.tugas2)
            stmt.setDouble(7, m.tugas3)
            stmt.setDouble(8, m.tugas4)
            stmt.setDouble(9, m.tugas5)
            stmt.setDouble(10, m.uts)
            stmt.setDouble(11, m.uas)
            stmt.setDouble(12, m.ipk)
            stmt.setString(13, m.grade)

            stmt.executeUpdate()
            stmt.close()
            conn.close()
            true

        } catch (ex: java.sql.SQLIntegrityConstraintViolationException) {
            // 👉 NIM sudah ada
            throw Exception("NIM sudah terdaftar")

        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }


    fun update(m: Mahasiswa): Boolean {
        return try {
            val sql = """
                UPDATE mahasiswa SET
                nama=?, jurusan=?, semester=?, tugas1=?, tugas2=?, tugas3=?, tugas4=?, tugas5=?, uts=?, uas=?, ipk=?, grade=?
                WHERE nim=?
            """.trimIndent()

            val conn = Database.getConnection()
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, m.nama)
            stmt.setString(2, m.jurusan)
            stmt.setString(3, m.semester)
            stmt.setDouble(4, m.tugas1)
            stmt.setDouble(5, m.tugas2)
            stmt.setDouble(6, m.tugas3)
            stmt.setDouble(7, m.tugas4)
            stmt.setDouble(8, m.tugas5)
            stmt.setDouble(9, m.uts)
            stmt.setDouble(10, m.uas)
            stmt.setDouble(11, m.ipk)
            stmt.setString(12, m.grade)
            stmt.setString(13, m.nim)
            val result = stmt.executeUpdate()
            stmt.close()
            conn.close()
            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    fun delete(nim: String): Boolean {
        return try {
            val sql = "DELETE FROM mahasiswa WHERE nim=?"
            val conn = Database.getConnection()
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, nim)
            val result = stmt.executeUpdate()
            stmt.close()
            conn.close()
            result > 0
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    fun getAll(): List<Mahasiswa> {
        val list = mutableListOf<Mahasiswa>()
        return try {
            val sql = "SELECT * FROM mahasiswa"
            val conn = Database.getConnection()
            val stmt = conn.prepareStatement(sql)
            val rs: ResultSet = stmt.executeQuery()
            while(rs.next()) {
                list.add(mapResultSet(rs))
            }
            rs.close(); stmt.close(); conn.close()
            list
        } catch (ex: Exception) {
            ex.printStackTrace()
            list
        }
    }

    fun search(keyword: String): List<Mahasiswa> {
        val list = mutableListOf<Mahasiswa>()
        return try {
            val sql = "SELECT * FROM mahasiswa WHERE nim LIKE ? OR nama LIKE ?"
            val conn = Database.getConnection()
            val stmt = conn.prepareStatement(sql)
            stmt.setString(1, "%$keyword%")
            stmt.setString(2, "%$keyword%")
            val rs: ResultSet = stmt.executeQuery()
            while(rs.next()) {
                list.add(mapResultSet(rs))
            }
            rs.close(); stmt.close(); conn.close()
            list
        } catch (ex: Exception) {
            ex.printStackTrace()
            list
        }
    }



    private fun mapResultSet(rs: ResultSet): Mahasiswa {
        return Mahasiswa(

            nim = rs.getString("nim"),
            nama = rs.getString("nama") ?: "",
            jurusan = rs.getString("jurusan") ?: "",
            semester = rs.getString("semester") ?: "",
            tugas1 = rs.getDouble("tugas1"),
            tugas2 = rs.getDouble("tugas2"),
            tugas3 = rs.getDouble("tugas3"),
            tugas4 = rs.getDouble("tugas4"),
            tugas5 = rs.getDouble("tugas5"),
            uts = rs.getDouble("uts"),
            uas = rs.getDouble("uas"),
            ipk = rs.getDouble("ipk"),
            grade = rs.getString("grade") ?: "-"
        )
    }


}
