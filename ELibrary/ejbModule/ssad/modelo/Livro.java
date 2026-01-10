package ssad.modelo;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "LIVRO")
public class Livro implements Serializable {

<<<<<<< HEAD
    private static final long serialVersionUID = 1L;

    @Id
=======
<<<<<<< HEAD
    private static final long serialVersionUID = 1L;

    @Id
=======
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String titulo;

    private String autor;
    private String editora;
    private Integer anoPublicacao;
<<<<<<< HEAD
    
    private String categoria; 
=======
<<<<<<< HEAD
    
    private String categoria; 
=======
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29

    @OneToMany(mappedBy = "livro", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private transient List<Exemplar> exemplares;

    public Livro() {}
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    
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

<<<<<<< HEAD
=======
=======
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
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    public List<Exemplar> getExemplares() { return exemplares; }
    public void setExemplares(List<Exemplar> exemplares) { this.exemplares = exemplares; }
}