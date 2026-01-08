package org.example.ui

import java.awt.*
import javax.swing.*

class LoginFrame : JFrame("Login Aplikasi") {

    private val txtUsername = JTextField()
    private val txtPassword = JPasswordField()
    private val btnLogin = JButton("LOGIN")
    private val btnReset = JButton("RESET")

    init {
        minimumSize = Dimension(500, 360)
        setLocationRelativeTo(null)
        defaultCloseOperation = EXIT_ON_CLOSE


        // ===== WARNA =====
        val blue = Color(33, 150, 243)
        val white = Color.WHITE
        val grayBorder = Color(210, 210, 210)

        // ===== BACKGROUND =====
        val bgPanel = JPanel(GridBagLayout())
        bgPanel.background = blue
        contentPane = bgPanel

        // ===== CARD =====
        val card = JPanel(GridBagLayout())
        card.background = white
        card.border = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        )

        bgPanel.add(card)

        val gbc = GridBagConstraints()
        gbc.gridx = 0
        gbc.fill = GridBagConstraints.HORIZONTAL
        gbc.insets = Insets(6, 0, 6, 0)

        // ===== JUDUL =====
        val title = JLabel("LOGIN", SwingConstants.CENTER)
        title.font = Font("Segoe UI", Font.BOLD, 20)
        title.foreground = blue

        gbc.gridy = 0
        gbc.insets = Insets(0, 0, 15, 0)
        card.add(title, gbc)

        gbc.insets = Insets(6, 0, 6, 0)

        // ===== USERNAME =====
        card.add(JLabel("Username"), gbc)

        gbc.gridy++
        txtUsername.preferredSize = Dimension(240, 32)
        txtUsername.border = BorderFactory.createLineBorder(grayBorder)
        card.add(txtUsername, gbc)

        // ===== PASSWORD =====
        gbc.gridy++
        card.add(JLabel("Password"), gbc)

        gbc.gridy++
        txtPassword.preferredSize = Dimension(240, 32)
        txtPassword.border = BorderFactory.createLineBorder(grayBorder)
        card.add(txtPassword, gbc)

        // ===== BUTTON =====
        gbc.gridy++
        gbc.insets = Insets(14, 0, 0, 0)

        btnLogin.background = blue
        btnLogin.foreground = white
        btnLogin.font = Font("Segoe UI", Font.BOLD, 13)
        btnLogin.isFocusPainted = false
        btnLogin.preferredSize = Dimension(240, 34)

        btnReset.background = Color(245, 245, 245)
        btnReset.isFocusPainted = false
        btnReset.preferredSize = Dimension(240, 32)

        val panelBtn = JPanel(GridLayout(2, 1, 0, 8))
        panelBtn.background = white
        panelBtn.add(btnLogin)
        panelBtn.add(btnReset)

        card.add(panelBtn, gbc)

        // ===== EVENT =====
        btnLogin.addActionListener { prosesLogin() }
        btnReset.addActionListener {
            txtUsername.text = ""
            txtPassword.text = ""
        }
    }

    private fun prosesLogin() {
        val user = txtUsername.text.trim()
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
