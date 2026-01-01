package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.ExemplarDTO;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;
import ssad.modelo.StatusExemplar;

@Path("/exemplares")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExemplarResource {

    @EJB
    private ExemplarSBRemote exemplarSB;

    // 1. GET /exemplares/{id} - Buscar por ID
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        Exemplar ex = exemplarSB.buscarPorId(id);
        if (ex == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(new ExemplarDTO(ex)).build();
    }

    // 2. PUT /exemplares/{id}/status - Alterar Status com validação rígida
    @PUT // O enunciado fala em PATCH na descrição mas PUT na tabela. Vamos de PUT.
    @Path("/{id}/status")
    public Response alterarStatus(@PathParam("id") Long id, 
                                  @QueryParam("novoStatus") String novoStatusStr) {
        
        Exemplar ex = exemplarSB.buscarPorId(id);
        if (ex == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Exemplar não encontrado").build();
        }

        if (novoStatusStr == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Informe o query param 'novoStatus'").build();
        }

        try {
            StatusExemplar novoStatus = StatusExemplar.valueOf(novoStatusStr.toUpperCase());
            StatusExemplar statusAtual = ex.getStatus();

            // --- MÁQUINA DE ESTADOS (Regra de Negócio da API) ---
            boolean transicaoValida = false;

            // DISPONIVEL -> RESERVADO
            if (statusAtual == StatusExemplar.DISPONIVEL && novoStatus == StatusExemplar.RESERVADO) {
                transicaoValida = true;
            }
            // RESERVADO -> EMPRESTADO
            else if (statusAtual == StatusExemplar.RESERVADO && novoStatus == StatusExemplar.EMPRESTADO) { // Assumindo que EMPRESTADO existe no enum
                transicaoValida = true;
            }
            // EMPRESTADO -> DISPONIVEL
            else if ((statusAtual == StatusExemplar.EMPRESTADO || statusAtual.name().equals("EMPRESTADO")) 
                     && novoStatus == StatusExemplar.DISPONIVEL) {
                transicaoValida = true;
            }
            // OBS: Se você não tiver o status RESERVADO ou EMPRESTADO no seu Enum ainda,
            // precisará adicionar no 'ssad.modelo.StatusExemplar'.
            // Caso sua regra atual seja só DISPONIVEL/EMPRESTADO, adapte aqui.
            
            if (!transicaoValida) {
                return Response.status(Response.Status.CONFLICT) // HTTP 409
                        .entity("Transição de status inválida: De " + statusAtual + " para " + novoStatus).build();
            }

            // Se passou, atualiza
            ex.setStatus(novoStatus);
            exemplarSB.salvar(ex);

            return Response.ok(new ExemplarDTO(ex)).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Status inválido.").build();
        }
    }
}