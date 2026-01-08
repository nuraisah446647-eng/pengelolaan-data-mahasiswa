package org.example.ui

import org.example.dao.MahasiswaDao
import org.example.model.Mahasiswa
import java.awt.*
import javax.swing.*
import javax.swing.table.DefaultTableModel

class AppMahasiswa(private val username: String) : JFrame("Pengelolaan Data Mahasiswa") {

    private val dao = MahasiswaDao()

    // ===== TABLE MODEL =====
    private val model = DefaultTableModel(
        arrayOf(
            "NIM", "Nama", "Jurusan", "Semester",
            "Tugas 1", "Tugas 2", "Tugas 3", "Tugas 4", "Tugas 5",
            "UTS", "UAS", "IPK", "Grade"
        ), 0
    )
    private val table = JTable(model)
    private val lblTotal = JLabel("Total Data: 0")

    // ===== FORM =====
    private val txtNim = JTextField(15)
    private val txtNama = JTextField(15)
    private val txtJurusan = JTextField(15)
    private val txtSemester = JTextField(5)
    private val txtTugas1 = JTextField(5)
    private val txtTugas2 = JTextField(5)
    private val txtTugas3 = JTextField(5)
    private val txtTugas4 = JTextField(5)
    private val txtTugas5 = JTextField(5)
    private val txtUTS = JTextField(5)
    private val txtUAS = JTextField(5)
    private val txtCari = JTextField(15)

    private var selectedRow = -1

    init {

        extendedState = JFrame.MAXIMIZED_BOTH
        defaultCloseOperation = EXIT_ON_CLOSE


        setSize(1200, 580)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE
        layout = BorderLayout(10, 10)

        // ===== HEADER =====
        val btnLogout = JButton("Logout")
        btnLogout.addActionListener { logout() }

        val header = JPanel(BorderLayout())
        header.border = BorderFactory.createEmptyBorder(5, 10, 5, 10)
        header.add(JLabel("Login sebagai: $username"), BorderLayout.WEST)
        header.add(btnLogout, BorderLayout.EAST)
        add(header, BorderLayout.NORTH)

        // ===== FORM PANEL =====
        val panelForm = JPanel(GridBagLayout())
        panelForm.border = BorderFactory.createTitledBorder("Input Data Mahasiswa")
        val gbc = GridBagConstraints()
        gbc.insets = Insets(8, 10, 8, 10)
        gbc.fill = GridBagConstraints.HORIZONTAL

        listOf(
            txtNim, txtNama, txtJurusan, txtSemester,
            txtTugas1, txtTugas2, txtTugas3, txtTugas4, txtTugas5,
            txtUTS, txtUAS
        ).forEach { it.preferredSize = Dimension(220, 28) }

        fun row(label: String, field: JTextField, y: Int) {
            gbc.gridx = 0; gbc.gridy = y
            panelForm.add(JLabel(label), gbc)
            gbc.gridx = 1
            panelForm.add(field, gbc)
        }

        row("NIM", txtNim, 0)
        row("Nama", txtNama, 1)
        row("Jurusan", txtJurusan, 2)
        row("Semester", txtSemester, 3)
        row("Tugas 1", txtTugas1, 4)
        row("Tugas 2", txtTugas2, 5)
        row("Tugas 3", txtTugas3, 6)
        row("Tugas 4", txtTugas4, 7)
        row("Tugas 5", txtTugas5, 8)
        row("UTS", txtUTS, 9)
        row("UAS", txtUAS, 10)

        val btnSimpan = JButton("Simpan")
        val btnEdit = JButton("Edit")
        val btnReset = JButton("Reset")

        listOf(btnSimpan, btnEdit, btnReset).forEach { btn ->
            btn.background = Color(70, 130, 180)
            btn.foreground = Color.WHITE
            btn.isFocusPainted = false
            btn.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            btn.addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseEntered(e: java.awt.event.MouseEvent?) {
                    btn.background = Color(100, 160, 220)
                }

                override fun mouseExited(e: java.awt.event.MouseEvent?) {
                    btn.background = Color(70, 130, 180)
                }
            })
        }

        btnSimpan.addActionListener { simpan() }
        btnEdit.addActionListener { update() }
        btnReset.addActionListener { resetForm() }

        gbc.gridx = 0
        gbc.gridy = 11
        gbc.gridwidth = 2
        panelForm.add(JPanel(GridLayout(3, 1, 0, 6)).apply {
            add(btnSimpan); add(btnEdit); add(btnReset)
        }, gbc)

        add(panelForm, BorderLayout.WEST)

        // ===== TABLE PANEL =====
        val panelTable = JPanel(BorderLayout())
        panelTable.border = BorderFactory.createTitledBorder("Data Mahasiswa")

        val btnCari = JButton("Cari")
        val btnTampil = JButton("Tampilkan Semua")
        val btnHapus = JButton("Hapus")

        listOf(btnCari, btnTampil, btnHapus).forEach { btn ->
            btn.background = Color(70, 130, 180)
            btn.foreground = Color.WHITE
            btn.isFocusPainted = false
            btn.cursor = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR)
            btn.addMouseListener(object : java.awt.event.MouseAdapter() {
                override fun mouseEntered(e: java.awt.event.MouseEvent?) {
                    btn.background = Color(100, 160, 220)
                }

                override fun mouseExited(e: java.awt.event.MouseEvent?) {
                    btn.background = Color(70, 130, 180)
                }
            })
        }

        btnCari.addActionListener { cari() }
        btnTampil.addActionListener { tampilkanData() }
        btnHapus.addActionListener { hapus() }

        val panelSearch = JPanel()
        panelSearch.add(JLabel("Cari NIM / Nama"))
        panelSearch.add(txtCari)
        panelSearch.add(btnCari)
        panelSearch.add(btnTampil)
        panelSearch.add(btnHapus)

        panelTable.add(panelSearch, BorderLayout.NORTH)
        panelTable.add(JScrollPane(table), BorderLayout.CENTER)
        add(panelTable, BorderLayout.CENTER)
        add(lblTotal, BorderLayout.SOUTH)

        // ===== TABLE CLICK =====
        table.selectionModel.addListSelectionListener {
            selectedRow = table.selectedRow
            if (selectedRow >= 0) {
                txtNim.text = model.getValueAt(selectedRow, 0).toString()
                txtNama.text = model.getValueAt(selectedRow, 1).toString()
                txtJurusan.text = model.getValueAt(selectedRow, 2).toString()
                txtSemester.text = model.getValueAt(selectedRow, 3).toString()
                txtTugas1.text = model.getValueAt(selectedRow, 4).toString()
                txtTugas2.text = model.getValueAt(selectedRow, 5).toString()
                txtTugas3.text = model.getValueAt(selectedRow, 6).toString()
                txtTugas4.text = model.getValueAt(selectedRow, 7).toString()
                txtTugas5.text = model.getValueAt(selectedRow, 8).toString()
                txtUTS.text = model.getValueAt(selectedRow, 9).toString()
                txtUAS.text = model.getValueAt(selectedRow, 10).toString()
                txtNim.isEnabled = false
            }
        }

        SwingUtilities.invokeLater {
            tampilkanData()
        }

    }

    // ===== LOGIC =====
    private fun ambilData(): Mahasiswa? {
        try {
            val t1 = txtTugas1.text.toDouble()
            val t2 = txtTugas2.text.toDouble()
            val t3 = txtTugas3.text.toDouble()
            val t4 = txtTugas4.text.toDouble()
            val t5 = txtTugas5.text.toDouble()
            val uts = txtUTS.text.toDouble()
            val uas = txtUAS.text.toDouble()
            val semester = txtSemester.text.trim()
            if (semester.isEmpty()) throw Exception("Semester kosong")

            val ipk = (t1 + t2 + t3 + t4 + t5) / 5 * 0.4 + uts * 0.3 + uas * 0.3
            val grade = hitungGrade(ipk)

            return Mahasiswa(

                nim = txtNim.text.trim(),
                nama = txtNama.text.trim(),
                semester = semester,
                jurusan = txtJurusan.text.trim(),
                tugas1 = t1,
                tugas2 = t2,
                tugas3 = t3,
                tugas4 = t4,
                tugas5 = t5,
                uts = uts,
                uas = uas,
                ipk = ipk,
                grade = grade
            )
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(this, "Input tidak valid: ${e.message}")
            return null
        }
    }

    private fun simpan() {
        val m = ambilData() ?: return
        try {
            if (dao.insert(m)) {
                JOptionPane.showMessageDialog(this,"Data berhasil disimpan")
                tampilkanData()
                resetForm()
            }
        } catch (e: Exception) {
            JOptionPane.showMessageDialog(
                this,
                e.message,
                "Gagal Simpan",
                JOptionPane.ERROR_MESSAGE
            )
        }
    }


    private fun update() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu"); return
        }
        val m = ambilData() ?: return
        if (dao.update(m)) {
            JOptionPane.showMessageDialog(this, "Data berhasil diupdate")
            tampilkanData()
            resetForm()
        } else {
            JOptionPane.showMessageDialog(this, "Gagal update data")
        }
    }

    private fun hapus() {
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data dulu"); return
        }
        val nim = model.getValueAt(selectedRow, 0).toString()
        if (JOptionPane.showConfirmDialog(
                this,
                "Hapus NIM $nim?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION
        ) {
            if (dao.delete(nim)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus")
                tampilkanData()
                resetForm()
            } else {
                JOptionPane.showMessageDialog(this, "Gagal hapus data")
            }
        }
    }

    private fun cari() {
        model.rowCount = 0
        dao.search(txtCari.text.trim()).forEach {
            model.addRow(
                arrayOf(
                    it.nim, it.nama, it.jurusan, it.semester,
                    it.tugas1, it.tugas2, it.tugas3, it.tugas4, it.tugas5,
                    it.uts, it.uas,
                    String.format("%.2f", it.ipk),
                    hitungGrade(it.ipk)
                )
            )
        }
        lblTotal.text = "Total Data: ${model.rowCount}"
    }

    private fun tampilkanData() {
        try {
            model.rowCount = 0
            dao.getAll().forEach {
                model.addRow(
                    arrayOf(
                        it.nim, it.nama, it.jurusan, it.semester,
                        it.tugas1, it.tugas2, it.tugas3, it.tugas4, it.tugas5,
                        it.uts, it.uas,
                        String.format("%.2f", it.ipk),
                        hitungGrade(it.ipk)
                    )
                )
            }
            lblTotal.text = "Total Data: ${model.rowCount}"
        } catch (ex: Exception) {
            JOptionPane.showMessageDialog(this, "Gagal menampilkan data: ${ex.message}")
        }
    }

    private fun resetForm() {
        txtNim.isEnabled = true

        val fields: List<JTextField> = listOf(
            txtNim, txtNama, txtJurusan, txtSemester,
            txtTugas1, txtTugas2, txtTugas3, txtTugas4, txtTugas5,
            txtUTS, txtUAS
        )

        fields.forEach { it.text = "" }
        selectedRow = -1
    }


    private fun logout() {
        if (JOptionPane.showConfirmDialog(
                this,
                "Logout?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION
            ) == JOptionPane.YES_OPTION
        ) {
            dispose()
            LoginFrame().isVisible = true
        }
    }

    private fun hitungGrade(ipk: Double): String = when {
        ipk >= 85 -> "A"
        ipk >= 70 -> "B"
        ipk >= 55 -> "C"
        ipk >= 40 -> "D"
        else -> "E"
    }

}

