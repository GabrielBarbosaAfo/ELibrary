package ssad.dto;

import ssad.modelo.Livro;
import java.io.Serializable;

public class LivroDTO implements Serializable {
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
<<<<<<< HEAD
=======
=======
    private Long id;
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    private String titulo;
    private String isbn;
    private String autor;
    private Integer anoPublicacao;

    public LivroDTO() {}

    public LivroDTO(Livro l) {
        this.id = l.getId();
        this.titulo = l.getTitulo();
        this.isbn = l.getIsbn();
        this.autor = l.getAutor();
        this.anoPublicacao = l.getAnoPublicacao();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    
    public Livro toEntity() {
        Livro l = new Livro();
        l.setId(this.id);
        l.setTitulo(this.titulo);
        l.setIsbn(this.isbn);
        l.setAutor(this.autor);
        l.setAnoPublicacao(this.anoPublicacao);
        return l;
    }
}