package ssad.interfaces;

import jakarta.ejb.Remote;
import java.util.List;
import ssad.modelo.Emprestimo;

@Remote
public interface EmprestimoSBRemote {
<<<<<<< HEAD
    List<Emprestimo> listarPorUsuario(Long idUsuario);
    
    void salvar(Emprestimo emprestimo);

	void verificarAtrasos();

	List<Emprestimo> listarTodos();
=======
    // Método necessário para o GraphQL
    List<Emprestimo> listarPorUsuario(Long idUsuario);
    
    // Método útil para criar empréstimos (se precisarmos testar)
    void salvar(Emprestimo emprestimo);

	void verificarAtrasos();
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
}