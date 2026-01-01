package br.elibrary.importacao;

import java.io.File;
import java.util.List;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import com.fasterxml.jackson.databind.ObjectMapper;

import ssad.interfaces.ExemplarSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;

public class ImportadorService {

    private LivroSBRemote livroSB;
    private ExemplarSBRemote exemplarSB;

    public ImportadorService(LivroSBRemote livroSB, ExemplarSBRemote exemplarSB) {
        this.livroSB = livroSB;
        this.exemplarSB = exemplarSB;
    }

    public void processarArquivo(String caminhoArquivo) {
        System.out.println("\n>>> [IMPORTADOR] Lendo arquivo: " + caminhoArquivo);
        File arquivo = new File(caminhoArquivo);
        
        if (!arquivo.exists()) {
            System.err.println(">>> ERRO: Arquivo não encontrado: " + caminhoArquivo);
            return;
        }

        try {
            List<LivroImportDTO> listaParaImportar = null;

            if (caminhoArquivo.endsWith(".xml")) {
                JAXBContext context = JAXBContext.newInstance(BibliotecaImportWrapper.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                BibliotecaImportWrapper wrapper = (BibliotecaImportWrapper) unmarshaller.unmarshal(arquivo);
                listaParaImportar = wrapper.getLivros();
            } 
            else if (caminhoArquivo.endsWith(".json")) {
                ObjectMapper mapper = new ObjectMapper();
                BibliotecaImportWrapper wrapper = mapper.readValue(arquivo, BibliotecaImportWrapper.class);
                listaParaImportar = wrapper.getLivros();
            } else {
                System.out.println(">>> Formato não suportado. Use .xml ou .json");
                return;
            }

            if (listaParaImportar != null) {
                System.out.println(">>> Encontrados " + listaParaImportar.size() + " livros para processar.");
                for (LivroImportDTO dto : listaParaImportar) {
                    importarLivro(dto);
                }
            }
            System.out.println(">>> Processo finalizado para: " + caminhoArquivo);

        } catch (Exception e) {
            System.err.println(">>> ERRO FATAL NA IMPORTAÇÃO: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void importarLivro(LivroImportDTO dto) {
        try {
            System.out.print("Processando ISBN " + dto.getIsbn() + "... ");
            
            Livro livro = livroSB.buscarPorISBN(dto.getIsbn());
            
            if (livro == null) {
                livro = dto.toEntity();
                livroSB.salvar(livro);
                
                livro = livroSB.buscarPorISBN(dto.getIsbn()); 
                System.out.print("[NOVO LIVRO CADASTRADO] ");
            } else {
                System.out.print("[LIVRO JÁ EXISTENTE] ");
            }

            int qtd = dto.getQuantidadeExemplares();
            if (qtd > 0) {
                for (int i = 0; i < qtd; i++) {
                    Exemplar ex = new Exemplar();
                    ex.setLivro(livro);
                    ex.setStatus(StatusExemplar.DISPONIVEL);
                    ex.setCodigoInterno(dto.getIsbn() + "-CP-" + System.currentTimeMillis() + "-" + i);
                    exemplarSB.salvar(ex);
                }
                System.out.println("-> + " + qtd + " exemplares adicionados.");
            } else {
                System.out.println("-> Nenhum exemplar solicitado.");
            }

        } catch (Exception e) {
            System.out.println("\n>>> ERRO ao importar item: " + e.getMessage());
        }
    }
}