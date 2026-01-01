package ssad.interfaces;

import jakarta.ejb.Remote;
import java.util.List;
import ssad.modelo.Emprestimo;

@Remote
public interface EmprestimoSBRemote {
    // Método necessário para o GraphQL
    List<Emprestimo> listarPorUsuario(Long idUsuario);
    
    // Método útil para criar empréstimos (se precisarmos testar)
    void salvar(Emprestimo emprestimo);

	void verificarAtrasos();
}