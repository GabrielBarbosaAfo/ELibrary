package br.elibrary.client;

import java.util.Scanner;
import javax.naming.Context;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.ExemplarSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SISTEMA DE BALCÃO (CLIENTE JAVA SE) ===");
        System.out.println("Conectando ao servidor...");

        try {
            Context context = JNDIUtil.getInitialContext();
            
            LivroSBRemote livroSB = (LivroSBRemote) JNDIUtil.lookup(context, "LivroSB", "ssad.interfaces.LivroSBRemote");
            ExemplarSBRemote exemplarSB = (ExemplarSBRemote) JNDIUtil.lookup(context, "ExemplarSB", "ssad.interfaces.ExemplarSBRemote");
            CatalogStatusSBRemote statusSB = (CatalogStatusSBRemote) JNDIUtil.lookup(context, "CatalogStatusSB", "ssad.interfaces.CatalogStatusSBRemote");

            System.out.println("Conectado com sucesso!");
            
            System.out.print("Login do Atendente: ");
            String login = scanner.nextLine();
            System.out.println("Bem-vindo(a), " + login + "!\n");

            int opcao = 0;
            while (opcao != 9) {
                System.out.println("---------------------------------");
                System.out.println("1 - Ver Status da Biblioteca (Singleton)");
                System.out.println("2 - Cadastrar Novo Livro + Exemplar (Stateless)");
                System.out.println("9 - Sair");
                System.out.print("Escolha: ");
                
                try {
                    opcao = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) { opcao = 0; }

                switch (opcao) {
                    case 1:
                        statusSB.atualizarCache(); 
                        System.out.println("\n--- STATUS DO ACERVO ---");
                        System.out.println("Total de Títulos: " + statusSB.getTotalLivros());
                        System.out.println("Total de Exemplares: " + statusSB.getTotalExemplares());
                        System.out.println("Disponíveis agora: " + statusSB.getExemplaresDisponiveis());
                        break;

                    case 2:
                        System.out.println("\n--- NOVO LIVRO ---");
                        Livro l = new Livro();
                        
                        System.out.print("Título: ");
                        l.setTitulo(scanner.nextLine());
                        
                        System.out.print("ISBN: ");
                        l.setIsbn(scanner.nextLine());
                        
                        System.out.print("Autor: ");
                        l.setAutor(scanner.nextLine());
                        
                        try {
                            livroSB.salvar(l);
                            System.out.println(">> Livro salvo!");
                            
                            Exemplar ex = new Exemplar();
                            ex.setLivro(l);
                            ex.setStatus(StatusExemplar.DISPONIVEL);
                            ex.setCodigoInterno(l.getIsbn() + "-SE-" + System.currentTimeMillis());
                            exemplarSB.salvar(ex);
                            
                            System.out.println(">> Exemplar físico gerado e estocado.");
                        } catch (Exception e) {
                            System.out.println("ERRO: " + e.getMessage());
                        }
                        break;

                    case 9:
                        System.out.println("Fechando sistema...");
                        break;
                        
                    default:
                        System.out.println("Opção inválida.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO DE CONEXÃO:");
            System.err.println("Verifique se o servidor Wildfly está rodando.");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}