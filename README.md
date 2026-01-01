# E-Library - Sistema de Gerenciamento de Biblioteca Distribuída

Trabalho Final da disciplina de Serviços de Suporte a Aplicações Distribuídas.

## 🚀 Tecnologias Utilizadas
* **Java 17**
* **Jakarta EE 10**
* **Servidor:** WildFly 27.0.0.Final (ou superior)
* **Banco de Dados:** PostgreSQL / H2 (Interno do Wildfly)

## 🛠️ Arquitetura
O sistema é dividido em módulos:
* **ELibrary (EJB):** Core do sistema, contém as regras de negócio, Entidades JPA e Session Beans.
* **ELibraryWeb (WAR):** API REST (JAX-RS) e GraphQL.
* **ELibraryClient (JAR):** Cliente Java SE para Importação e Testes.

## ⚙️ Como Rodar
1.  Importe os projetos no Eclipse como "Existing Projects".
2.  Configure o servidor WildFly 27+.
3.  Adicione o `ELibraryEAR` ao servidor e inicie.
4.  **Credenciais de Teste:**
    * Usuário: `admin`
    * Senha: `123` (ou a que você configurou no add-user do Wildfly)

## 🧪 Como testar o Cliente (Java SE)
1.  Vá no projeto `ELibraryClient`.
2.  Execute a classe `br.elibrary.client.Main`.
3.  Escolha a opção no menu:
    * **Opção 1:** Importação de dados (XML/JSON).
    * **Opção 2:** Teste de Mensageria (JMS/MDB).

## 📦 Funcionalidades Implementadas
* API REST (Livros e Exemplares)
* API GraphQL (Dashboard e Consultas)
* Importação de Dados (XML/JSON)
* Mensageria Assíncrona (JMS)
