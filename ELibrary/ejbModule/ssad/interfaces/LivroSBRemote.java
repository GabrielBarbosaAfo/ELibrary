package ssad.interfaces;

import ssad.modelo.Livro;
import jakarta.ejb.Remote;
import java.util.List;

@Remote
public interface LivroSBRemote {
    void salvar(Livro livro);
    void remover(Long id);
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> listarLivrosSemEstoque();
    Livro buscarPorISBN(String isbn);
    Long buscarIdPorISBN(String isbn);
    List<Livro> buscarPorAutor(String autor);
}