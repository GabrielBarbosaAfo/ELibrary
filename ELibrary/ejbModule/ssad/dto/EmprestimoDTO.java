package ssad.dto;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import ssad.modelo.Emprestimo;

public class EmprestimoDTO implements Serializable {
    private Long id;
    private String dataEmprestimo;
    private String dataPrevDevolucao;
    private String dataDevolucao;
    private ExemplarDTO exemplar; 

    public EmprestimoDTO() {}

    public EmprestimoDTO(Emprestimo e) {
        this.id = e.getId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        
        if (e.getDataEmprestimo() != null) 
            this.dataEmprestimo = sdf.format(e.getDataEmprestimo());
            
        if (e.getDataPrevistaDevolucao() != null) 
            this.dataPrevDevolucao = sdf.format(e.getDataPrevistaDevolucao());
            
        if (e.getDataDevolucao() != null) 
            this.dataDevolucao = sdf.format(e.getDataDevolucao());
            
        if (e.getExemplar() != null)
            this.exemplar = new ExemplarDTO(e.getExemplar());
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
}