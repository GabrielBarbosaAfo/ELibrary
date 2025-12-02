package ssad.sessionbeans;

import ssad.interfaces.CatalogStatusSBRemote;
import ssad.interfaces.UsuarioSBRemote;
import ssad.modelo.*;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateful;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;
import java.util.Date;
import java.util.List;
import java.util.Calendar;

@Stateful
public class UsuarioSB implements UsuarioSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;

    @EJB
    private CatalogStatusSBRemote catalogStatus; 

    private Usuario usuarioLogado;

    @Override
    public boolean login(String matricula, String senha) {
        try {
            Usuario u = em.createQuery("SELECT u FROM Usuario u WHERE u.matricula = :matricula AND u.senhaHash = :senha", Usuario.class)
                    .setParameter("matricula", matricula)
                    .setParameter("senha", senha)
                    .getSingleResult();
            this.usuarioLogado = u;
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public void logout() {
        this.usuarioLogado = null;
    }

    @Override
    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    @Override
    public boolean realizarEmprestimo(Long idExemplar) {
        if (usuarioLogado == null) return false;

        Exemplar exemplar = em.find(Exemplar.class, idExemplar);
        
        if (exemplar != null && exemplar.getStatus() == StatusExemplar.DISPONIVEL) {
            
            exemplar.setStatus(StatusExemplar.EMPRESTADO);
            em.merge(exemplar);

            Emprestimo emp = new Emprestimo();
            emp.setUsuario(usuarioLogado);
            emp.setExemplar(exemplar);
            emp.setDataEmprestimo(new Date());
            
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.DATE, 7);
            emp.setDataPrevistaDevolucao(c.getTime());
            
            emp.setStatus(StatusEmprestimo.ATIVO);

            em.persist(emp);
            
            catalogStatus.atualizarCache();
            
            return true;
        }
        return false;
    }

    @Override
    public boolean realizarDevolucao(Long idEmprestimo) {
        Emprestimo emp = em.find(Emprestimo.class, idEmprestimo);
        
        if (emp != null && emp.getStatus() == StatusEmprestimo.ATIVO) {
            
            emp.setStatus(StatusEmprestimo.FINALIZADO);
            emp.setDataDevolucao(new Date());
            em.merge(emp);

            Exemplar exemplar = emp.getExemplar();
            exemplar.setStatus(StatusExemplar.DISPONIVEL);
            em.merge(exemplar);

            catalogStatus.atualizarCache();
            
            return true;
        }
        return false;
    }

    @Override
    public List<Emprestimo> listarMeusEmprestimosAtivos() {
        if (usuarioLogado == null) return null;
        
        return em.createQuery(
            "SELECT e FROM Emprestimo e JOIN FETCH e.exemplar JOIN FETCH e.exemplar.livro " +
            "WHERE e.usuario.id = :uid AND e.status = :status", Emprestimo.class)
            .setParameter("uid", usuarioLogado.getId())
            .setParameter("status", StatusEmprestimo.ATIVO)
            .getResultList();
    }
    
    
}