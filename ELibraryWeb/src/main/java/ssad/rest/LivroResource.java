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
            livros = livroSB.listarTodos();
        }

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
            @FormParam("ano") Integer ano, 
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

        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);
        
        Exemplar exemplarSalvo = exemplares.stream()
                .filter(e -> e.getCodigoInterno().equals(codigoGerado))
                .findFirst()
                .orElse(novoExemplar); 

        return Response.status(Response.Status.CREATED)
                .entity(new ExemplarDTO(exemplarSalvo))
                .build();
    }

    // --- REQUISITO: GET /livros/{id}/exemplares ---
    @GET
    @Path("/{id}/exemplares")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listarExemplaresDoLivro(
            @PathParam("id") Long idLivro,
            @QueryParam("status") String statusFiltro) {
        
        if (livroSB.buscarPorId(idLivro) == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Exemplar> exemplares = exemplarSB.listarPorLivro(idLivro);

        if (statusFiltro != null && !statusFiltro.isEmpty()) {
            exemplares = exemplares.stream()
                .filter(e -> e.getStatus().name().equalsIgnoreCase(statusFiltro))
                .collect(Collectors.toList());
        }

        List<ExemplarDTO> dtos = exemplares.stream()
                .map(ExemplarDTO::new)
                .collect(Collectors.toList());

        return Response.ok(dtos).build();
    }
    
    @DELETE
    @Path("/{id}")
    public Response remover(@PathParam("id") Long id) {
        try {
            livroSB.remover(id); 
            return Response.noContent().build(); 
        } catch (Exception e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity("ERRO DE REGRA: " + e.getMessage()) 
                    .type(MediaType.TEXT_PLAIN)
                    .build();
        }
    }
    
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
}