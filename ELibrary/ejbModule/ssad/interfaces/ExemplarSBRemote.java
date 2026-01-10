package ssad.interfaces;

import jakarta.ejb.Remote;
import ssad.modelo.Exemplar;
<<<<<<< HEAD
import ssad.modelo.StatusExemplar;
=======
<<<<<<< HEAD
import ssad.modelo.StatusExemplar;
=======

>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
import java.util.List;

@Remote
public interface ExemplarSBRemote {
    void salvar(Exemplar exemplar);
    void remover(Long id);
    Exemplar buscarPorId(Long id);
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    List<Exemplar> listarPorLivro(Long idLivro);
    List<Exemplar> listarTodos();
    List<Exemplar> pesquisarPorTitulo(String titulo);
    void disponibilizarExemplar(Long exemplarId);
    public void alterarStatus(Long id, String novoStatus);
    int contarPorStatus(StatusExemplar status);
<<<<<<< HEAD
}
=======
}
=======
    List<Exemplar> listarPorLivro(Long idLivro); 
    List<Exemplar> listarTodos();
    List<Exemplar> pesquisarPorTitulo(String titulo);
    void disponibilizarExemplar(Long exemplarId);
}
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
