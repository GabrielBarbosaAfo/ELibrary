package ssad.dto;

import java.io.Serializable;
import org.eclipse.microprofile.graphql.Input;

@Input("LivroInput")
public class LivroInput implements Serializable {
    
    private String titulo;
    private String isbn;
    private String autor;
    private Integer anoPublicacao;

    // Construtor vazio é OBRIGATÓRIO
    public LivroInput() {}

    // Getters e Setters são OBRIGATÓRIOS
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    
    public Integer getAnoPublicacao() { return anoPublicacao; }
    public void setAnoPublicacao(Integer anoPublicacao) { this.anoPublicacao = anoPublicacao; }
}