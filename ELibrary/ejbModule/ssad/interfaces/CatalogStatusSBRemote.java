package ssad.interfaces;

import jakarta.ejb.Remote;

@Remote
public interface CatalogStatusSBRemote {
	long getTotalLivros();
    long getTotalExemplares();
    long getExemplaresDisponiveis();
    
    void atualizarCache();
}
