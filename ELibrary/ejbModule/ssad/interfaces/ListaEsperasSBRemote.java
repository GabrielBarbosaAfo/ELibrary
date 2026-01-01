package ssad.interfaces;

import jakarta.ejb.Remote;

@Remote
public interface ListaEsperasSBRemote {
    void inscrever(Long usuarioId, Long livroId);
}