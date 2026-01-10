package ssad.interfaces;

import jakarta.ejb.Remote;
import java.util.List;
import ssad.modelo.Emprestimo;

@Remote
public interface EmprestimoSBRemote {
    List<Emprestimo> listarPorUsuario(Long idUsuario);
    
    void salvar(Emprestimo emprestimo);

	void verificarAtrasos();

	List<Emprestimo> listarTodos();
}