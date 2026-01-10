package ssad.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import ssad.modelo.Emprestimo;

public class EmprestimoDTO implements Serializable {
<<<<<<< HEAD
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String dataEmprestimo;
    private String dataPrevDevolucao;
    private String dataDevolucao;
    private ExemplarDTO exemplar;
    
    private Long usuarioId; 
=======
    private Long id;
    private String dataEmprestimo;
    private String dataPrevDevolucao;
    private String dataDevolucao;
    private ExemplarDTO exemplar; // Reutiliza o DTO de Exemplar
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29

    public EmprestimoDTO() {}

    public EmprestimoDTO(Emprestimo e) {
        this.id = e.getId();
<<<<<<< HEAD
        
=======
        // Formata datas para String (padrão GraphQL costuma ser ISO-8601 ou String simples)
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if (e.getDataEmprestimo() != null) 
            this.dataEmprestimo = sdf.format(e.getDataEmprestimo());
            
        if (e.getDataPrevistaDevolucao() != null) 
<<<<<<< HEAD
            this.dataPrevDevolucao = sdf.format(e.getDataPrevistaDevolucao()); // Ajustei o nome do getter se for diferente no seu modelo
=======
            this.dataPrevDevolucao = sdf.format(e.getDataPrevistaDevolucao());
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            
        if (e.getDataDevolucao() != null) 
            this.dataDevolucao = sdf.format(e.getDataDevolucao());
            
        if (e.getExemplar() != null)
            this.exemplar = new ExemplarDTO(e.getExemplar());
<<<<<<< HEAD

        if (e.getUsuario() != null) {
            this.usuarioId = e.getUsuario().getId();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(String dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    
    public String getDataPrevDevolucao() { return dataPrevDevolucao; }
    public void setDataPrevDevolucao(String dataPrevDevolucao) { this.dataPrevDevolucao = dataPrevDevolucao; }
    
    public String getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(String dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    
    public ExemplarDTO getExemplar() { return exemplar; }
    public void setExemplar(ExemplarDTO exemplar) { this.exemplar = exemplar; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }
=======
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDataEmprestimo() { return dataEmprestimo; }
    public void setDataEmprestimo(String dataEmprestimo) { this.dataEmprestimo = dataEmprestimo; }
    public String getDataPrevDevolucao() { return dataPrevDevolucao; }
    public void setDataPrevDevolucao(String dataPrevDevolucao) { this.dataPrevDevolucao = dataPrevDevolucao; }
    public String getDataDevolucao() { return dataDevolucao; }
    public void setDataDevolucao(String dataDevolucao) { this.dataDevolucao = dataDevolucao; }
    public ExemplarDTO getExemplar() { return exemplar; }
    public void setExemplar(ExemplarDTO exemplar) { this.exemplar = exemplar; }
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
}