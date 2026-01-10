package ssad.graphql;

import jakarta.enterprise.context.RequestScoped;
import org.eclipse.microprofile.graphql.*;
import ssad.dto.*;
import ssad.interfaces.*;
import ssad.modelo.*;
import javax.naming.InitialContext;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@GraphQLApi
@RequestScoped
public class BibliotecaGraphQL {

    
	public BibliotecaGraphQL() {
        System.out.println(">>> VERSÃO NOVA DO GRAPHQL CARREGADA COM SUCESSO! <<<");
        System.out.println(">>> SE VOCÊ ESTÁ LENDO ISSO, O DOCKER ATUALIZOU! <<<");
    }
	
    private LivroSBRemote getLivroSB() {
        try {
            return (LivroSBRemote) new InitialContext().lookup("java:global/ELibraryEAR/ELibrary/LivroSB!ssad.interfaces.LivroSBRemote");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FALHA JNDI LIVRO: " + e.getMessage());
        }
    }

    private ExemplarSBRemote getExemplarSB() {
        try {
            return (ExemplarSBRemote) new InitialContext().lookup("java:global/ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("FALHA JNDI EXEMPLAR: " + e.getMessage());
        }
    }


    @Query("dashboardBiblioteca")
    public DashboardDTO getDashboard() throws GraphQLException {
        try {
            LivroSBRemote livroSB = getLivroSB();
            ExemplarSBRemote exemplarSB = getExemplarSB();
            
            DashboardDTO d = new DashboardDTO();
            d.setTotalLivros(livroSB.listarTodos().size());
            d.setTotalDisponiveis(exemplarSB.contarPorStatus(StatusExemplar.DISPONIVEL));
            d.setTotalReservados(exemplarSB.contarPorStatus(StatusExemplar.RESERVADO));
            d.setTotalEmprestados(exemplarSB.contarPorStatus(StatusExemplar.EMPRESTADO));
            d.setTotalExemplares(d.getTotalDisponiveis() + d.getTotalReservados() + d.getTotalEmprestados());
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphQLException("Erro Dashboard: " + e.getMessage());
        }
    }

    @Query("livrosDisponiveis")
    public List<LivroDTO> livrosDisponiveis(@Name("filtro") LivroFiltro filtro) {
        try {
            LivroSBRemote livroSB = getLivroSB();
            String autor = (filtro != null) ? filtro.getAutor() : null;
            String statusStr = (filtro != null && filtro.getStatus() != null) ? filtro.getStatus() : "DISPONIVEL";
            
            StatusExemplar status = null;
            try { status = StatusExemplar.valueOf(statusStr.toUpperCase()); } catch (Exception e) {}
    
            List<Livro> livros = livroSB.pesquisarComFiltros(autor, status);
            return livros.stream().map(LivroDTO::new).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Query("livroByIsbn")
    public LivroDTO buscarLivroPorIsbn(@Name("isbn") String isbn) {
        try {
            Livro l = getLivroSB().buscarPorISBN(isbn);
            if (l == null) return null;
            return new LivroDTO(l);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- MUTATIONS ---

    @Mutation("cadastrarLivro")
    public LivroDTO cadastrarLivro(LivroInput input) throws GraphQLException {
        LivroSBRemote livroSB = getLivroSB();
        
        if (livroSB.buscarPorISBN(input.getIsbn()) != null) {
            throw new GraphQLException("ISBN duplicado: " + input.getIsbn());
        }
        try {
            Livro novo = new Livro();
            novo.setTitulo(input.getTitulo());
            novo.setIsbn(input.getIsbn());
            novo.setAutor(input.getAutor());
            novo.setAnoPublicacao(input.getAnoPublicacao());
            novo.setCategoria(input.getCategoria());
            
            livroSB.salvar(novo);
            return new LivroDTO(livroSB.buscarPorISBN(input.getIsbn()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphQLException("Erro ao salvar: " + e.getMessage());
        }
    }

    @Mutation("alterarStatusExemplar")
    public ExemplarDTO alterarStatusExemplar(@Name("exemplarId") String idStr, @Name("status") String statusStr) throws GraphQLException {
        try {
            Long id = Long.parseLong(idStr);
            ExemplarSBRemote exemplarSB = getExemplarSB();
            
            exemplarSB.alterarStatus(id, statusStr);
            return new ExemplarDTO(exemplarSB.buscarPorId(id));
        } catch (IllegalStateException e) {
            throw new GraphQLException(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphQLException("Erro interno: " + e.getMessage());
        }
    }
    
    public List<ExemplarDTO> exemplares(@Source LivroDTO livro) {
        try {
            return getExemplarSB().listarPorLivro(livro.getId()).stream()
                    .map(ExemplarDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}