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
<<<<<<< HEAD
    private MensageiroSB mensageiro; 
=======
    private MensageiroSB mensageiro; // Injeção do Produtor
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d

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

<<<<<<< HEAD
=======
    // LISTA APENAS DISPONÍVEIS
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
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

<<<<<<< HEAD
=======
        // 1. Verifica se estava tudo emprestado (count de disponiveis == 0)
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
        Long qtdDisponivel = em.createQuery(
            "SELECT COUNT(e) FROM Exemplar e WHERE e.livro = :livro AND e.status = :status", Long.class)
            .setParameter("livro", exemplar.getLivro())
            .setParameter("status", StatusExemplar.DISPONIVEL)
            .getSingleResult();

        boolean estavaZerado = (qtdDisponivel == 0);

<<<<<<< HEAD
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        em.merge(exemplar);

=======
        // 2. Torna disponível
        exemplar.setStatus(StatusExemplar.DISPONIVEL);
        em.merge(exemplar);
        System.out.println(">>> [ExemplarSB] Exemplar " + exemplarId + " agora está DISPONÍVEL.");

        // 3. Se estava zerado, avisa a lista de espera
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
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
<<<<<<< HEAD
            String json = "{ \"evento\": \"LIVRO_DISPONIVEL\", \"titulo\": \"" + livro.getTitulo() + "\" }";
            mensageiro.enviarNotificacao(json);
        }
    }

    @Override
    public int contarPorStatus(StatusExemplar status) {
        try {
            Long count = em.createQuery("SELECT COUNT(e) FROM Exemplar e WHERE e.status = :status", Long.class)
                         .setParameter("status", status)
                         .getSingleResult();
            return count.intValue();
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public void alterarStatus(Long id, String novoStatusStr) {
        Exemplar exemplar = em.find(Exemplar.class, id);
        if (exemplar == null) {
            throw new IllegalArgumentException("Exemplar não encontrado com ID: " + id);
        }

        try {
            StatusExemplar novoStatus = StatusExemplar.valueOf(novoStatusStr.toUpperCase());
            StatusExemplar statusAtual = exemplar.getStatus();

            boolean transicaoValida = false;

            // REMOVIDA A LINHA QUE PERMITIA STATUS IGUAL
            // Vamos direto para as validações de transição

            // Regra: DISPONIVEL → RESERVADO
            if (statusAtual == StatusExemplar.DISPONIVEL && novoStatus == StatusExemplar.RESERVADO) {
                transicaoValida = true;
            }
            // Regra: RESERVADO → EMPRESTADO
            else if (statusAtual == StatusExemplar.RESERVADO && novoStatus == StatusExemplar.EMPRESTADO) {
                transicaoValida = true;
            }
            // Regra: EMPRESTADO → DISPONIVEL
            else if (statusAtual == StatusExemplar.EMPRESTADO && novoStatus == StatusExemplar.DISPONIVEL) {
                transicaoValida = true;
            }

            if (!transicaoValida) {
                // Mensagem clara para vermos no teste
                throw new IllegalStateException("ERRO DE REGRA: Transição inválida de " + statusAtual + " para " + novoStatus);
            }

            exemplar.setStatus(novoStatus);
            em.merge(exemplar);
            if (catalogStatus != null) catalogStatus.atualizarCache();

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + novoStatusStr);
        }
    }
=======
            System.out.println(">>> [ExemplarSB] Lista de espera detectada! Enviando mensagem...");
            
            // Monta JSON simples
            String json = "{ \"evento\": \"LIVRO_DISPONIVEL\", \"titulo\": \"" + livro.getTitulo() + "\" }";

            // Envia para fila
            mensageiro.enviarNotificacao(json);
        }
    }
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
}