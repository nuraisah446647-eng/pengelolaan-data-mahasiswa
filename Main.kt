package org.example
import java.awt.*
import javax.swing.*
import javax.swing.table.DefaultTableModel


class LoginFrame : JFrame("Login Aplikasi") {

    private val txtUsername = JTextField(15)
    private val txtPassword = JPasswordField(15)
    private val btnLogin = JButton("Login")
    private val btnReset = JButton("Reset")

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(420, 300)
        setLocationRelativeTo(null)
        layout = BorderLayout()

        val title = JLabel("LOGIN APLIKASI", SwingConstants.CENTER)
        title.font = Font("Segoe UI", Font.BOLD, 20)
        title.border = BorderFactory.createEmptyBorder(20, 0, 15, 0)
        add(title, BorderLayout.NORTH)

        val panel = JPanel(GridBagLayout())
        panel.border = BorderFactory.createEmptyBorder(10, 40, 25, 40)

        val gbc = GridBagConstraints()
        gbc.insets = Insets(10, 10, 10, 10)
        gbc.fill = GridBagConstraints.HORIZONTAL

        gbc.gridx = 0; gbc.gridy = 0
        panel.add(JLabel("Username"), gbc)
        gbc.gridx = 1
        panel.add(txtUsername, gbc)

        gbc.gridx = 0; gbc.gridy = 1
        panel.add(JLabel("Password"), gbc)
        gbc.gridx = 1
        panel.add(txtPassword, gbc)

        gbc.gridx = 0; gbc.gridy = 2
        gbc.gridwidth = 2
        gbc.anchor = GridBagConstraints.CENTER
        val panelBtn = JPanel()
        panelBtn.add(btnLogin)
        panelBtn.add(btnReset)
        panel.add(panelBtn, gbc)

        add(panel, BorderLayout.CENTER)

        btnLogin.addActionListener { prosesLogin() }
        btnReset.addActionListener {
            txtUsername.text = ""
            txtPassword.text = ""
        }
    }

    private fun prosesLogin() {
        val user = txtUsername.text
        val pass = String(txtPassword.password)

        if (user == "admin" && pass == "admin") {
            dispose()
            AppMahasiswa(user).isVisible = true
        } else {
            JOptionPane.showMessageDialog(
                this,
                "Username atau Password salah!",
                "Login Gagal",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }
}

class AppMahasiswa(private val username: String) : JFrame("Pengelolaan Data Mahasiswa") {
    private fun tampilkanData() {
        model.rowCount = 0

        try {
            val conn = Database.getConnection()
            val sql = "SELECT * FROM mahasiswa"
            val ps = conn.prepareStatement(sql)
            val rs = ps.executeQuery()

            while (rs.next()) {
                val teknik = rs.getDouble("nilai_Teknik")
                val data = rs.getDouble("nilai_Data")
                val aplikasi = rs.getDouble("nilai_Aplikasi")
                val ipk = (teknik + data + aplikasi) / 3

                model.addRow(arrayOf(
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("jurusan"),
                    rs.getInt("semester"),
                    teknik,
                    data,
                    aplikasi,
                    String.format("%.2f", ipk)
                ))
            }

            rs.close()
            ps.close()
            conn.close()

            updateTotal()

        } catch (e: Exception) {
            JOptionPane.showMessageDialog(this, e.message)
        }
    }

    private val model: DefaultTableModel
    private val lblTotal = JLabel("Total Data Mahasiswa: 0")

    private val txtNim = JTextField(15)
    private val txtNama = JTextField(20)
    private val txtJurusan = JTextField(20)
    private val txtSemester = JTextField(5)
    private val txtTeknik = JTextField(5)
    private val txtData = JTextField(5)
    private val txtAplikasi = JTextField(5)
    private val txtCari = JTextField(15)

    private var selectedRow = -1

    init {
        setSize(980, 560)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout(10, 10)

        val btnLogout = JButton("Logout")
        val header = JPanel(BorderLayout())
        header.border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        header.add(JLabel("Login sebagai: $username"), BorderLayout.WEST)
        header.add(btnLogout, BorderLayout.EAST)
        add(header, BorderLayout.NORTH)


        val panelForm = JPanel(GridBagLayout())
        panelForm.border = BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(),
            "Input Data Mahasiswa"
        )

        val gbc = GridBagConstraints()
        gbc.insets = Insets(8, 10, 8, 10)   // ⬅️ jarak antar baris
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.anchor = GridBagConstraints.WEST


        listOf(
            txtNim, txtNama, txtJurusan,
            txtSemester, txtTeknik, txtData, txtAplikasi
        ).forEach {
            it.preferredSize = Dimension(220, 28)
        }


        fun row(label: String, field: JTextField, y: Int) {
            gbc.gridx = 0
            gbc.gridy = y
            gbc.weightx = 0.3
            panelForm.add(JLabel(label), gbc)

            gbc.gridx = 1
            gbc.weightx = 0.7
            panelForm.add(field, gbc)
        }

        row("NIM", txtNim, 0)
        row("Nama", txtNama, 1)
        row("Jurusan", txtJurusan, 2)
        row("Semester", txtSemester, 3)
        row("Teknik Optimasi", txtTeknik, 4)
        row("Data Mining", txtData, 5)
        row("Aplikasi Mobile 1", txtAplikasi, 6)


        val btnSimpan = JButton("Simpan")

        val btnEdit = JButton("Edit")

        val btnReset = JButton("Reset")


        val panelBtn = JPanel(GridLayout(3, 1, 0, 8)) // ⬅️ tombol rapi ke bawah
        panelBtn.add(btnSimpan)
        panelBtn.add(btnEdit)
        panelBtn.add(btnReset)

        gbc.gridx = 0
        gbc.gridy = 7
        gbc.gridwidth = 2
        gbc.weightx = 1.0
        gbc.insets = Insets(20, 10, 10, 10)
        panelForm.add(panelBtn, gbc)


        val kolom = arrayOf("NIM", "Nama", "Jurusan", "Semester", "Teknik Optimasi", "Data Mining", "Aplikasi Mobile", "IPK")
        model = DefaultTableModel(kolom, 0)
        val table = JTable(model)
        table.selectionModel.addListSelectionListener {
            selectedRow = table.selectedRow

            if (selectedRow >= 0) {
                txtNim.text = table.getValueAt(selectedRow, 0).toString()
                txtNama.text = table.getValueAt(selectedRow, 1).toString()
                txtJurusan.text = table.getValueAt(selectedRow, 2).toString()
                txtSemester.text = table.getValueAt(selectedRow, 3).toString()
                txtTeknik.text = table.getValueAt(selectedRow, 4).toString()
                txtData.text = table.getValueAt(selectedRow, 5).toString()
                txtAplikasi.text = table.getValueAt(selectedRow, 6).toString()
            }
        }


        val panelTabel = JPanel(BorderLayout())
        panelTabel.border = BorderFactory.createTitledBorder("Data Mahasiswa")

        val btnCari = JButton("Cari")
        btnCari.addActionListener {
            model.rowCount = 0
            val keyword = "%${txtCari.text}%"

            val sql = """
        SELECT * FROM mahasiswa
        WHERE nim LIKE ? OR nama LIKE ?
    """

            val conn = Database.getConnection()
            val ps = conn.prepareStatement(sql)
            ps.setString(1, keyword)
            ps.setString(2, keyword)

            val rs = ps.executeQuery()
            var found = false

            while (rs.next()) {
                found = true
                val teknik = rs.getDouble("nilai_Teknik")
                val data = rs.getDouble("nilai_Data")
                val aplikasi = rs.getDouble("nilai_Aplikasi")
                val ipk = (teknik + data + aplikasi) / 3

                model.addRow(arrayOf(
                    rs.getString("nim"),
                    rs.getString("nama"),
                    rs.getString("jurusan"),
                    rs.getInt("semester"),
                    teknik, data, aplikasi,
                    String.format("%.2f", ipk)
                ))
            }

            if (!found) {
                JOptionPane.showMessageDialog(this, "Data tidak ditemukan")
            }

            rs.close()
            ps.close()
            conn.close()
        }

        val btnTampil = JButton("Tampilkan Semua")
        val btnHapus = JButton("Hapus")

        val panelAtas = JPanel(GridBagLayout())
        panelAtas.border = BorderFactory.createEmptyBorder(8, 8, 8, 8)

        val gbcSearch = GridBagConstraints()
        gbcSearch.insets = Insets(4, 8, 4, 8)
        gbcSearch.anchor = GridBagConstraints.WEST

// Label
        gbcSearch.gridx = 0
        gbcSearch.gridy = 0
        panelAtas.add(JLabel("Cari (NIM / Nama)"), gbcSearch)

// TextField
        txtCari.preferredSize = Dimension(180, 28)
        gbcSearch.gridx = 1
        panelAtas.add(txtCari, gbcSearch)

// Tombol Cari
        gbcSearch.gridx = 2
        panelAtas.add(btnCari, gbcSearch)

// Tombol Tampilkan Semua
        gbcSearch.gridx = 3
        panelAtas.add(btnTampil, gbcSearch)

// Tombol Hapus
        gbcSearch.gridx = 4
        panelAtas.add(btnHapus, gbcSearch)


        panelTabel.add(panelAtas, BorderLayout.NORTH)
        panelTabel.add(JScrollPane(table), BorderLayout.CENTER)

        // ===== EVENT =====
        btnSimpan.addActionListener {

            val nim = txtNim.text.trim()
            val nama = txtNama.text.trim()
            val jurusan = txtJurusan.text.trim()
            val semesterText = txtSemester.text.trim()
            val teknikText = txtTeknik.text.trim()
            val dataText = txtData.text.trim()
            val aplikasiText = txtAplikasi.text.trim()

            // VALIDASI KOSONG
            if (
                nim.isEmpty() || nama.isEmpty() || jurusan.isEmpty() ||
                semesterText.isEmpty() || teknikText.isEmpty() ||
                dataText.isEmpty() || aplikasiText.isEmpty()
            ) {
                JOptionPane.showMessageDialog(
                    this,
                    "SEMUA FIELD WAJIB DIISI!",
                    "Validasi",
                    JOptionPane.WARNING_MESSAGE
                )
                return@addActionListener
            }

            try {
                val semester = semesterText.toInt()
                val teknik = teknikText.toDouble()
                val data = dataText.toDouble()
                val aplikasi = aplikasiText.toDouble()

                val conn = Database.getConnection()
                val sql = """
            INSERT INTO mahasiswa
            (nim, nama, jurusan, semester, nilai_Teknik, nilai_Data, nilai_Aplikasi)
            VALUES (?,?,?,?,?,?,?)
        """
                val ps = conn.prepareStatement(sql)

                ps.setString(1, nim)
                ps.setString(2, nama)
                ps.setString(3, jurusan)
                ps.setInt(4, semester)
                ps.setDouble(5, teknik)
                ps.setDouble(6, data)
                ps.setDouble(7, aplikasi)

                ps.executeUpdate()

                ps.close()
                conn.close()

                JOptionPane.showMessageDialog(this, "Data berhasil disimpan ke database")

                tampilkanData()   // ⬅️ ambil ulang dari database
                resetForm()

            }  catch (e: NumberFormatException) {
            JOptionPane.showMessageDialog(
                this,
                "Semester dan Nilai harus berupa angka!",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )

            } catch (e: java.sql.SQLIntegrityConstraintViolationException) {
            JOptionPane.showMessageDialog(
                this,
                "NIM sudah terdaftar!\nSilakan gunakan NIM lain.",
                "Data Duplikat",
                JOptionPane.ERROR_MESSAGE
            )

        } catch (e: Exception) {
            JOptionPane.showMessageDialog(
                this,
                "Gagal menyimpan data ke database",
                "Error",
                JOptionPane.ERROR_MESSAGE
            )
        }

    }

        btnEdit.addActionListener {

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(
                    this,
                    "Pilih data terlebih dahulu!",
                    "Peringatan",
                    JOptionPane.WARNING_MESSAGE
                )
                return@addActionListener
            }

            val nim = txtNim.text.trim()
            val nama = txtNama.text.trim()
            val jurusan = txtJurusan.text.trim()
            val semesterText = txtSemester.text.trim()
            val teknikText = txtTeknik.text.trim()
            val dataText = txtData.text.trim()
            val aplikasiText = txtAplikasi.text.trim()

            if (
                nim.isEmpty() || nama.isEmpty() || jurusan.isEmpty() ||
                semesterText.isEmpty() || teknikText.isEmpty() ||
                dataText.isEmpty() || aplikasiText.isEmpty()
            ) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!")
                return@addActionListener
            }

            try {
                val semester = semesterText.toInt()
                val teknik = teknikText.toDouble()
                val data = dataText.toDouble()
                val aplikasi = aplikasiText.toDouble()

                val conn = Database.getConnection()
                val sql = """
            UPDATE mahasiswa
            SET nama = ?, jurusan = ?, semester = ?,
                nilai_Teknik = ?, nilai_Data = ?, nilai_Aplikasi = ?
            WHERE nim = ?
        """
                val ps = conn.prepareStatement(sql)

                ps.setString(1, nama)
                ps.setString(2, jurusan)
                ps.setInt(3, semester)
                ps.setDouble(4, teknik)
                ps.setDouble(5, data)
                ps.setDouble(6, aplikasi)
                ps.setString(7, nim)

                ps.executeUpdate()

                ps.close()
                conn.close()

                JOptionPane.showMessageDialog(this, "Data berhasil diupdate")

                tampilkanData()
                selectedRow = -1
                resetForm()

            } catch (e: NumberFormatException) {
                JOptionPane.showMessageDialog(this, "Semester dan nilai harus angka!")
            } catch (e: Exception) {
                JOptionPane.showMessageDialog(this, e.message)
            }
        }

        btnHapus.addActionListener {

            val row = table.selectedRow
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih data terlebih dahulu!")
                return@addActionListener
            }

            val nim = model.getValueAt(row, 0).toString()

            val confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin menghapus data NIM $nim?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
            )

            if (confirm == JOptionPane.YES_OPTION) {
                val conn = Database.getConnection()
                val ps = conn.prepareStatement(
                    "DELETE FROM mahasiswa WHERE nim = ?"
                )
                ps.setString(1, nim)
                ps.executeUpdate()

                ps.close()
                conn.close()

                JOptionPane.showMessageDialog(this, "Data berhasil dihapus")

                tampilkanData()
                resetForm()
            }
        }

        btnCari.addActionListener {
            val key = txtCari.text.lowercase()
            var found = false
            for (i in 0 until model.rowCount) {
                val nim = model.getValueAt(i, 0).toString().lowercase()
                val nama = model.getValueAt(i, 1).toString().lowercase()
                if (nim.contains(key) || nama.contains(key)) {
                    table.setRowSelectionInterval(i, i)
                    found = true
                    break
                }
            }
            if (!found) JOptionPane.showMessageDialog(this, "Data tidak ditemukan!")
        }

        btnTampil.addActionListener {
            table.clearSelection()
            txtCari.text = ""
        }

        btnReset.addActionListener { resetForm() }

        btnLogout.addActionListener {
            val confirm = JOptionPane.showConfirmDialog(
                this, "Yakin ingin logout?", "Logout",
                JOptionPane.YES_NO_OPTION
            )
            if (confirm == JOptionPane.YES_OPTION) {
                dispose()
                LoginFrame().isVisible = true
            }
        }
        
        add(panelForm, BorderLayout.WEST)
        add(panelTabel, BorderLayout.CENTER)
        add(lblTotal, BorderLayout.SOUTH)

        tampilkanData()

    }

    private fun resetForm() {
        txtNim.text = ""
        txtNama.text = ""
        txtJurusan.text = ""
        txtSemester.text = ""
        txtTeknik.text = ""
        txtData.text = ""
        txtAplikasi.text = ""
        selectedRow = -1
    }

    private fun updateTotal() {
        lblTotal.text = "Total Data Mahasiswa: ${model.rowCount}"
    }
}

fun main() {
    SwingUtilities.invokeLater {
        LoginFrame().isVisible = true
    }
}


