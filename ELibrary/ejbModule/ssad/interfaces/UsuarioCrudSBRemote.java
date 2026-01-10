package ssad.interfaces;

import java.util.List;

import jakarta.ejb.Remote;
import ssad.modelo.Usuario;

@Remote
public interface UsuarioCrudSBRemote {
	void salvar(Usuario usuario) throws Exception;
    List<Usuario> listarTodos();
}
