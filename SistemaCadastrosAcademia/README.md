# Uc_programacao_solucao_computacional-Projeto_academia_A3
Aplicativo feito em Java para cadastro de alunos e treinos, persistindo os dados no SGBD MySQL.

# Sistema de Cadastros - Academia 🏋️‍♂️

Aplicação desktop em Java para gerenciamento de alunos e treinos de uma academia.  
O sistema permite cadastrar alunos, registrar treinos associados e consultar os dados armazenados em um banco MySQL.

---

## 🧰 Tecnologias utilizadas

- **Linguagem:** Java (JDK 25)
- **IDE recomendada:** NetBeans
- **Banco de dados:** MySQL (via **XAMPP** ou instalação separada)
- **Driver JDBC:** MySQL Connector/J **9.5**
- **Engine do banco:** InnoDB
- **Charset:** utf8mb4

---

## ✅ Pré-requisitos

Antes de rodar o projeto, você precisa ter instalado:

1. **Java JDK 25**  
   - Configurado no `PATH` do sistema.
     
2. **NetBeans** (ou outra IDE compatível com projetos Java/Ant).
   
3. **XAMPP** 
   - **Apache** e o **MySQL** precisa estar rodando.

---

## 🗄️ Configuração do banco de dados

1. Inicie o **Apache** e o **MySQL** pelo XAMPP (botão “Start”).
   
2. Acesse o MySQL via:
   - **Admin** (http://localhost/phpmyadmin/index.php)
 
3. Execute o script SQL do projeto:

   - Arquivo: `sql/academia.sql`
   - Esse script irá:
     - Criar o banco de dados **`academia`**
     - Criar as tabelas **`alunos`** e **`treinos`**
     - Configurar chaves primárias, CPF único e chave estrangeira entre `treinos.aluno_id` → `alunos.id`.


