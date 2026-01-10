package ssad.graphqltutorial.dto;

public class ExemplarDTO {
    private Long id;
    private String codigoInterno;
    private String status;
    
    private LivroDTO livro; 

    public LivroDTO getLivro() { return livro; }
    public void setLivro(LivroDTO livro) { this.livro = livro; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}