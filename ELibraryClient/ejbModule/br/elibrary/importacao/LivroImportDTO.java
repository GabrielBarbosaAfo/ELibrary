package br.elibrary.importacao;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import ssad.modelo.Livro;

<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
@XmlRootElement(name = "livro")
@XmlAccessorType(XmlAccessType.FIELD)
public class LivroImportDTO {

    private String isbn;
    private String titulo;
    private String autor;
    private String editora;
    private int anoPublicacao;
    private int quantidadeExemplares;

    public Livro toEntity() {
        Livro l = new Livro();
        l.setIsbn(this.isbn);
        l.setTitulo(this.titulo);
        l.setAutor(this.autor);
        l.setEditora(this.editora);
        l.setAnoPublicacao(this.anoPublicacao);
        
        l.setCategoria("Geral"); 
        
        return l;
    }
<<<<<<< HEAD
=======
=======
@XmlRootElement(name = "livro") 
@XmlAccessorType(XmlAccessType.FIELD)
public class LivroImportDTO {
    
    private String isbn;
    private String titulo;
    private String autor;
    private Integer anoPublicacao;
    private int quantidadeExemplares; 
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }
    public int getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(int anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public int getQuantidadeExemplares() { return quantidadeExemplares; }
    public void setQuantidadeExemplares(int quantidadeExemplares) { this.quantidadeExemplares = quantidadeExemplares; }
<<<<<<< HEAD
=======
=======
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
    public int getQuantidadeExemplares() { return quantidadeExemplares; }
    public void setQuantidadeExemplares(int quantidadeExemplares) { this.quantidadeExemplares = quantidadeExemplares; }
    
    public Livro toEntity() {
        Livro l = new Livro();
        l.setIsbn(this.isbn);
        l.setTitulo(this.titulo);
        l.setAutor(this.autor);
        l.setAnoPublicacao(this.anoPublicacao);
        return l;
    }
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
}