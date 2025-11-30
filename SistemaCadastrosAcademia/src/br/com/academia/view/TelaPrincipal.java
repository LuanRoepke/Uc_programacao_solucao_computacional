/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.academia.view;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {

    public TelaPrincipal() {
        setTitle("Sistema Academia");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JButton btnAlunos  = new JButton("Gerenciar Alunos");
        JButton btnTreinos = new JButton("Gerenciar Treinos");

        btnAlunos.addActionListener(e -> 
            SwingUtilities.invokeLater(() -> new TelaAlunos().setVisible(true))
        );

        btnTreinos.addActionListener(e -> 
            SwingUtilities.invokeLater(() -> new TelaTreinos().setVisible(true))
        );

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(btnAlunos);
        panel.add(btnTreinos);

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPrincipal().setVisible(true));
    }
}

