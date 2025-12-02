-- E-LIBRARY SEED / DUMP

-- 1. Inserção de LIVROS 
INSERT INTO LIVRO (id, anoPublicacao, autor, editora, isbn, titulo) VALUES 
(1, 2020, 'Robert C. Martin', 'Alta Books', '978-8550811765', 'Clean Architecture'),
(2, 2008, 'Robert C. Martin', 'Prentice Hall', '978-0132350884', 'Clean Code'),
(3, 1994, 'Erich Gamma', 'Addison-Wesley', '978-0201633610', 'Design Patterns'),
(4, 2019, 'Andrew Hunt', 'Addison-Wesley', '978-0135957059', 'The Pragmatic Programmer'),
(5, 2017, 'Kathy Sierra', 'O Reilly', '978-1491900864', 'Head First Java');

-- 2. Inserção de EXEMPLARES 
INSERT INTO EXEMPLAR (id, codigoInterno, status, ID_LIVRO) VALUES 
(1, 'CA-001', 'DISPONIVEL', 1),
(2, 'CA-002', 'DISPONIVEL', 1),
(3, 'CC-001', 'DISPONIVEL', 2),
(4, 'CC-002', 'DISPONIVEL', 2),
(5, 'CC-003', 'DISPONIVEL', 2),
(6, 'DP-001', 'DISPONIVEL', 3),
(7, 'PP-001', 'DISPONIVEL', 4),
(8, 'HF-001', 'DISPONIVEL', 5);

-- 3. Inserção de USUÁRIOS
-- Admin (Senha: 123)
INSERT INTO USUARIO (id, nome, matricula, email, senhaHash, tipo) VALUES 
(1, 'Administrador', 'admin', 'admin@elibrary.com', '123', 'ADMIN');

-- Professor (Senha: 123)
INSERT INTO USUARIO (id, nome, matricula, email, senhaHash, tipo) VALUES 
(2, 'Prof. Silva', 'silva', 'silva@prof.com', '123', 'PROFESSOR');

-- Aluno (Senha: 123)
INSERT INTO USUARIO (id, nome, matricula, email, senhaHash, tipo) VALUES 
(3, 'João Aluno', 'joao', 'joao@aluno.com', '123', 'ALUNO');

