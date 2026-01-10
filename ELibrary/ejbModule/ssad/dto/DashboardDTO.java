package ssad.dto;

import java.io.Serializable;

public class DashboardDTO implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int totalLivros;
    private int totalExemplares;
    private int totalDisponiveis;
    private int totalReservados;
    private int totalEmprestados;

    public DashboardDTO() {}

    public int getTotalLivros() { return totalLivros; }
    public void setTotalLivros(int totalLivros) { this.totalLivros = totalLivros; }

    public int getTotalExemplares() { return totalExemplares; }
    public void setTotalExemplares(int totalExemplares) { this.totalExemplares = totalExemplares; }

    public int getTotalDisponiveis() { return totalDisponiveis; }
    public void setTotalDisponiveis(int totalDisponiveis) { this.totalDisponiveis = totalDisponiveis; }

    public int getTotalReservados() { return totalReservados; }
    public void setTotalReservados(int totalReservados) { this.totalReservados = totalReservados; }

    public int getTotalEmprestados() { return totalEmprestados; }
    public void setTotalEmprestados(int totalEmprestados) { this.totalEmprestados = totalEmprestados; }
}