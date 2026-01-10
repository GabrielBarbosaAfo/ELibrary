package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.ExemplarDTO;
<<<<<<< HEAD
import ssad.dto.LivroDTO; 
=======
<<<<<<< HEAD
import ssad.dto.LivroDTO; 
=======
import ssad.dto.LivroDTO; // Mantenha o DTO para o retorno do GET (JSON)
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
import ssad.interfaces.ExemplarSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/livros")
public class LivroResource {

    @EJB
    private LivroSBRemote livroSB;
    
    @EJB
<<<<<<< HEAD
    private ExemplarSBRemote exemplarSB; 

    // --- REQUISITO 1: GET com filtros  ---
=======
<<<<<<< HEAD
    private ExemplarSBRemote exemplarSB; 

    // --- REQUISITO 1: GET com filtros  ---
=======
    private ExemplarSBRemote exemplarSB; // Injete o ExemplarSB

    // --- REQUISITO 1: GET com filtros opcionais ---
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarLivros(
            @QueryParam("autor") String autor,
<<<<<<< HEAD
            @QueryParam("categoria") String categoria) {
=======
<<<<<<< HEAD
            @QueryParam("categoria") String categoria) {
=======
            @QueryParam("categoria") String categoria) { // Categoria mantida, mas sem implementação no banco por enquanto
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        
        List<Livro> livros;
        
        if (autor != null && !autor.trim().isEmpty()) {
<<<<<<< HEAD
            livros = livroSB.buscarPorAutor(autor);
        } else {
            livros = livroSB.listarTodos();
        }

=======
<<<<<<< HEAD
=======
            // Agora usa o método real do EJB (Performance correta)
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
            livros = livroSB.buscarPorAutor(autor);
        } else {
            // Se não tem filtro, traz todos
            livros = livroSB.listarTodos();
        }

        // Converte para DTO para não expor entidade JPA
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        List<LivroDTO> dtos = livros.stream().map(LivroDTO::new).collect(Collectors.toList());
        
        return Response.ok(dtos).build();
    }

    // --- REQUISITO 2: GET por ID ---
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarPorId(@PathParam("id") Long id) {
        Livro l = livroSB.buscarPorId(id);
        if (l == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new LivroDTO(l)).build();
    }

    // --- REQUISITO 3: POST via Formulário HTML e Redirect 303 ---
    @POST
<<<<<<< HEAD
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
=======
<<<<<<< HEAD
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
=======
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // Mudança Crucial: Aceita formulário html padrão
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    public Response cadastrar(
            @FormParam("titulo") String titulo,
            @FormParam("isbn") String isbn,
            @FormParam("autor") String autor,
<<<<<<< HEAD
            @FormParam("ano") Integer ano, 
            @Context UriInfo uriInfo) {

        if (titulo == null || isbn == null || autor == null) {
=======
            @FormParam("ano") Integer ano, // Recebe campos individuais, não um JSON
            @Context UriInfo uriInfo) {

<<<<<<< HEAD
        if (titulo == null || isbn == null || autor == null) {
=======
        // 1. Validação de campos obrigatórios
        if (titulo == null || isbn == null || autor == null) {
            // Retorna 400 Bad Request
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campos obrigatórios: titulo, isbn, autor").build();
        }

<<<<<<< HEAD
        if (livroSB.buscarPorISBN(isbn) != null) {
=======
<<<<<<< HEAD
        if (livroSB.buscarPorISBN(isbn) != null) {
=======
        // 2. Validação de Unicidade do ISBN
        if (livroSB.buscarPorISBN(isbn) != null) {
            // Retorna 409 Conflict
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            return Response.status(Response.Status.CONFLICT)
                    .entity("Já existe um livro com este ISBN.").build();
        }

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
        // 3. Monta e Salva a Entidade
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        Livro novoLivro = new Livro();
        novoLivro.setTitulo(titulo);
        novoLivro.setIsbn(isbn);
        novoLivro.setAutor(autor);
        novoLivro.setAnoPublicacao(ano);

        livroSB.salvar(novoLivro);

<<<<<<< HEAD
        Long idGerado = livroSB.buscarIdPorISBN(isbn);

=======
<<<<<<< HEAD
        Long idGerado = livroSB.buscarIdPorISBN(isbn);

=======
        // 4. Recupera o ID gerado
        Long idGerado = livroSB.buscarIdPorISBN(isbn);

        // 5. Redirecionamento 303 (See Other) para /livros/{id}
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        URI uriLivroCriado = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(idGerado))
                .build();
        
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        return Response.seeOther(uriLivroCriado).build();
    }
    
    // PUT (Atualizar) 
<<<<<<< HEAD
=======
=======
        // Response.seeOther gera automaticamente o status 303 e o header Location
        return Response.seeOther(uriLivroCriado).build();
    }
    
    // PUT (Atualizar) - Vamos manter JSON ou quer mudar para Form também?
    // O enunciado não especifica o formato do PUT, apenas do POST.
    // Vou manter JSON por ser padrão de mercado, mas se quiser mudamos.
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizar(@PathParam("id") Long id, LivroDTO dto) {
        Livro existente = livroSB.buscarPorId(id);
        if (existente == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existente.setTitulo(dto.getTitulo());
        existente.setAutor(dto.getAutor());
        livroSB.salvar(existente);
        return Response.ok(new LivroDTO(existente)).build();
    }
    
<<<<<<< HEAD
    // --- REQUISITO: POST /livros/{id}/exemplares ---
=======
<<<<<<< HEAD
    // --- REQUISITO: POST /livros/{id}/exemplares ---
=======
 // --- REQUISITO: POST /livros/{id}/exemplares ---
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    @POST
    @Path("/{id}/exemplares")
    @Produces(MediaType.APPLICATION_JSON)
    public Response criarExemplar(@PathParam("id") Long idLivro) {
        Livro livro = livroSB.buscarPorId(idLivro);
        if (livro == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Livro não encontrado").build();
        }

        Exemplar novoExemplar = new ssad.modelo.Exemplar();
        novoExemplar.setLivro(livro);
        novoExemplar.setStatus(StatusExemplar.DISPONIVEL);
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
        // Gera o código e guarda numa variável para buscarmos depois
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        String codigoGerado = livro.getIsbn() + "-" + System.currentTimeMillis();
        novoExemplar.setCodigoInterno(codigoGerado);

        exemplarSB.salvar(novoExemplar);

<<<<<<< HEAD
=======
        // --- CORREÇÃO: Recuperar o ID gerado ---
        // Como o EJB é remoto, o 'novoExemplar' local continua com ID null.
        // Vamos buscar no banco pelo código único que acabamos de gerar.
        // (Isso evita termos que alterar a Interface do EJB de novo)
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);
        
        Exemplar exemplarSalvo = exemplares.stream()
                .filter(e -> e.getCodigoInterno().equals(codigoGerado))
                .findFirst()
<<<<<<< HEAD
                .orElse(novoExemplar); 
=======
                .orElse(novoExemplar); // Se der azar de não achar, retorna o sem ID mesmo (mas vai achar)
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29

        return Response.status(Response.Status.CREATED)
                .entity(new ExemplarDTO(exemplarSalvo))
                .build();
    }

    // --- REQUISITO: GET /livros/{id}/exemplares ---
<<<<<<< HEAD
=======
    // Listar exemplares de um livro (filtro opcional por status)
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    @GET
    @Path("/{id}/exemplares")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarExemplaresDoLivro(
            @PathParam("id") Long idLivro,
            @QueryParam("status") String statusFiltro) {
        
<<<<<<< HEAD
=======
        // Verifica se o livro existe
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        if (livroSB.buscarPorId(idLivro) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);

<<<<<<< HEAD
=======
        // Filtra na memória se houver query param (ex: ?status=DISPONIVEL)
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        if (statusFiltro != null && !statusFiltro.isEmpty()) {
            exemplares = exemplares.stream()
                .filter(e -> e.getStatus().name().equalsIgnoreCase(statusFiltro))
                .collect(Collectors.toList());
        }

<<<<<<< HEAD
=======
        // Converte para DTO
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        List<ExemplarDTO> dtos = exemplares.stream()
                .map(ExemplarDTO::new)
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    
    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        try {
<<<<<<< HEAD
            livroSB.remover(id); 
            return Response.noContent().build(); 
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("ERRO DE REGRA: " + e.getMessage()) 
=======
            livroSB.remover(id); // Chama o EJB que tem a regra de proteção
            return Response.noContent().build(); // Retorna 204 (Sucesso, sem conteúdo) se deletar
        } catch (Exception e) {
            // Se o EJB bloquear (por ter exemplares emprestados), cai aqui
            return Response.status(Response.Status.CONFLICT)
                    .entity("ERRO DE REGRA: " + e.getMessage()) // Retorna 409 Conflict
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
<<<<<<< HEAD
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarJson(LivroDTO dto) { // Receives a DTO object directly
        
        if (dto.getTitulo() == null || dto.getIsbn() == null || dto.getAutor() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campos obrigatórios: titulo, isbn, autor").build();
        }

        if (livroSB.buscarPorISBN(dto.getIsbn()) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Já existe um livro com este ISBN.").build();
        }

        Livro novoLivro = new Livro();
        novoLivro.setTitulo(dto.getTitulo());
        novoLivro.setIsbn(dto.getIsbn());
        novoLivro.setAutor(dto.getAutor());
        novoLivro.setAnoPublicacao(dto.getAnoPublicacao());

        livroSB.salvar(novoLivro);
        
        Long id = livroSB.buscarIdPorISBN(dto.getIsbn());
        dto.setId(id);

        return Response.status(Response.Status.CREATED).entity(dto).build();
    }
=======
=======
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
}