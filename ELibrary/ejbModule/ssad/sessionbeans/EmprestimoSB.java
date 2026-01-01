package ssad.sessionbeans;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Date;
import java.util.List;
import ssad.interfaces.EmprestimoSBRemote;
import ssad.modelo.Emprestimo;

@Stateless
public class EmprestimoSB implements EmprestimoSBRemote {

    @PersistenceContext(unitName = "ELibrary") // Confirme se o nome é esse no seu persistence.xml
    private EntityManager em;
    
    @EJB
    private MensageiroSB mensageiro; // Injeta o carteiro

    @Override
    public List<Emprestimo> listarPorUsuario(Long idUsuario) {
        // Busca empréstimos onde o atributo 'usuario' tem o ID passado
        return em.createQuery("SELECT e FROM Emprestimo e WHERE e.usuario.id = :idUsuario", Emprestimo.class)
                 .setParameter("idUsuario", idUsuario)
                 .getResultList();
    }

    @Override
    public void salvar(Emprestimo emprestimo) {
        if (emprestimo.getId() == null) {
            em.persist(emprestimo);
        } else {
            em.merge(emprestimo);
        }
    }
    
    @Override
    public void verificarAtrasos() {
        System.out.println(">>> [EmprestimoSB] Verificando atrasos...");

        // 1. Busca empréstimos não devolvidos e com data prevista menor que hoje
        String jpql = "SELECT e FROM Emprestimo e WHERE e.dataDevolucao IS NULL AND e.dataPrevistaDevolucao < :hoje";
        
        List<Emprestimo> atrasados = em.createQuery(jpql, Emprestimo.class)
                .setParameter("hoje", new Date())
                .getResultList();

        if (atrasados.isEmpty()) {
            System.out.println(">>> [EmprestimoSB] Nenhum atraso encontrado.");
        }

        // 2. Para cada atrasado, manda mensagem para a fila
        for (Emprestimo e : atrasados) {
            long diff = new Date().getTime() - e.getDataPrevistaDevolucao().getTime();
            long diasAtraso = diff / (1000 * 60 * 60 * 24);

            String json = String.format(
                "{ \"evento\": \"EMPRESTIMO_EM_ATRASO\", \"usuario\": \"%s\", \"livro\": \"%s\", \"diasAtraso\": %d }",
                e.getUsuario().getNome(),
                e.getExemplar().getLivro().getTitulo(),
                diasAtraso
            );

            mensageiro.enviarNotificacao(json);
        }
    }
}