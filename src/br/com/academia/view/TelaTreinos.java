/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.academia.view;

import br.com.academia.dao.AlunoDAO;
import br.com.academia.model.Aluno;
import br.com.academia.model.Treino;

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
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;


public class TelaTreinos extends JFrame {

    private JComboBox<Aluno> cbAluno;
    private JTextField txtId;
    private JTextField txtTipo;
    private JTextField txtDuracao;
    private JTextField txtDataInicio;
    private JTextArea txtDescricao;

    private JTable tblTreinos;
    private DefaultTableModel tableModel;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public TelaTreinos() {
        setTitle("Cadastro de Treinos");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();
        carregarAlunos(); 
    }


    private void initComponents() {
    JPanel root = new JPanel(new BorderLayout(8, 8));
    root.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(root);

    //TOPOAluno + dados do treino
    JPanel panelTopo = new JPanel(new BorderLayout(5, 5));

    //Seleção de aluno
    JPanel panelAluno = new JPanel(new FlowLayout(FlowLayout.LEFT));
    panelAluno.setBorder(new TitledBorder("Seleção de aluno"));
    panelAluno.add(new JLabel("Aluno:"));

    cbAluno = new JComboBox<>();
    cbAluno.setPreferredSize(new Dimension(250, 25));
    cbAluno.addActionListener(e -> carregarTabela());
    panelAluno.add(cbAluno);

    panelTopo.add(panelAluno, BorderLayout.NORTH);

    //Dados do treino
    JPanel panelDados = new JPanel(new BorderLayout(5, 5));
    panelDados.setBorder(new TitledBorder("Dados do treino"));

    JPanel panelCampos = new JPanel(new GridLayout(2, 4, 8, 8));

    panelCampos.add(new JLabel("ID:"));
    txtId = new JTextField();
    txtId.setEditable(false);
    panelCampos.add(txtId);

    panelCampos.add(new JLabel("Tipo de treino:"));
    txtTipo = new JTextField();
    panelCampos.add(txtTipo);

    panelCampos.add(new JLabel("Duração (minutos):"));
    txtDuracao = new JTextField();
    panelCampos.add(txtDuracao);

    panelCampos.add(new JLabel("Data início (dd/MM/yyyy):"));
    txtDataInicio = new JTextField();
    panelCampos.add(txtDataInicio);

    panelDados.add(panelCampos, BorderLayout.NORTH);

    //Descrição embaixo, com altura controlada
    JPanel panelDescricao = new JPanel(new BorderLayout());
    panelDescricao.setBorder(new TitledBorder("Descrição"));

    txtDescricao = new JTextArea(3, 20);
    txtDescricao.setLineWrap(true);
    txtDescricao.setWrapStyleWord(true);
    JScrollPane scrollDesc = new JScrollPane(txtDescricao);
    scrollDesc.setPreferredSize(new Dimension(100, 70)); // altura pequena

    panelDescricao.add(scrollDesc, BorderLayout.CENTER);

    panelDados.add(panelDescricao, BorderLayout.CENTER);

    panelTopo.add(panelDados, BorderLayout.CENTER);

    // coloca o topo inteiro
    root.add(panelTopo, BorderLayout.NORTH);

    //TABELA
    String[] colunas = { "ID", "Tipo", "Duração (min)", "Data início", "Descrição" };
    tableModel = new DefaultTableModel(colunas, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tblTreinos = new JTable(tableModel);
    tblTreinos.setFillsViewportHeight(true);
    tblTreinos.setRowHeight(22);
    tblTreinos.getTableHeader().setFont(
            tblTreinos.getTableHeader().getFont().deriveFont(Font.BOLD));
    JScrollPane scrollTabela = new JScrollPane(tblTreinos);

    tblTreinos.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            preencherFormularioAPartirDaTabela();
        }
    });

    root.add(scrollTabela, BorderLayout.CENTER);

    //BOTÕES
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
    btnSalvar.addActionListener(e -> salvarTreino());
    btnExcluir.addActionListener(e -> excluirTreinoSelecionado());
    btnAtualizar.addActionListener(e -> carregarTabela());

    JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
    panelBotoes.setBorder(new EmptyBorder(5, 0, 0, 0));
    panelBotoes.add(btnNovo);
    panelBotoes.add(btnSalvar);
    panelBotoes.add(btnExcluir);
    panelBotoes.add(btnAtualizar);

    root.add(panelBotoes, BorderLayout.SOUTH);
}


    
    /** Carrega alunos no combobox */
    private void carregarAlunos() {
        cbAluno.removeAllItems();
        AlunoDAO dao = new AlunoDAO();
        try {
            List<Aluno> alunos = dao.listarTodos();
            for (Aluno a : alunos) {
                cbAluno.addItem(a); 
            }
            if (cbAluno.getItemCount() > 0) {
                cbAluno.setSelectedIndex(0);
                carregarTabela();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar alunos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Carrega a tabela com os treinos do aluno selecionado */
    private void carregarTabela() {
        tableModel.setRowCount(0); 

        Aluno alunoSel = (Aluno) cbAluno.getSelectedItem();
        if (alunoSel == null) return;

        try {
            List<Treino> treinos = Treino.listarPorAluno(alunoSel.getId());
            for (Treino t : treinos) {
                Object[] row = {
                        t.getId(),
                        t.getTipoTreino(),
                        t.getDuracaoMinutos(),
                        t.getDataInicio() != null ? t.getDataInicio().format(formatter) : "",
                        t.getDescricao()
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar treinos:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Limpa os campos do formulário */
    private void limparCampos() {
        txtId.setText("");
        txtTipo.setText("");
        txtDuracao.setText("");
        txtDataInicio.setText("");
        txtDescricao.setText("");
        tblTreinos.clearSelection();
    }

    /** Monta Treino a partir do que foi digitado na tela */
    private Treino getTreinoDoFormulario() throws Exception {
        Aluno alunoSel = (Aluno) cbAluno.getSelectedItem();
        if (alunoSel == null) {
            throw new Exception("Selecione um aluno.");
        }

        String tipo = txtTipo.getText().trim();
        String duracaoStr = txtDuracao.getText().trim();
        String dataStr = txtDataInicio.getText().trim();
        String descricao = txtDescricao.getText().trim();

        if (tipo.isEmpty() || duracaoStr.isEmpty() || dataStr.isEmpty()) {
            throw new Exception("Tipo, duração e data de início são obrigatórios.");
        }

        int duracao;
        try {
            duracao = Integer.parseInt(duracaoStr);
        } catch (NumberFormatException ex) {
            throw new Exception("Duração deve ser um número inteiro em minutos.");
        }

        LocalDate dataInicio;
        try {
            dataInicio = LocalDate.parse(dataStr, formatter);
        } catch (Exception ex) {
            throw new Exception("Data de início inválida. Use o formato dd/MM/yyyy.");
        }

        Treino t = new Treino();
        
        if (!txtId.getText().trim().isEmpty()) {
            t.setId(Integer.parseInt(txtId.getText().trim()));
        }
        t.setAlunoId(alunoSel.getId());
        t.setTipoTreino(tipo);
        t.setDuracaoMinutos(duracao);
        t.setDataInicio(dataInicio);
        t.setDescricao(descricao);

        return t;
    }

    /** Quando clica em salvar */
    private void salvarTreino() {
        try {
            Treino treino = getTreinoDoFormulario();

            boolean novo = (treino.getId() == 0);

            treino.salvar();

            if (novo) {
                JOptionPane.showMessageDialog(this, "Treino inserido com sucesso!");
            } else {
                JOptionPane.showMessageDialog(this, "Treino atualizado com sucesso!");
            }

            carregarTabela();
            limparCampos();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao salvar treino:\n" + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    e.getMessage(),
                    "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Quando clica na linha da tabela, preenche os campos */
    private void preencherFormularioAPartirDaTabela() {
        int row = tblTreinos.getSelectedRow();
        if (row == -1) return;

        txtId.setText(tableModel.getValueAt(row, 0).toString());
        txtTipo.setText(tableModel.getValueAt(row, 1).toString());
        txtDuracao.setText(tableModel.getValueAt(row, 2).toString());
        txtDataInicio.setText(tableModel.getValueAt(row, 3).toString());
        Object descObj = tableModel.getValueAt(row, 4);
        txtDescricao.setText(descObj != null ? descObj.toString() : "");
    }

    /** Excluir treino selecionado na tabela */
    private void excluirTreinoSelecionado() {
        int row = tblTreinos.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Selecione um treino na tabela para excluir.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        int opc = JOptionPane.showConfirmDialog(this,
                "Confirma excluir o treino ID " + id + "?",
                "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION);

        if (opc == JOptionPane.YES_OPTION) {
            try {
                Treino.excluirPorId(id);
                JOptionPane.showMessageDialog(this, "Treino excluído com sucesso!");
                carregarTabela();
                limparCampos();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao excluir treino:\n" + e.getMessage(),
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}


