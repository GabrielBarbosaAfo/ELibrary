package ssad.graphqltutorial.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import ssad.graphqltutorial.dto.*;
import ssad.graphqltutorial.service.WildflyService;

import java.util.List;

@Controller
public class BibliotecaController {

    @Autowired
    private WildflyService service;

    @QueryMapping
    public LivroDTO livroByIsbn(@Argument String isbn) {
        return service.buscarPorIsbn(isbn);
    }

    @QueryMapping
    public List<LivroDTO> livrosDisponiveis(@Argument LivroFiltro filtro) {
        return service.listarComFiltro(filtro);
    }
    
    @QueryMapping
    public List<EmprestimoDTO> emprestimosAtivosUsuario(@Argument Long usuarioId) {
        return service.emprestimosAtivos(usuarioId);
    }
    
    @QueryMapping
    public DashboardDTO dashboardBiblioteca() {
        return service.getDashboard();
    }

    @MutationMapping
    public LivroDTO cadastrarLivro(@Argument LivroInput livro) { 
        return service.cadastrarLivro(livro);
    }
    
    @MutationMapping
    public ExemplarDTO alterarStatusExemplar(@Argument Long exemplarId, @Argument String status) {
        return service.alterarStatusExemplar(exemplarId, status);
    }
}