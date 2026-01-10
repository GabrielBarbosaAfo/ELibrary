package br.elibrary.client;

import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;

import br.elibrary.importacao.ImportadorService;
import ssad.interfaces.ExemplarSBRemote;
import ssad.interfaces.ListaEsperasSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.interfaces.EmprestimoSBRemote;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.println(">>> [Main] Conectando ao servidor Wildfly...");
            
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
            // 1. Configuração JNDI (Comum para todos os testes)
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            Properties p = new Properties();
            p.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
            p.put(Context.PROVIDER_URL, "remote+http://localhost:8080");
            p.put(Context.SECURITY_PRINCIPAL, "appuser"); 
            p.put(Context.SECURITY_CREDENTIALS, "Senha123!");
            p.put("jboss.naming.client.ejb.context", "true");

            InitialContext ctx = new InitialContext(p);
            System.out.println(">>> [Main] Conexão estabelecida com sucesso!");

            while (true) {
                System.out.println("\n=========================================");
                System.out.println("      E-LIBRARY CLIENT - MENU DE TESTES   ");
                System.out.println("=========================================");
                System.out.println("1 - Testar Parte 3: Importação de Arquivos (XML/JSON)");
                System.out.println("2 - Testar Parte 4: Mensageria (Lista de Espera e Atrasos)");
                System.out.println("0 - Sair");
                System.out.print(">>> Escolha uma opção: ");

                String opcao = scanner.next();

                if (opcao.equals("0")) {
                    System.out.println("Encerrando cliente...");
                    break;
                }

                switch (opcao) {
                    case "1":
                        executarTesteImportacao(ctx);
                        break;
                    case "2":
                        executarTesteMensageria(ctx);
                        break;
                    default:
                        System.out.println("Opção inválida!");
                }
            }

        } catch (Exception e) {
            System.err.println(">>> ERRO FATAL DE CONEXÃO:");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    // --- LÓGICA DA PARTE 3 (IMPORTAÇÃO) ---
    private static void executarTesteImportacao(InitialContext ctx) {
        try {
            System.out.println("\n--- INICIANDO TESTE PARTE 3 (IMPORTAÇÃO) ---");
            
<<<<<<< HEAD
=======
<<<<<<< HEAD
            // Lookup dos EJBs 
=======
            // Lookup dos EJBs necessários
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            LivroSBRemote livroSB = (LivroSBRemote) ctx.lookup("ELibraryEAR/ELibrary/LivroSB!ssad.interfaces.LivroSBRemote");
            ExemplarSBRemote exemplarSB = (ExemplarSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");

            ImportadorService importador = new ImportadorService(livroSB, exemplarSB);
            
<<<<<<< HEAD
            System.out.println(">>> Processando 'doacao.xml'...");
            importador.processarArquivo("doacao.xml");
            
=======
            // Processa XML
            System.out.println(">>> Processando 'doacao.xml'...");
            importador.processarArquivo("doacao.xml");
            
            // Processa JSON
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            System.out.println(">>> Processando 'doacao.json'...");
            importador.processarArquivo("doacao.json");
            
            System.out.println(">>> Teste de Importação CONCLUÍDO.");

        } catch (Exception e) {
            System.err.println("Erro no teste de importação: " + e.getMessage());
        }
    }

    // --- LÓGICA DA PARTE 4 (MENSAGERIA) ---
    private static void executarTesteMensageria(InitialContext ctx) {
        try {
            System.out.println("\n--- INICIANDO TESTE PARTE 4 (MENSAGERIA) ---");
            
<<<<<<< HEAD
=======
            // Lookup dos EJBs
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            ExemplarSBRemote exemplarSB = (ExemplarSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");
            ListaEsperasSBRemote listaEsperaSB = (ListaEsperasSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ListaEsperasSB!ssad.interfaces.ListaEsperasSBRemote");
            EmprestimoSBRemote emprestimoSB = (EmprestimoSBRemote) ctx.lookup("ELibraryEAR/ELibrary/EmprestimoSB!ssad.interfaces.EmprestimoSBRemote");

<<<<<<< HEAD
=======
            // CENÁRIO 1: Lista de Espera
            // Supõe-se Usuário ID 1 e Livro ID 1. Se der erro, verifique o banco.
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            Long idUsuario = 1L;
            Long idLivro = 1L;
            Long idExemplar = 1L; 

            System.out.println(">>> [1] Inscrevendo usuário " + idUsuario + " na espera pelo livro " + idLivro + "...");
            try {
                listaEsperaSB.inscrever(idUsuario, idLivro);
                System.out.println("    Inscrição realizada (ou já existente).");
            } catch (Exception e) {
                System.out.println("    Aviso: " + e.getMessage());
            }

            System.out.println(">>> [2] Devolvendo exemplar " + idExemplar + " (Isso deve disparar notificação se houver espera)...");
            exemplarSB.disponibilizarExemplar(idExemplar);
            System.out.println("    Comando de devolução enviado.");

<<<<<<< HEAD
=======
            // CENÁRIO 2: Atrasos
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            System.out.println(">>> [3] Verificando atrasos (Disparo de notificações de atraso)...");
            emprestimoSB.verificarAtrasos();
            System.out.println("    Verificação solicitada.");

            System.out.println(">>> Teste de Mensageria CONCLUÍDO. Verifique o console do SERVIDOR (Wildfly) para ver as mensagens chegando!");

        } catch (Exception e) {
            System.err.println("Erro no teste de mensageria: " + e.getMessage());
            e.printStackTrace();
        }
    }
}