package ssad.graphql;

<<<<<<< HEAD
import jakarta.enterprise.context.RequestScoped;
=======
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
import org.eclipse.microprofile.graphql.*;
import ssad.dto.*;
import ssad.interfaces.*;
import ssad.modelo.*;
import javax.naming.InitialContext;
<<<<<<< HEAD
=======
import javax.naming.NamingException;
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

@GraphQLApi
<<<<<<< HEAD
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
=======
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
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
        }
    }

    @Query("livroByIsbn")
    public LivroDTO buscarLivroPorIsbn(@Name("isbn") String isbn) {
<<<<<<< HEAD
        try {
            Livro l = getLivroSB().buscarPorISBN(isbn);
            if (l == null) return null;
            return new LivroDTO(l);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

=======
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

>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
    // --- MUTATIONS ---

    @Mutation("cadastrarLivro")
    public LivroDTO cadastrarLivro(LivroInput input) throws GraphQLException {
<<<<<<< HEAD
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
=======
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
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
        }
    }

    @Mutation("alterarStatusExemplar")
    public ExemplarDTO alterarStatusExemplar(@Name("exemplarId") String idStr, @Name("status") String statusStr) throws GraphQLException {
<<<<<<< HEAD
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
=======
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
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
        }
    }
}