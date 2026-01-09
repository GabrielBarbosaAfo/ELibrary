package ssad.interfaces;

import jakarta.ejb.Remote;
import ssad.modelo.Usuario;
import ssad.modelo.Emprestimo;
import java.util.List;

@Remote
public interface UsuarioSBRemote {
    boolean login(String matricula, String senha);
    void logout();
    Usuario getUsuarioLogado();
    boolean realizarEmprestimo(Long idExemplar);
    boolean realizarDevolucao(Long idEmprestimo);
    List<Emprestimo> listarMeusEmprestimosAtivos();
}