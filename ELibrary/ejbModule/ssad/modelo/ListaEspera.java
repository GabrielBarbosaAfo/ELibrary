package ssad.modelo;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "LISTA_ESPERA")
public class ListaEspera implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_LIVRO")
    private Livro livro;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInscricao;

    public ListaEspera() {
        this.dataInscricao = new Date();
    }
    
    public ListaEspera(Usuario u, Livro l) {
        this();
        this.usuario = u;
        this.livro = l;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Livro getLivro() { return livro; }
    public void setLivro(Livro livro) { this.livro = livro; }
    public Date getDataInscricao() { return dataInscricao; }
    public void setDataInscricao(Date dataInscricao) { this.dataInscricao = dataInscricao; }
}