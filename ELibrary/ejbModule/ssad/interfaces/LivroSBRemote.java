package ssad.interfaces;

<<<<<<< HEAD
import jakarta.ejb.Remote;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;
=======
import ssad.modelo.Livro;
import jakarta.ejb.Remote;
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
import java.util.List;

@Remote
public interface LivroSBRemote {
    void salvar(Livro livro);
<<<<<<< HEAD
    void remover(Long id) throws Exception;
=======
    void remover(Long id);
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> buscarPorTitulo(String titulo);
    List<Livro> listarLivrosSemEstoque();
    Livro buscarPorISBN(String isbn);
    Long buscarIdPorISBN(String isbn);
    List<Livro> buscarPorAutor(String autor);
<<<<<<< HEAD
    
    List<Livro> pesquisarComFiltros(String autor, StatusExemplar status);
=======
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
}