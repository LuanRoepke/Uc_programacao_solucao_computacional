/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.academia.view;

import br.com.academia.dao.AlunoDAO;
import br.com.academia.model.Aluno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.Font;

public class TelaAlunos extends JFrame {

    private JTextField txtId;
    private JTextField txtNome;
    private JTextField txtCpf;
    private JTextField txtDataNasc;
    private JTextField txtTelefone;
    private JTextField txtEmail;

    private JTable tblAlunos;
    private DefaultTableModel tableModel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaAlunos() {
        setTitle("Cadastro de Alunos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        carregarTabela();
    }

    private void initComponents() {
    JPanel root = new JPanel(new BorderLayout(8, 8));
    root.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(root);

    JPanel panelForm = new JPanel(new GridLayout(6, 2, 8, 8));
    panelForm.setBorder(new TitledBorder("Dados do aluno"));

    panelForm.add(new JLabel("ID:"));
    txtId = new JTextField();
    txtId.setEditable(false);
    panelForm.add(txtId);

    panelForm.add(new JLabel("Nome:"));
    txtNome = new JTextField();
    panelForm.add(txtNome);

    panelForm.add(new JLabel("CPF:"));
    txtCpf = new JTextField();
    panelForm.add(txtCpf);

    panelForm.add(new JLabel("Data Nasc. (dd/MM/yyyy):"));
    txtDataNasc = new JTextField();
    panelForm.add(txtDataNasc);

    panelForm.add(new JLabel("Telefone:"));
    txtTelefone = new JTextField();
    panelForm.add(txtTelefone);

    panelForm.add(new JLabel("E-mail:"));
    txtEmail = new JTextField();
    panelForm.add(txtEmail);

    String[] colunas = { "ID", "Nome", "CPF", "Data Nasc.", "Telefone", "E-mail" };
    tableModel = new DefaultTableModel(colunas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tblAlunos = new JTable(tableModel);
    tblAlunos.setFillsViewportHeight(true);
    tblAlunos.setRowHeight(22);
    tblAlunos.getTableHeader().setFont(
            tblAlunos.getTableHeader().getFont().deriveFont(Font.BOLD));
    JScrollPane scrollTabela = new JScrollPane(tblAlunos);

    tblAlunos.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            preencherFormularioAPartirDaTabela();
        }
    });

    // Botões
    JButton btnNovo = new JButton("Novo");
    JButton btnSalvar = new JButton("Salvar");
    JButton btnExcluir = new JButton("Excluir");
    JButton btnAtualizar = new JButton("Atualizar Lista");

    Dimension btnSize = new Dimension(120, 30);
    btnNovo.setPreferredSize(btnSize);
    btnSalvar.setPreferredSize(btnSize);
    btnExcluir.setPreferredSize(btnSize);
    btnAtualizar.setPreferredSize(btnSize);

    btnNovo.addActionListener(e -> limparCampos());
    btnSalvar.addActionListener(e -> salvarAluno());
    btnExcluir.addActionListener(e -> excluirAlunoSelecionado());
    btnAtualizar.addActionListener(e -> carregarTabela());

    JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    panelBotoes.setBorder(new EmptyBorder(5, 0, 0, 0));
    panelBotoes.add(btnNovo);
    panelBotoes.add(btnSalvar);
    panelBotoes.add(btnExcluir);
    panelBotoes.add(btnAtualizar);

    //Monta tudo no root
    root.add(panelForm, BorderLayout.NORTH);
    root.add(scrollTabela, BorderLayout.CENTER);
    root.add(panelBotoes, BorderLayout.SOUTH);
    }


    //Açõe
    private void carregarTabela() {
        tableModel.setRowCount(0); // limpa

        AlunoDAO dao = new AlunoDAO();
        try {
            List<Aluno> alunos = dao.listarTodos();
            for (Aluno a : alunos) {
                Object[] row = {
                        a.getId(),
                        a.getNome(),
                        a.getCpf(),
                        a.getDataNascimento().format(formatter),
                        a.getTelefone(),
                        a.getEmail()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar lista de alunos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtCpf.setText("");
        txtDataNasc.setText("");
        txtTelefone.setText("");
        txtEmail.setText("");
        tblAlunos.clearSelection();
    }

    private Aluno getAlunoDoFormulario() throws Exception {
        String nome = txtNome.getText().trim();
        String cpf = txtCpf.getText().trim();
        String dataStr = txtDataNasc.getText().trim();
        String telefone = txtTelefone.getText().trim();
        String email = txtEmail.getText().trim();

        if (nome.isEmpty() || cpf.isEmpty() || dataStr.isEmpty()) {
            throw new Exception("Nome, CPF e Data de Nascimento são obrigatórios.");
        }

        LocalDate dataNasc;
        try {
            dataNasc = LocalDate.parse(dataStr, formatter);
        } catch (Exception ex) {
            throw new Exception("Data de nascimento inválida. Use o formato dd/MM/yyyy.");
        }

        Aluno a = new Aluno();
        if (!txtId.getText().trim().isEmpty()) {
            a.setId(Integer.parseInt(txtId.getText().trim()));
        }
        a.setNome(nome);
        a.setCpf(cpf);
        a.setDataNascimento(dataNasc);
        a.setTelefone(telefone);
        a.setEmail(email);

        return a;
    }

    private void salvarAluno() {
        try {
            Aluno aluno = getAlunoDoFormulario();
            AlunoDAO dao = new AlunoDAO();

            if (aluno.getId() == 0) {
           
                dao.inserir(aluno);
                JOptionPane.showMessageDialog(this, "Aluno inserido com sucesso!");
            } else {
       
                dao.atualizar(aluno);
                JOptionPane.showMessageDialog(this, "Aluno atualizado com sucesso!");
            }

            carregarTabela();
            limparCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar aluno:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void preencherFormularioAPartirDaTabela() {
        int row = tblAlunos.getSelectedRow();
        if (row == -1) return;

        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtNome.setText(tableModel.getValueAt(row, 1).toString());
        txtCpf.setText(tableModel.getValueAt(row, 2).toString());
        txtDataNasc.setText(tableModel.getValueAt(row, 3).toString());
        txtTelefone.setText(tableModel.getValueAt(row, 4) != null ? tableModel.getValueAt(row, 4).toString() : "");
        txtEmail.setText(tableModel.getValueAt(row, 5) != null ? tableModel.getValueAt(row, 5).toString() : "");
    }

    private void excluirAlunoSelecionado() {
        int row = tblAlunos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um aluno na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        int opc = JOptionPane.showConfirmDialog(this,
                "Confirma excluir o aluno ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            try {
                AlunoDAO dao = new AlunoDAO();
                dao.excluir(id);
                JOptionPane.showMessageDialog(this, "Aluno excluído com sucesso!");
                carregarTabela();
                limparCampos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir aluno:\n" + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

