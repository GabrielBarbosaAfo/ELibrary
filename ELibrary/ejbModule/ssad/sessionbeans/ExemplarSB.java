package ssad.sessionbeans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.ListaEspera;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;
import java.util.List;

@Stateless
public class ExemplarSB implements ExemplarSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;
    
    @EJB private CatalogStatusSBRemote catalogStatus;
    
    @EJB
    private MensageiroSB mensageiro; // Injeção do Produtor

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
    public Exemplar buscarPorId(Long id) { return em.find(Exemplar.class, id); }

    @Override
    public List<Exemplar> listarPorLivro(Long idLivro) {
        return em.createQuery("SELECT e FROM Exemplar e WHERE e.livro.id = :idLivro", Exemplar.class)
                 .setParameter("idLivro", idLivro)
                 .getResultList();
    }

    // LISTA APENAS DISPONÍVEIS
    @Override
    public List<Exemplar> listarTodos() {
        return em.createQuery("SELECT e FROM Exemplar e WHERE e.status = :status", Exemplar.class)
                 .setParameter("status", StatusExemplar.DISPONIVEL)
                 .getResultList();
    }
    
    @Override
    public List<Exemplar> pesquisarPorTitulo(String titulo) {
         return em.createQuery("SELECT e FROM Exemplar e WHERE lower(e.livro.titulo) LIKE :titulo AND e.status = :status", Exemplar.class)
                 .setParameter("titulo", "%" + titulo.toLowerCase() + "%")
                 .setParameter("status", StatusExemplar.DISPONIVEL)
                 .getResultList();
    }
    
    @Override
    public void disponibilizarExemplar(Long exemplarId) {
        Exemplar exemplar = em.find(Exemplar.class, exemplarId);
        if (exemplar == null) return;

        // 1. Verifica se estava tudo emprestado (count de disponiveis == 0)
        Long qtdDisponivel = em.createQuery(
            "SELECT COUNT(e) FROM Exemplar e WHERE e.livro = :livro AND e.status = :status", Long.class)
            .setParameter("livro", exemplar.getLivro())
            .setParameter("status", StatusExemplar.DISPONIVEL)
            .getSingleResult();

        boolean estavaZerado = (qtdDisponivel == 0);

        // 2. Torna disponível
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        em.merge(exemplar);
        System.out.println(">>> [ExemplarSB] Exemplar " + exemplarId + " agora está DISPONÍVEL.");

        // 3. Se estava zerado, avisa a lista de espera
        if (estavaZerado) {
            verificarListaEspera(exemplar.getLivro());
        }
    }

    private void verificarListaEspera(Livro livro) {
        List<ListaEspera> espera = em.createQuery(
            "SELECT le FROM ListaEspera le WHERE le.livro = :livro", ListaEspera.class)
            .setParameter("livro", livro)
            .getResultList();

        if (!espera.isEmpty()) {
            System.out.println(">>> [ExemplarSB] Lista de espera detectada! Enviando mensagem...");
            
            // Monta JSON simples
            String json = "{ \"evento\": \"LIVRO_DISPONIVEL\", \"titulo\": \"" + livro.getTitulo() + "\" }";

            // Envia para fila
            mensageiro.enviarNotificacao(json);
        }
    }
}