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

    @PATCH
    @Path("/{id}/status")
    // Removemos @FormParam e usamos um Map para ler o JSON genérico {"status": "..."}
    public Response alterarStatus(@PathParam("id") Long id, java.util.Map<String, String> body) {
        try {
            // Extrai o valor da chave "status" do JSON recebido
            String novoStatus = body.get("status");
            
            if (novoStatus == null) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("O campo 'status' é obrigatório no JSON.")
                               .build();
            }

            exemplarSB.alterarStatus(id, novoStatus);
            return Response.ok("Status alterado com sucesso.").build();

        } catch (IllegalStateException e) {
            // 409 Conflict - Transição Inválida
            return Response.status(Response.Status.CONFLICT)
                           .entity(e.getMessage())
                           .build();

        } catch (IllegalArgumentException e) {
            // 400 Bad Request - Status inválido
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
                           .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
        }
    }
}