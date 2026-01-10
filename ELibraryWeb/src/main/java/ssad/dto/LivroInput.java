package ssad.dto;

import java.io.Serializable;
import org.eclipse.microprofile.graphql.Input;

@Input("LivroInput")
public class LivroInput implements Serializable {
    
    private String titulo;
    private String isbn;
    private String autor;
    private Integer anoPublicacao;

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    private String categoria;
    
    public LivroInput() {}

<<<<<<< HEAD
=======
=======
    // Construtor vazio é OBRIGATÓRIO
    public LivroInput() {}

    // Getters e Setters são OBRIGATÓRIOS
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
<<<<<<< HEAD
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
=======
<<<<<<< HEAD
    
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
=======
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
}