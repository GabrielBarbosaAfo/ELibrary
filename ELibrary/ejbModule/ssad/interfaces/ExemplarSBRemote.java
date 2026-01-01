package ssad.interfaces;

import jakarta.ejb.Remote;
import ssad.modelo.Exemplar;

import java.util.List;

@Remote
public interface ExemplarSBRemote {
    void salvar(Exemplar exemplar);
    void remover(Long id);
    Exemplar buscarPorId(Long id);
    List<Exemplar> listarPorLivro(Long idLivro); 
    List<Exemplar> listarTodos();
    List<Exemplar> pesquisarPorTitulo(String titulo);
    void disponibilizarExemplar(Long exemplarId);
}
