package ssad.sessionbeans;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.ListaEsperasSBRemote;
import ssad.modelo.ListaEspera;
import ssad.modelo.Livro;
import ssad.modelo.Usuario;

@Stateless
public class ListaEsperasSB implements ListaEsperasSBRemote {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void inscrever(Long usuarioId, Long livroId) {
        Usuario u = em.find(Usuario.class, usuarioId);
        Livro l = em.find(Livro.class, livroId);

        if (u != null && l != null) {
            // Verifica se já não está inscrito
            long count = em.createQuery("SELECT count(le) FROM ListaEspera le WHERE le.usuario = :u AND le.livro = :l", Long.class)
                    .setParameter("u", u)
                    .setParameter("l", l)
                    .getSingleResult();

            if (count == 0) {
                ListaEspera le = new ListaEspera(u, l);
                em.persist(le);
                System.out.println(">>> [ListaEsperasSB] Usuário " + u.getNome() + " entrou na fila para: " + l.getTitulo());
            } else {
                System.out.println(">>> [ListaEsperasSB] Usuário já está na fila.");
            }
        }
    }
}