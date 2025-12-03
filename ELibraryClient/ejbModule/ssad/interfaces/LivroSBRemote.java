package ssad.interfaces;

import java.util.List;

import jakarta.ejb.Remote;
import ssad.modelo.Livro;

@Remote
public interface LivroSBRemote {
	void salvar(Livro livro);
    void remover(Long id);
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> buscarPorTitulo(String titulo);
	List<Livro> listarLivrosSemEstoque();
	Livro buscarPorISBN(String isbn);
	public Long buscarIdPorISBN(String isbn);
}
