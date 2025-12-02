package ssad.interfaces;

import java.util.List;

import ssad.modelo.Livro;

public interface LivroSBRemote {
	void salvar(Livro livro);
    void remover(Long id);
    Livro buscarPorId(Long id);
    List<Livro> listarTodos();
    List<Livro> buscarPorTitulo(String titulo);
	List<Livro> listarLivrosSemEstoque();
	public Livro buscarPorISBN(String isbn);
}
