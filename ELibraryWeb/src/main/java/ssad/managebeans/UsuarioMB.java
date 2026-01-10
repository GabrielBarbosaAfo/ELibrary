package ssad.managebeans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import ssad.interfaces.UsuarioSBRemote;
import ssad.modelo.Emprestimo;
import ssad.modelo.Usuario;
import ssad.modelo.TipoUsuario;
import java.io.Serializable;
import java.util.List;

@Named(value = "usuarioMB") 
@SessionScoped 
public class UsuarioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioSBRemote usuarioSB;
    
    @Inject
    private HttpServletRequest request; 

    private String matricula;
    private String senha;
    private Usuario usuarioLogado;
    private String aviso = null;

    // --- LOGIN ---
    public String realizarLogin() {
        boolean sucesso = usuarioSB.login(matricula, senha);

        if (sucesso) {
            this.usuarioLogado = usuarioSB.getUsuarioLogado();
            this.aviso = null;
            
            // Grava na sessão para o Filtro
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogado", this.usuarioLogado);
            
            // Redirecionamento baseado no tipo
            if (this.usuarioLogado.getTipo() == TipoUsuario.ADMIN) {
                return "admin?faces-redirect=true";
            } else {
                return "index?faces-redirect=true";
            }
        } else {
            this.aviso = "Login inválido! Verifique matrícula e senha.";
            return null; 
        }
    }
    
    // --- EMPRÉSTIMO (O que faz o botão funcionar) ---
    public String realizarEmprestimo(Long idExemplar) {
        try {
            boolean sucesso = usuarioSB.realizarEmprestimo(idExemplar);
            
            if (sucesso) {
                this.aviso = "Sucesso! Livro emprestado para você.";
                return "index?faces-redirect=true"; // Volta para o Dashboard
            } else {
                this.aviso = "Erro: Não foi possível emprestar. O livro pode não estar disponível.";
                return null; // Fica na mesma página e mostra o erro
            }
        } catch (Exception e) {
            this.aviso = "Erro técnico: " + e.getMessage();
            return null;
        }
    }
    
    // --- DEVOLUÇÃO ---
    public String realizarDevolucao(Long idEmprestimo) {
        usuarioSB.realizarDevolucao(idEmprestimo);
        this.aviso = "Devolução registrada com sucesso.";
        return "index?faces-redirect=true";
    }
    
    // --- LOGOUT ---
    public String realizarLogout() {
        usuarioSB.logout();
        this.usuarioLogado = null;
        this.aviso = null;
        request.getSession().invalidate(); // Mata a sessão
        return "login?faces-redirect=true";
    }
    
    public void limparAviso() { this.aviso = null; }
    
    // Getters e Setters
    public boolean isLogado() { return usuarioLogado != null; }
    public List<Emprestimo> getMeusEmprestimos() { return usuarioSB.listarMeusEmprestimosAtivos(); }
    
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Usuario getUsuarioLogado() { return usuarioLogado; }
    public void setUsuarioLogado(Usuario usuarioLogado) { this.usuarioLogado = usuarioLogado; }
    public String getAviso() { return aviso; }
    public void setAviso(String aviso) { this.aviso = aviso; }
}