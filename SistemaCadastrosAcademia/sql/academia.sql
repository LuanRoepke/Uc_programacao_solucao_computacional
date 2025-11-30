-- Cria o banco de dados (se ainda não existir)
CREATE DATABASE IF NOT EXISTS academia
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

-- Usa o banco de dados
USE academia;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

START TRANSACTION;

-- Tabela de alunos
CREATE TABLE IF NOT EXISTS `alunos` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `nome` varchar(100) NOT NULL,
  `cpf` varchar(14) NOT NULL,
  `data_nascimento` date NOT NULL,
  `telefone` varchar(15) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_alunos_cpf` (`cpf`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- Tabela de treinos
CREATE TABLE IF NOT EXISTS `treinos` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `aluno_id` int(10) UNSIGNED NOT NULL,
  `tipo_treino` varchar(50) NOT NULL,
  `descricao` text DEFAULT NULL,
  `duracao_minutos` int(10) UNSIGNED DEFAULT NULL,
  `data_inicio` date DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_treinos_aluno_id` (`aluno_id`),
  CONSTRAINT `fk_treinos_aluno`
    FOREIGN KEY (`aluno_id`)
    REFERENCES `alunos` (`id`)
    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

COMMIT;
