package ssad.sessionbeans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.StatusExemplar; 

import java.util.List;

@Stateless
public class ExemplarSB implements ExemplarSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;
    
    @EJB 
    private CatalogStatusSBRemote catalogStatus;


    @Override
    public void salvar(Exemplar exemplar) {
        if (exemplar.getId() == null) {
            em.persist(exemplar);
        } else {
            em.merge(exemplar);
        }
        catalogStatus.atualizarCache();
    }

    @Override
    public void remover(Long id) {
        Exemplar exemplar = em.find(Exemplar.class, id);
        if (exemplar != null) {
            em.remove(exemplar);
            catalogStatus.atualizarCache();
        }
    }

    @Override
    public Exemplar buscarPorId(Long id) {
        return em.find(Exemplar.class, id);
    }

    @Override
    public List<Exemplar> listarPorLivro(Long idLivro) {
        return em.createQuery("SELECT e FROM Exemplar e WHERE e.livro.id = :idLivro", Exemplar.class)
                 .setParameter("idLivro", idLivro)
                 .getResultList();
    }

    @Override
    public List<Exemplar> listarTodos() {
        return em.createQuery("SELECT e FROM Exemplar e WHERE e.status = :status", Exemplar.class)
                 .setParameter("status", StatusExemplar.DISPONIVEL) // Só traz os disponíveis
                 .getResultList();
    }
    
    @Override
    public List<Exemplar> pesquisarPorTitulo(String titulo) {
         return em.createQuery("SELECT e FROM Exemplar e WHERE lower(e.livro.titulo) LIKE :titulo AND e.status = :status", Exemplar.class)
                 .setParameter("titulo", "%" + titulo.toLowerCase() + "%")
                 .setParameter("status", StatusExemplar.DISPONIVEL) // Só traz os disponíveis
                 .getResultList();
    }
}