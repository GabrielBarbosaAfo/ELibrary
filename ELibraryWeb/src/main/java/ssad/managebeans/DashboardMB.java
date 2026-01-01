package ssad.managebeans;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Named;
import ssad.interfaces.CatalogStatusSBRemote;

@Named(value = "dashboardMB")
@RequestScoped
public class DashboardMB {

    @EJB
    private CatalogStatusSBRemote catalogStatus;

    public long getTotalLivros() {
        return catalogStatus.getTotalLivros();
    }

    public long getTotalExemplares() {
        return catalogStatus.getTotalExemplares();
    }

    public long getExemplaresDisponiveis() {
        return catalogStatus.getExemplaresDisponiveis();
    }
}
