package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.ExemplarDTO;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;

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

    @POST
    @Path("/{id}/status")
    public Response alterarStatus(
            @PathParam("id") Long id, 
            @QueryParam("valor") String novoStatus) { 
        
        try {
            if (novoStatus == null || novoStatus.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("O parâmetro 'valor' é obrigatório na URL.")
                               .build();
            }

            exemplarSB.alterarStatus(id, novoStatus);
            
            return Response.ok("Status alterado com sucesso para: " + novoStatus).build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                           .entity(e.getMessage())
                           .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Status inválido: " + novoStatus)
                           .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }
}