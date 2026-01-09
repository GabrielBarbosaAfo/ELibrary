package ssad.managebeans;

import ssad.interfaces.LivroSBRemote;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Livro;
import ssad.modelo.Exemplar; 
import ssad.modelo.StatusExemplar; 
import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import java.util.UUID; 
@Named(value = "livroMB")
@RequestScoped
public class LivroMB {

    @EJB
    private LivroSBRemote livroSB;
    
    @EJB
    private ExemplarSBRemote exemplarSB; 

    private Livro livro = new Livro();
    private String mensagem = ""; 

    public String salvar() {
        try {
            livroSB.salvar(livro);
            
            Exemplar exemplarAuto = new Exemplar();
            exemplarAuto.setLivro(livro);
            exemplarAuto.setStatus(StatusExemplar.DISPONIVEL);
            exemplarAuto.setCodigoInterno(livro.getIsbn() + "-" + (int)(Math.random() * 1000));
            
            exemplarSB.salvar(exemplarAuto);

            this.mensagem = "Sucesso! Livro cadastrado e 1 exemplar adicionado ao estoque.";
            this.livro = new Livro(); 
        } catch (Exception e) {
            e.printStackTrace(); 
            this.mensagem = "Erro ao salvar: " + e.getMessage();
        }
        return null; 
    }

    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
}