package ssad.sessionbeans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;

import java.util.List;

@Stateless
public class LivroSB implements LivroSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;

    @EJB private CatalogStatusSBRemote catalogStatus; 

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
    public void remover(Long id) throws Exception {
        // 1. REGRA DO PDF: Verifica se tem exemplares EMPRESTADOS
        // O PDF diz: "Um Livro não pode ser removido se possuir Exemplares emprestados."
        Long qtdEmprestados = (Long) em.createQuery("SELECT COUNT(e) FROM Exemplar e WHERE e.livro.id = :id AND e.status = 'EMPRESTADO'")
                .setParameter("id", id)
                .getSingleResult();

        if (qtdEmprestados > 0) {
            // Lança exceção que vai virar erro 409 lá no Resource
            throw new Exception("Não é possível remover o livro pois ele possui Exemplares EMPRESTADOS.");
        }

        // 2. Se não tiver impedimento, busca e remove
        Livro livro = em.find(Livro.class, id);
        if (livro != null) {
            em.remove(livro);
        } else {
            throw new Exception("Livro não encontrado para remoção.");
        }
    }

    @Override
    public Livro buscarPorId(Long id) {
        return em.find(Livro.class, id);
    }

    @Override
    public List<Livro> listarTodos() {
        return em.createQuery("SELECT l FROM Livro l ORDER BY l.titulo", Livro.class).getResultList();
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
            .setParameter("status", StatusExemplar.DISPONIVEL)
            .getResultList();
    }
    
    @Override
    public Livro buscarPorISBN(String isbn) {
        try {
            return em.createQuery("SELECT l FROM Livro l WHERE l.isbn = :isbn", Livro.class)
                    .setParameter("isbn", isbn)
                    .getSingleResult();
        } catch (NoResultException e) { return null; }
    }
    
    @Override
    public Long buscarIdPorISBN(String isbn) {
        try {
            return em.createQuery("SELECT l.id FROM Livro l WHERE l.isbn = :isbn", Long.class)
                     .setParameter("isbn", isbn)
                     .getSingleResult();
        } catch (Exception e) { return null; }
    }
    
    @Override
    public List<Livro> buscarPorAutor(String autor) {
        return em.createQuery("SELECT l FROM Livro l WHERE lower(l.autor) LIKE :autor", Livro.class)
                 .setParameter("autor", "%" + autor.toLowerCase() + "%")
                 .getResultList();
    }
    
    @Override
    public List<Livro> pesquisarComFiltros(String autor, StatusExemplar statusExemplar) {
        // Query mais simples: Seleciona o livro partindo do Exemplar (evita problemas de JOIN)
        StringBuilder jpql = new StringBuilder("SELECT DISTINCT e.livro FROM Exemplar e WHERE 1=1 ");
        
        if (autor != null && !autor.isEmpty()) {
            jpql.append("AND lower(e.livro.autor) LIKE :autor ");
        }
        
        if (statusExemplar != null) {
            jpql.append("AND e.status = :status ");
        }
        
        var query = em.createQuery(jpql.toString(), Livro.class);
        
        if (autor != null && !autor.isEmpty()) {
            query.setParameter("autor", "%" + autor.toLowerCase() + "%");
        }
        if (statusExemplar != null) {
            query.setParameter("status", statusExemplar);
        }
        
        return query.getResultList();
    }
}