package ssad.sessionbeans;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.interfaces.CatalogStatusSBRemote;
import ssad.modelo.StatusExemplar;
import ssad.modelo.TipoUsuario;
import ssad.modelo.Usuario;


@Singleton
@Startup
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class CatalogStatusSB implements CatalogStatusSBRemote {

    @PersistenceContext(unitName = "ELibrary")
    private EntityManager em;

    private long totalLivros;
    private long totalExemplares;
    private long exemplaresDisponiveis;

    @PostConstruct
    public void init() {
        System.out.println("[CatalogStatusSB] Inicializando sistema...");
        
        criarAdminSeNaoExistir();
        
        atualizarCache();
    }

    private void criarAdminSeNaoExistir() {
        try {
            long count = em.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class).getSingleResult();
            
            if (count == 0) {
                System.out.println("[CatalogStatusSB] Banco vazio detectado. Criando usuário ADMIN...");
                
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setMatricula("admin");
                admin.setEmail("admin@elibrary.com");
                admin.setSenhaHash("123"); 
                admin.setTipo(TipoUsuario.ADMIN);
                
                em.persist(admin);
                System.out.println("[CatalogStatusSB] Usuário ADMIN criado com sucesso! (Login: admin / Senha: 123)");
            } else {
                System.out.println("[CatalogStatusSB] Usuários já existem. Nenhuma ação necessária.");
            }
        } catch (Exception e) {
            System.out.println("[CatalogStatusSB] Erro ao tentar criar admin automático: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void atualizarCache() {
        try {
            this.totalLivros = em.createQuery("SELECT COUNT(l) FROM Livro l", Long.class).getSingleResult();
            this.totalExemplares = em.createQuery("SELECT COUNT(e) FROM Exemplar e", Long.class).getSingleResult();
            this.exemplaresDisponiveis = em.createQuery("SELECT COUNT(e) FROM Exemplar e WHERE e.status = :status", Long.class)
                .setParameter("status", StatusExemplar.DISPONIVEL)
                .getSingleResult();
        } catch (Exception e) {
            // Se as tabelas estiverem vazias ou der erro na query
            this.totalLivros = 0;
            this.totalExemplares = 0;
            this.exemplaresDisponiveis = 0;
        }
    }

    @Override
    public long getTotalLivros() { return totalLivros; }

    @Override
    public long getTotalExemplares() { return totalExemplares; }

    @Override
    public long getExemplaresDisponiveis() { return exemplaresDisponiveis; }
}