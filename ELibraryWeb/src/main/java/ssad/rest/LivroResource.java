package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.ExemplarDTO;
import ssad.dto.LivroDTO; 
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
    private ExemplarSBRemote exemplarSB; 

    // --- REQUISITO 1: GET com filtros  ---
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarLivros(
            @QueryParam("autor") String autor,
            @QueryParam("categoria") String categoria) {
        
        List<Livro> livros;
        
        if (autor != null && !autor.trim().isEmpty()) {
            livros = livroSB.buscarPorAutor(autor);
        } else {
            // Se não tem filtro, traz todos
            livros = livroSB.listarTodos();
        }

        // Converte para DTO para não expor entidade JPA
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
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) 
    public Response cadastrar(
            @FormParam("titulo") String titulo,
            @FormParam("isbn") String isbn,
            @FormParam("autor") String autor,
            @FormParam("ano") Integer ano, // Recebe campos individuais, não um JSON
            @Context UriInfo uriInfo) {

        if (titulo == null || isbn == null || autor == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Campos obrigatórios: titulo, isbn, autor").build();
        }

        if (livroSB.buscarPorISBN(isbn) != null) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("Já existe um livro com este ISBN.").build();
        }

        Livro novoLivro = new Livro();
        novoLivro.setTitulo(titulo);
        novoLivro.setIsbn(isbn);
        novoLivro.setAutor(autor);
        novoLivro.setAnoPublicacao(ano);

        livroSB.salvar(novoLivro);

        Long idGerado = livroSB.buscarIdPorISBN(isbn);

        URI uriLivroCriado = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(idGerado))
                .build();
        
        return Response.seeOther(uriLivroCriado).build();
    }
    
    // PUT (Atualizar) 
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
    
    // --- REQUISITO: POST /livros/{id}/exemplares ---
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
        String codigoGerado = livro.getIsbn() + "-" + System.currentTimeMillis();
        novoExemplar.setCodigoInterno(codigoGerado);

        exemplarSB.salvar(novoExemplar);

        // --- CORREÇÃO: Recuperar o ID gerado ---
        // Como o EJB é remoto, o 'novoExemplar' local continua com ID null.
        // Vamos buscar no banco pelo código único que acabamos de gerar.
        // (Isso evita termos que alterar a Interface do EJB de novo)
        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);
        
        Exemplar exemplarSalvo = exemplares.stream()
                .filter(e -> e.getCodigoInterno().equals(codigoGerado))
                .findFirst()
                .orElse(novoExemplar); // Se der azar de não achar, retorna o sem ID mesmo (mas vai achar)

        return Response.status(Response.Status.CREATED)
                .entity(new ExemplarDTO(exemplarSalvo))
                .build();
    }

    // --- REQUISITO: GET /livros/{id}/exemplares ---
    // Listar exemplares de um livro (filtro opcional por status)
    @GET
    @Path("/{id}/exemplares")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarExemplaresDoLivro(
            @PathParam("id") Long idLivro,
            @QueryParam("status") String statusFiltro) {
        
        // Verifica se o livro existe
        if (livroSB.buscarPorId(idLivro) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);

        // Filtra na memória se houver query param (ex: ?status=DISPONIVEL)
        if (statusFiltro != null && !statusFiltro.isEmpty()) {
            exemplares = exemplares.stream()
                .filter(e -> e.getStatus().name().equalsIgnoreCase(statusFiltro))
                .collect(Collectors.toList());
        }

        // Converte para DTO
        List<ExemplarDTO> dtos = exemplares.stream()
                .map(ExemplarDTO::new)
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        try {
            livroSB.remover(id); // Chama o EJB que tem a regra de proteção
            return Response.noContent().build(); // Retorna 204 (Sucesso, sem conteúdo) se deletar
        } catch (Exception e) {
            // Se o EJB bloquear (por ter exemplares emprestados), cai aqui
            return Response.status(Response.Status.CONFLICT)
                    .entity("ERRO DE REGRA: " + e.getMessage()) // Retorna 409 Conflict
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
}