package ssad.graphqltutorial.dto;

public class EmprestimoDTO {
    private Long id;
    private String dataEmprestimo;
    private String dataPrevDevolucao;
    private String dataDevolucao;
    private ExemplarDTO exemplar; 
    private Long usuarioId; 

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
}