package org.example

import org.example.ui.LoginFrame
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater {
        LoginFrame().isVisible = true
    }
}
