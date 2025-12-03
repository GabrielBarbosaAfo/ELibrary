# E-Library - Sistema de Gerenciamento de Biblioteca

Sistema desenvolvido como trabalho final da disciplina de Desenvolvimento de Sistemas Corporativos. O projeto utiliza arquitetura distribuída com Java EE (Jakarta EE), EJB, JPA e JSF.

## 🚀 Tecnologias Utilizadas
* **Java:** 17
* **Servidor de Aplicação:** WildFly 27+
* **Banco de Dados:** PostgreSQL (Configurado via `standalone.xml`) / H2 (Memória para testes rápidos)
* **Frameworks:** EJB 3.x, JSF (Jakarta Faces), JPA (Hibernate)
* **IDE Recomendada:** Eclipse IDE for Enterprise Java

## 📦 Estrutura do Projeto
O projeto é um Enterprise Archive (EAR) composto por:
1.  **ELibrary (EJB):** Contém as Entidades JPA e os Session Beans (Lógica de Negócio).
2.  **ELibraryWeb (WAR):** Contém as páginas `.xhtml` e Managed Beans.
3.  **ELibraryClient (Java SE):** Aplicação Desktop para acesso remoto (Balcão).

## 🛠️ Como Rodar

### 1. Configuração do Banco
O projeto está configurado para criar as tabelas automaticamente (`hibernate.hbm2ddl.auto=create`).
Caso deseje popular o banco com dados iniciais, execute o script `seed.sql` no seu gerenciador de banco de dados.

### 2. Deploy no Wildfly
1.  Importe o projeto no Eclipse.
2.  Adicione o servidor Wildfly 27+ na aba Servers.
3.  Adicione o módulo `ELibraryEAR` ao servidor.
4.  Inicie o servidor.

### 3. Acesso ao Sistema
* **URL:** `http://localhost:8080/ELibraryWeb`
* **Admin Padrão:**
    * Login: `admin`
    * Senha: `123`

### 4. Rodar o Cliente Desktop
1.  No Eclipse, abra o projeto `ELibraryClient`.
2.  Execute a classe `ssad.client.Main` como **Java Application**.
3.  Siga as instruções no Console.

## 📋 Funcionalidades Implementadas
* [x] Login com níveis de acesso (Admin, Aluno, Professor)
* [x] Cadastro de Livros e Exemplares (Admin)
* [x] Consulta e Empréstimo de Livros (Web e Desktop)
* [x] Devolução de Livros
* [x] Dashboard com indicadores em tempo real (Singleton)
