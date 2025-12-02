package ssad.managebeans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped; // MUDOU PARA SESSION
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
            if (livroSB.buscarPorISBN(novoLivro.getIsbn()) != null) {
                this.mensagem = "ERRO: Já existe um livro com o ISBN " + novoLivro.getIsbn();
                return null; 
            }

            livroSB.salvar(novoLivro);
            
            this.mensagem = "Sucesso! Título cadastrado. Lembre-se de adicionar exemplares abaixo.";
            
            this.novoLivro = new Livro(); 
            return "admin?faces-redirect=true";
            
        } catch (Exception e) {
            this.mensagem = "Erro técnico: " + e.getMessage();
            return null;
        }
    }

    public String adicionarCopiaExtra() {
        try {
            if (idLivroParaAdicionarCopia != null) {
                Livro livroExistente = livroSB.buscarPorId(idLivroParaAdicionarCopia);
                adicionarExemplarAoLivro(livroExistente);
                this.mensagem = "Sucesso! Mais uma cópia adicionada para: " + livroExistente.getTitulo();
                return "admin?faces-redirect=true";
            } else {
                this.mensagem = "Erro: Selecione um livro primeiro.";
                return null;
            }
        } catch (Exception e) {
            this.mensagem = "Erro: " + e.getMessage();
            return null;
        }
    }

    private void adicionarExemplarAoLivro(Livro livro) {
        Exemplar ex = new Exemplar();
        ex.setLivro(livro);
        ex.setStatus(StatusExemplar.DISPONIVEL);
        ex.setCodigoInterno(livro.getIsbn() + "-" + System.currentTimeMillis()); 
        exemplarSB.salvar(ex);
    }

    public String cadastrarUsuario() {
        try {
            usuarioCrudSB.salvar(novoUsuario);
            this.mensagem = "Usuário " + novoUsuario.getNome() + " cadastrado com sucesso!";
            this.novoUsuario = new Usuario(); 
            return "admin?faces-redirect=true";
        } catch (Exception e) {
            this.mensagem = "Erro ao criar usuário: " + e.getMessage();
            return null;
        }
    }
    
    public void limparMensagem() {
        this.mensagem = "";
    }

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