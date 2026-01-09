package ssad.modelo;

import java.io.Serializable;
import jakarta.persistence.*;
import java.util.Date; // Usando Date clássico para garantir compatibilidade

@Entity
@Table(name = "EMPRESTIMO")
public class Emprestimo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dataEmprestimo;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dataPrevistaDevolucao;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataDevolucao; 

    @Enumerated(EnumType.STRING)
    private StatusEmprestimo status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_USUARIO")
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EXEMPLAR")
    private Exemplar exemplar;

    public Emprestimo() {
        this.dataEmprestimo = new Date(); 
        this.status = StatusEmprestimo.ATIVO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Date getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(Date dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public Date getDataPrevistaDevolucao() { return dataPrevistaDevolucao; }
    public void setDataPrevistaDevolucao(Date dataPrevistaDevolucao) { this.dataPrevistaDevolucao = dataPrevistaDevolucao; }
    public Date getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(Date dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    public StatusEmprestimo getStatus() { return status; }
    public void setStatus(StatusEmprestimo status) { this.status = status; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Exemplar getExemplar() { return exemplar; }
    public void setExemplar(Exemplar exemplar) { this.exemplar = exemplar; }
}
