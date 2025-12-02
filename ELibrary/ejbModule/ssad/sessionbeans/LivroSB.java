package ssad.sessionbeans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.modelo.Livro;

import java.util.List;

@Stateless
public class LivroSB implements LivroSBRemote {

	@PersistenceContext(unitName = "ELibrary")
    private EntityManager em;

    @EJB 
    private CatalogStatusSBRemote catalogStatus; 

    @Override
    public void salvar(Livro livro) {
        if (livro.getId() == null) {
            em.persist(livro);
        } else {
            em.merge(livro);
        }
        catalogStatus.atualizarCache(); 
    }

    @Override
    public void remover(Long id) {
        Livro livro = em.find(Livro.class, id);
        if (livro != null) {
            em.remove(livro);
            catalogStatus.atualizarCache(); 
        }
    }

    @Override
    public Livro buscarPorId(Long id) {
        return em.find(Livro.class, id);
    }

    @Override
    public List<Livro> listarTodos() {
        return em.createQuery("SELECT l FROM Livro l", Livro.class).getResultList();
    }

    @Override
    public List<Livro> buscarPorTitulo(String titulo) {
        return em.createQuery("SELECT l FROM Livro l WHERE lower(l.titulo) LIKE :titulo", Livro.class)
                 .setParameter("titulo", "%" + titulo.toLowerCase() + "%")
                 .getResultList();
    }

    @Override
    public List<Livro> listarLivrosSemEstoque() {
        return em.createQuery(
            "SELECT l FROM Livro l WHERE " +
            "(SELECT COUNT(e) FROM Exemplar e WHERE e.livro = l AND e.status = :status) = 0", 
            Livro.class)
            .setParameter("status", ssad.modelo.StatusExemplar.DISPONIVEL)
            .getResultList();
    }
    
    @Override
    public Livro buscarPorISBN(String isbn) {
        try {
            return em.createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }
    
}
