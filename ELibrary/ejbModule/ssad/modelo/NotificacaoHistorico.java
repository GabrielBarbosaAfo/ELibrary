package ssad.modelo;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.*;

@Entity
@Table(name = "NOTIFICACAO_HISTORICO")
public class NotificacaoHistorico implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long usuarioIdDestino; 
    
    @Column(length = 1000)
    private String mensagem; 
    
    private String tipoEvento; 

    @Temporal(TemporalType.TIMESTAMP)
    private Date dataEnvio;

    public NotificacaoHistorico() {
        this.dataEnvio = new Date();
    }
    
    public NotificacaoHistorico(Long usuarioId, String tipo, String msg) {
        this();
        this.usuarioIdDestino = usuarioId;
        this.tipoEvento = tipo;
        this.mensagem = msg;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUsuarioIdDestino() { return usuarioIdDestino; }
    public void setUsuarioIdDestino(Long usuarioIdDestino) { this.usuarioIdDestino = usuarioIdDestino; }
    public String getMensagem() { return mensagem; }
    public void setMensagem(String mensagem) { this.mensagem = mensagem; }
    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }
    public Date getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(Date dataEnvio) { this.dataEnvio = dataEnvio; }
}