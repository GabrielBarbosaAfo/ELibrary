package ssad.managebeans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import ssad.interfaces.ExemplarSBRemote;
import ssad.interfaces.LivroSBRemote;
import ssad.interfaces.UsuarioCrudSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.Livro;
import ssad.modelo.StatusExemplar;
import ssad.modelo.Usuario;
import ssad.modelo.TipoUsuario;
import java.io.Serializable;
import java.util.List;

@Named(value = "adminMB")
@SessionScoped
public class AdminMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB private LivroSBRemote livroSB;
    @EJB private ExemplarSBRemote exemplarSB;
    @EJB private UsuarioCrudSBRemote usuarioCrudSB;
    
    private Livro novoLivro = new Livro();
    private Usuario novoUsuario = new Usuario();
    private Long idLivroParaAdicionarCopia;
    private String mensagem = "";

    public String cadastrarLivro() {
        try {
            if (novoLivro.getId() == null && livroSB.buscarPorISBN(novoLivro.getIsbn()) != null) {
                this.mensagem = "ERRO: Já existe um livro com o ISBN " + novoLivro.getIsbn();
                return null;
            }

            livroSB.salvar(novoLivro);
            
            String acao = (novoLivro.getId() == null) ? "cadastrado" : "alterado";
            this.mensagem = "Sucesso! Livro " + acao + ".";
            
            this.novoLivro = new Livro(); 
            return "admin?faces-redirect=true";
            
        } catch (Exception e) {
            this.mensagem = "Erro técnico: " + e.getMessage();
            return null;
        }
    }

    // PREPARAR EDIÇÃO
    public void prepararEdicao(Livro livro) {
        this.novoLivro = livro; 
        this.mensagem = "Editando: " + livro.getTitulo();
    }

    public void cancelarEdicao() {
        this.novoLivro = new Livro();
        this.mensagem = "Edição cancelada.";
    }

    public void excluirLivro(Livro livro) {
        try {
            livroSB.remover(livro.getId());
            this.mensagem = "Livro excluído com sucesso!";
        } catch (Exception e) {
            this.mensagem = "Erro ao excluir: " + e.getMessage();
        }
    }
    
    public String adicionarCopiaExtra() {
        try {
            if (idLivroParaAdicionarCopia != null) {
                Livro l = livroSB.buscarPorId(idLivroParaAdicionarCopia);
                Exemplar ex = new Exemplar();
                ex.setLivro(l);
                ex.setStatus(StatusExemplar.DISPONIVEL);
                ex.setCodigoInterno(l.getIsbn() + "-" + System.currentTimeMillis()); 
                exemplarSB.salvar(ex);
                
                this.mensagem = "Cópia adicionada para: " + l.getTitulo();
                return "admin?faces-redirect=true";
            }
            return null;
        } catch (Exception e) {
            this.mensagem = "Erro: " + e.getMessage();
            return null;
        }
    }

    public String cadastrarUsuario() {
        try {
            usuarioCrudSB.salvar(novoUsuario);
            this.mensagem = "Usuário " + novoUsuario.getNome() + " cadastrado!";
            this.novoUsuario = new Usuario(); 
            return "admin?faces-redirect=true";
        } catch (Exception e) {
            this.mensagem = "Erro ao criar usuário: " + e.getMessage();
            return null;
        }
    }
    
    public void limparMensagem() { this.mensagem = ""; }

    public List<Livro> getTodosLivros() { return livroSB.listarTodos(); }
    public TipoUsuario[] getTiposUsuario() { return TipoUsuario.values(); }
    public Livro getNovoLivro() { return novoLivro; }
    public void setNovoLivro(Livro novoLivro) { this.novoLivro = novoLivro; }
    public Usuario getNovoUsuario() { return novoUsuario; }
    public void setNovoUsuario(Usuario novoUsuario) { this.novoUsuario = novoUsuario; }
    public Long getIdLivroParaAdicionarCopia() { return idLivroParaAdicionarCopia; }
    public void setIdLivroParaAdicionarCopia(Long id) { this.idLivroParaAdicionarCopia = id; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String msg) { this.mensagem = msg; }
}