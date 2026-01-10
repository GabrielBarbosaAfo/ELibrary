package br.elibrary.importacao;

import java.util.List;
<<<<<<< HEAD
=======
<<<<<<< HEAD
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "biblioteca")
@XmlAccessorType(XmlAccessType.FIELD)
public class BibliotecaImportWrapper {

    @XmlElement(name = "livro")
<<<<<<< HEAD
    @JsonProperty("livros") 
    private List<LivroImportDTO> livros;

=======
    @JsonProperty("livros") // Jackson lê "livros": [] no JSON
    private List<LivroImportDTO> livros;

=======
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "biblioteca") 
public class BibliotecaImportWrapper {
    
    private List<LivroImportDTO> livros;

    @XmlElement(name = "livro") 
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
    public List<LivroImportDTO> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroImportDTO> livros) {
        this.livros = livros;
    }
}