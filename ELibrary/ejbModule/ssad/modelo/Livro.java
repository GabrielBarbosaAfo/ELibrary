package ssad.modelo;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "LIVRO")
public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    private String autor;
    private String editora;
    private Integer anoPublicacao;
    
    private String categoria; 

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private transient List<Exemplar> exemplares;

    public Livro() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public List<Exemplar> getExemplares() { return exemplares; }
    public void setExemplares(List<Exemplar> exemplares) { this.exemplares = exemplares; }
}