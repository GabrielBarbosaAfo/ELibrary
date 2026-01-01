package ssad.sessionbeans;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.UsuarioCrudSBRemote;
import ssad.modelo.Usuario;
import java.util.List;

@Stateless
public class UsuarioCrudSB implements UsuarioCrudSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;

    @Override
    public void salvar(Usuario usuario) throws Exception {
        long qtd = em.createQuery("SELECT COUNT(u) FROM Usuario u WHERE u.matricula = :mat", Long.class)
                     .setParameter("mat", usuario.getMatricula())
                     .getSingleResult();
        
        if (qtd > 0 && usuario.getId() == null) {
            throw new Exception("Matrícula já cadastrada!");
        }

        if (usuario.getId() == null) {
            em.persist(usuario);
        } else {
            em.merge(usuario);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT u FROM Usuario u", Usuario.class).getResultList();
    }
}