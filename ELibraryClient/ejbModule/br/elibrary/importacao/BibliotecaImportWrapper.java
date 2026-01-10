package br.elibrary.importacao;

import java.util.List;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import com.fasterxml.jackson.annotation.JsonProperty;

@XmlRootElement(name = "biblioteca")
@XmlAccessorType(XmlAccessType.FIELD)
public class BibliotecaImportWrapper {

    @XmlElement(name = "livro")
    @JsonProperty("livros") 
    private List<LivroImportDTO> livros;

    public List<LivroImportDTO> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroImportDTO> livros) {
        this.livros = livros;
    }
}