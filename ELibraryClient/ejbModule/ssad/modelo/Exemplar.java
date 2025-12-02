package ssad.modelo;

import java.io.Serializable;
import jakarta.persistence.*;

@Entity
@Table(name = "EXEMPLAR")
public class Exemplar implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigoInterno; 

    @Enumerated(EnumType.STRING)
    private StatusExemplar status;

    @ManyToOne(optional = false) 
    @JoinColumn(name = "ID_LIVRO") 
    private Livro livro;

    public Exemplar() {
        this.status = StatusExemplar.DISPONIVEL;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
    public StatusExemplar getStatus() { return status; }
    public void setStatus(StatusExemplar status) { this.status = status; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
}
