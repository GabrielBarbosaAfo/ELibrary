package ssad.interfaces;

import jakarta.ejb.Remote;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;
import java.util.List;

@Remote
public interface LivroSBRemote {
    void salvar(Livro livro);
    void remover(Long id) throws Exception;
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> listarLivrosSemEstoque();
    Livro buscarPorISBN(String isbn);
    Long buscarIdPorISBN(String isbn);
    List<Livro> buscarPorAutor(String autor);
    
    List<Livro> pesquisarComFiltros(String autor, StatusExemplar status);
}