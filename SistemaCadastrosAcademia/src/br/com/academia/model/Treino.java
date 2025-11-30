/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.academia.model;

import br.com.academia.dao.conexao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Treino {

    private int id;
    private int alunoId;
    private String tipoTreino;
    private String descricao;
    private int duracaoMinutos;
    private LocalDate dataInicio;

    public Treino() { }

    public Treino(int id, int alunoId, String tipoTreino,
                  String descricao, int duracaoMinutos, LocalDate dataInicio) {
        this.id = id;
        this.alunoId = alunoId;
        this.tipoTreino = tipoTreino;
        this.descricao = descricao;
        this.duracaoMinutos = duracaoMinutos;
        this.dataInicio = dataInicio;
    }

    // ===== getters e setters =====

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAlunoId() { return alunoId; }
    public void setAlunoId(int alunoId) { this.alunoId = alunoId; }

    public String getTipoTreino() { return tipoTreino; }
    public void setTipoTreino(String tipoTreino) { this.tipoTreino = tipoTreino; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    // ===== métodos de banco de dados =====

    /** Insere ou atualiza o treino no banco */
    public void salvar() throws SQLException {
        if (this.alunoId == 0) {
            throw new SQLException("Aluno não informado para o treino.");
        }

        try (Connection con = conexao.getConnection()) {
            if (this.id == 0) {
                // INSERT
                String sql = "INSERT INTO treinos " +
                             "(aluno_id, tipo_treino, descricao, duracao_minutos, data_inicio) " +
                             "VALUES (?, ?, ?, ?, ?)";

                try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, this.alunoId);
                    ps.setString(2, this.tipoTreino);
                    ps.setString(3, this.descricao);
                    ps.setInt(4, this.duracaoMinutos);
                    ps.setDate(5, Date.valueOf(this.dataInicio));

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            this.id = rs.getInt(1);
                        }
                    }
                }

            } else {
                // UPDATE
                String sql = "UPDATE treinos SET tipo_treino = ?, descricao = ?, " +
                             "duracao_minutos = ?, data_inicio = ? WHERE id = ?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setString(1, this.tipoTreino);
                    ps.setString(2, this.descricao);
                    ps.setInt(3, this.duracaoMinutos);
                    ps.setDate(4, Date.valueOf(this.dataInicio));
                    ps.setInt(5, this.id);

                    ps.executeUpdate();
                }
            }
        }
    }

    /** Lista todos os treinos de um aluno */
    public static List<Treino> listarPorAluno(int alunoId) throws SQLException {
        List<Treino> lista = new ArrayList<>();

        String sql = "SELECT * FROM treinos WHERE aluno_id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, alunoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Treino t = new Treino();
                    t.setId(rs.getInt("id"));
                    t.setAlunoId(rs.getInt("aluno_id"));
                    t.setTipoTreino(rs.getString("tipo_treino"));
                    t.setDescricao(rs.getString("descricao"));
                    t.setDuracaoMinutos(rs.getInt("duracao_minutos"));
                    Date dt = rs.getDate("data_inicio");
                    if (dt != null) {
                        t.setDataInicio(dt.toLocalDate());
                    }
                    lista.add(t);
                }
            }
        }

        return lista;
    }

    /** Exclui um treino pelo ID */
    public static void excluirPorId(int id) throws SQLException {
        String sql = "DELETE FROM treinos WHERE id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}



