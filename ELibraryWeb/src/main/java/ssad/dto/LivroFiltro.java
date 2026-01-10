package ssad.dto;

import java.io.Serializable;
import org.eclipse.microprofile.graphql.Input;

@Input
public class LivroFiltro implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String autor;
    private String status; 

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}