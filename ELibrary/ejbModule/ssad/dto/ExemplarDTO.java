package ssad.dto;

import ssad.modelo.Exemplar;
import ssad.modelo.StatusExemplar;
import java.io.Serializable;

public class ExemplarDTO implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Long id;
    private String codigoInterno;
    
    // Usamos String para facilitar a serialização JSON/GraphQL
    private String status; 
    
    private Long idLivro;
    private String tituloLivro;

    public ExemplarDTO() {}

    public ExemplarDTO(Exemplar e) {
        this.id = e.getId();
        this.codigoInterno = e.getCodigoInterno();
        
        // Converte o Enum do banco para String
        if (e.getStatus() != null) {
            this.status = e.getStatus().name(); 
        }
        
        if (e.getLivro() != null) {
            this.idLivro = e.getLivro().getId();
            this.tituloLivro = e.getLivro().getTitulo();
        }
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getIdLivro() { return idLivro; }
    public void setIdLivro(Long idLivro) { this.idLivro = idLivro; }
    
    public String getTituloLivro() { return tituloLivro; }
    public void setTituloLivro(String tituloLivro) { this.tituloLivro = tituloLivro; }
}