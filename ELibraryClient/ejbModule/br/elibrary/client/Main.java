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
            
            // Lookup dos EJBs 
            LivroSBRemote livroSB = (LivroSBRemote) ctx.lookup("ELibraryEAR/ELibrary/LivroSB!ssad.interfaces.LivroSBRemote");
            ExemplarSBRemote exemplarSB = (ExemplarSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");

            ImportadorService importador = new ImportadorService(livroSB, exemplarSB);
            
            // Processa XML
            System.out.println(">>> Processando 'doacao.xml'...");
            importador.processarArquivo("doacao.xml");
            
            // Processa JSON
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
            
            // Lookup dos EJBs
            ExemplarSBRemote exemplarSB = (ExemplarSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");
            ListaEsperasSBRemote listaEsperaSB = (ListaEsperasSBRemote) ctx.lookup("ELibraryEAR/ELibrary/ListaEsperasSB!ssad.interfaces.ListaEsperasSBRemote");
            EmprestimoSBRemote emprestimoSB = (EmprestimoSBRemote) ctx.lookup("ELibraryEAR/ELibrary/EmprestimoSB!ssad.interfaces.EmprestimoSBRemote");

            // CENÁRIO 1: Lista de Espera
            // Supõe-se Usuário ID 1 e Livro ID 1. Se der erro, verifique o banco.
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

            // CENÁRIO 2: Atrasos
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