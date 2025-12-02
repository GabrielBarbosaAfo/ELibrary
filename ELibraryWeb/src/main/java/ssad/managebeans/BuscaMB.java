package ssad.managebeans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;
import java.util.List;

@Named(value = "buscaMB")
@RequestScoped
public class BuscaMB {

    @EJB 
    private ExemplarSBRemote exemplarSB;

    private String termoPesquisa;
    
    private List<Exemplar> listaExemplares;

    public List<Exemplar> getExemplaresDisponiveis() {
        if (listaExemplares == null) {
            if (termoPesquisa != null && !termoPesquisa.trim().isEmpty()) {
                listaExemplares = exemplarSB.pesquisarPorTitulo(termoPesquisa);
            } else {
                listaExemplares = exemplarSB.listarTodos();
            }
        }
        return listaExemplares;
    }
    
    public void pesquisar() {
        this.listaExemplares = null; 
    }

    public String getTermoPesquisa() { return termoPesquisa; }
    public void setTermoPesquisa(String termoPesquisa) { this.termoPesquisa = termoPesquisa; }
}