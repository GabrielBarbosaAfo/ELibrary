package br.elibrary.importacao;

import java.util.List;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "biblioteca") 
public class BibliotecaImportWrapper {
    
    private List<LivroImportDTO> livros;

    @XmlElement(name = "livro") 
    public List<LivroImportDTO> getLivros() {
        return livros;
    }

    public void setLivros(List<LivroImportDTO> livros) {
        this.livros = livros;
    }
}