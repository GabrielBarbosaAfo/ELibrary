package ssad.graphqltutorial.dto; // <--- Ajuste para seu pacote se precisar

public class LivroDTO {
    private Long id;
    private String titulo;
    private String isbn;
    private String autor;
    private String categoria;
    private Integer anoPublicacao;
    private java.util.List<ExemplarDTO> exemplares;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }

    public java.util.List<ExemplarDTO> getExemplares() { return exemplares; }
    public void setExemplares(java.util.List<ExemplarDTO> exemplares) { this.exemplares = exemplares; }
}