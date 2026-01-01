package ssad.graphql;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.graphql.*;
import ssad.dto.*;
import ssad.interfaces.*;
import ssad.modelo.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@GraphQLApi
@ApplicationScoped
public class BibliotecaGraphQL {

    private LivroSBRemote livroSB;
    private ExemplarSBRemote exemplarSB;
    private CatalogStatusSBRemote statusSB;
    private EmprestimoSBRemote emprestimoSB; // NOVO EJB

    @PostConstruct
    public void inicializar() {
        System.out.println(">>> [GraphQL] Iniciando carga dos EJBs via JNDI...");
        try {
            InitialContext ctx = new InitialContext();
            this.livroSB = (LivroSBRemote) ctx.lookup("java:global/ELibraryEAR/ELibrary/LivroSB!ssad.interfaces.LivroSBRemote");
            this.exemplarSB = (ExemplarSBRemote) ctx.lookup("java:global/ELibraryEAR/ELibrary/ExemplarSB!ssad.interfaces.ExemplarSBRemote");
            this.statusSB = (CatalogStatusSBRemote) ctx.lookup("java:global/ELibraryEAR/ELibrary/CatalogStatusSB!ssad.interfaces.CatalogStatusSBRemote");
            // Adicionando EmprestimoSB
            this.emprestimoSB = (EmprestimoSBRemote) ctx.lookup("java:global/ELibraryEAR/ELibrary/EmprestimoSB!ssad.interfaces.EmprestimoSBRemote");
            
            System.out.println(">>> [GraphQL] EJBs localizados com sucesso!");
        } catch (NamingException e) {
            System.err.println(">>> [GraphQL] ERRO CRÍTICO: Falha no Lookup JNDI.");
            e.printStackTrace();
        }
    }

    // --- QUERIES EXISTENTES ---

    @Query("dashboardBiblioteca")
    public DashboardDTO getDashboard() throws GraphQLException {
        if (statusSB == null) throw new GraphQLException("Serviço offline.");
        try {
            statusSB.atualizarCache();
            DashboardDTO d = new DashboardDTO();
            d.setTotalLivros((int) statusSB.getTotalLivros());
            d.setTotalExemplares((int) statusSB.getTotalExemplares());
            d.setTotalDisponiveis((int) statusSB.getExemplaresDisponiveis());
            
            List<Exemplar> todos = exemplarSB.listarTodos();
            if (todos != null) {
                int reservados = (int) todos.stream().filter(e -> "RESERVADO".equals(e.getStatus().name())).count();
                int emprestados = (int) todos.stream().filter(e -> "EMPRESTADO".equals(e.getStatus().name())).count();
                d.setTotalReservados(reservados);
                d.setTotalEmprestados(emprestados);
            }
            return d;
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphQLException("Erro no dashboard");
        }
    }

    @Query("livroByIsbn")
    public LivroDTO buscarLivroPorIsbn(@Name("isbn") String isbn) {
        if (livroSB == null) return null;
        Livro l = livroSB.buscarPorISBN(isbn);
        if (l == null) return null;
        return new LivroDTO(l);
    }

    // --- NOVAS QUERIES (REQUISITOS FALTANTES) ---

    @Query("livrosDisponiveis")
    @Description("Retorna livros com exemplares disponíveis conforme filtro")
    public List<LivroDTO> livrosDisponiveis(@Name("filtro") LivroFiltro filtro) {
        if (livroSB == null || exemplarSB == null) return new ArrayList<>();

        // 1. Busca todos os livros (ou filtra por autor no banco se possível para otimizar)
        List<Livro> todosLivros;
        if (filtro != null && filtro.getAutor() != null && !filtro.getAutor().isEmpty()) {
            todosLivros = livroSB.buscarPorAutor(filtro.getAutor());
        } else {
            todosLivros = livroSB.listarTodos();
        }

        List<LivroDTO> resultado = new ArrayList<>();

        // 2. Filtra na memória aqueles que têm exemplares com o status desejado
        String statusAlvo = (filtro != null && filtro.getStatus() != null) ? filtro.getStatus() : "DISPONIVEL";

        for (Livro l : todosLivros) {
            List<Exemplar> exemplares = exemplarSB.listarPorLivro(l.getId());
            boolean temDisponivel = exemplares.stream()
                    .anyMatch(e -> e.getStatus().name().equalsIgnoreCase(statusAlvo));
            
            if (temDisponivel) {
                resultado.add(new LivroDTO(l));
            }
        }
        return resultado;
    }

    @Query("emprestimosAtivosUsuario")
    public List<EmprestimoDTO> emprestimosAtivosUsuario(@Name("usuarioId") String usuarioId) throws GraphQLException {
        if (emprestimoSB == null) throw new GraphQLException("Serviço de empréstimos offline");

        try {
            Long id = Long.parseLong(usuarioId);
            List<Emprestimo> emprestimos = emprestimoSB.listarPorUsuario(id);
            
            // Filtra apenas os ativos (dataDevolucao == null)
            return emprestimos.stream()
                    .filter(e -> e.getDataDevolucao() == null)
                    .map(EmprestimoDTO::new)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new GraphQLException("Erro ao buscar empréstimos: " + e.getMessage());
        }
    }

    // --- RESOLVERS ---
    
    public List<ExemplarDTO> exemplares(@Source LivroDTO livroDto) {
        if (exemplarSB == null) return null;
        List<Exemplar> lista = exemplarSB.listarPorLivro(livroDto.getId());
        return lista.stream().map(ExemplarDTO::new).collect(Collectors.toList());
    }

    // --- MUTATIONS ---

    @Mutation("cadastrarLivro")
    public LivroDTO cadastrarLivro(LivroInput input) throws GraphQLException {
        System.out.println(">>> DEBUG: Tentando cadastrar ISBN: " + input.getIsbn());

        if (livroSB == null) {
            throw new GraphQLException("Serviço LivroSB não está injetado.");
        }

        // 1. Tenta salvar direto. Se der duplicidade, o catch pega.
        try {
            // Verifica antes só para evitar o processamento se já for óbvio
            Livro existente = livroSB.buscarPorISBN(input.getIsbn());
            if (existente != null) {
                throw new GraphQLException("Já existe um livro com o ISBN " + input.getIsbn());
            }

            // Monta o objeto
            Livro novoLivro = new Livro();
            novoLivro.setTitulo(input.getTitulo());
            novoLivro.setIsbn(input.getIsbn());
            novoLivro.setAutor(input.getAutor());
            novoLivro.setAnoPublicacao(input.getAnoPublicacao());

            // Salva
            livroSB.salvar(novoLivro);
            System.out.println(">>> DEBUG: Salvo com sucesso!");

            // 2. Recupera o ID usando o método ANTIGO (que sabemos que funciona)
            // Como acabamos de salvar, ele vai achar.
            Livro recuperado = livroSB.buscarPorISBN(input.getIsbn());
            
            if (recuperado == null) {
                 // Caso muito raro onde salva mas não acha (delay do banco)
                 // Retornamos o DTO sem ID ou lançamos erro. Vamos tentar retornar o input.
                 System.out.println(">>> DEBUG: Alerta - Não foi possível recuperar o ID imediatamente.");
                 return new LivroDTO(novoLivro); 
            }

            return new LivroDTO(recuperado);

        } catch (GraphQLException ge) {
            throw ge; // Repassa o erro de negócio (ISBN duplicado)
        } catch (Exception e) {
            // Se o erro for de Constraint (duplicidade) que passou pelo filtro
            if (e.toString().contains("ConstraintViolation") || e.getMessage().contains("duplicar")) {
                throw new GraphQLException("Já existe um livro com o ISBN " + input.getIsbn());
            }
            
            e.printStackTrace(); // Mostra o erro real no console
            throw new GraphQLException("Erro técnico ao salvar: " + e.getMessage());
        }
    }

    @Mutation("alterarStatusExemplar")
    public ExemplarDTO alterarStatusExemplar(@Name("exemplarId") String idStr, @Name("status") String statusStr) throws GraphQLException {
        if (exemplarSB == null) throw new GraphQLException("Serviço offline");
        try {
            Long id = Long.parseLong(idStr);
            Exemplar ex = exemplarSB.buscarPorId(id);
            if (ex == null) throw new GraphQLException("Exemplar não encontrado");
            
            StatusExemplar novoStatus = StatusExemplar.valueOf(statusStr.toUpperCase());
            ex.setStatus(novoStatus);
            exemplarSB.salvar(ex);
            return new ExemplarDTO(ex);
        } catch (IllegalArgumentException e) {
            throw new GraphQLException("Status inválido");
        }
    }
}