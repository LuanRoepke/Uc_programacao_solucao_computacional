/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.academia.dao;

import br.com.academia.model.Aluno;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {

    public void inserir(Aluno aluno) throws SQLException {
        String sql = "INSERT INTO alunos (nome, cpf, data_nascimento, telefone, email) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getCpf());
            ps.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            ps.setString(4, aluno.getTelefone());
            ps.setString(5, aluno.getEmail());

            ps.executeUpdate();
        }
    }

    public List<Aluno> listarTodos() throws SQLException {
        List<Aluno> lista = new ArrayList<>();
        String sql = "SELECT * FROM alunos";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Aluno a = new Aluno();
                a.setId(rs.getInt("id"));
                a.setNome(rs.getString("nome"));
                a.setCpf(rs.getString("cpf"));
                a.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                a.setTelefone(rs.getString("telefone"));
                a.setEmail(rs.getString("email"));
                lista.add(a);
            }
        }

        return lista;
    }

    public void atualizar(Aluno aluno) throws SQLException {
        String sql = "UPDATE alunos SET nome = ?, cpf = ?, data_nascimento = ?, " +
                     "telefone = ?, email = ? WHERE id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, aluno.getNome());
            ps.setString(2, aluno.getCpf());
            ps.setDate(3, Date.valueOf(aluno.getDataNascimento()));
            ps.setString(4, aluno.getTelefone());
            ps.setString(5, aluno.getEmail());
            ps.setInt(6, aluno.getId());

            ps.executeUpdate();
        }
    }

    public void excluir(int id) throws SQLException {
        String sql = "DELETE FROM alunos WHERE id = ?";

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public Aluno buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM alunos WHERE id = ?";
        Aluno a = null;

        try (Connection con = conexao.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    a = new Aluno();
                    a.setId(rs.getInt("id"));
                    a.setNome(rs.getString("nome"));
                    a.setCpf(rs.getString("cpf"));
                    a.setDataNascimento(rs.getDate("data_nascimento").toLocalDate());
                    a.setTelefone(rs.getString("telefone"));
                    a.setEmail(rs.getString("email"));
                }
            }
        }

        return a;
    }
}
