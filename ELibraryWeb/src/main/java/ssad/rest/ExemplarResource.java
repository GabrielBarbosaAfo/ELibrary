package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.ExemplarDTO;
import ssad.interfaces.ExemplarSBRemote;
import ssad.modelo.Exemplar;
<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
import ssad.modelo.StatusExemplar;
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29

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

<<<<<<< HEAD
    @POST
    @Path("/{id}/status")
    public Response alterarStatus(
            @PathParam("id") Long id, 
            @QueryParam("valor") String novoStatus) { 
        
        try {
            if (novoStatus == null || novoStatus.isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                               .entity("O parâmetro 'valor' é obrigatório na URL.")
=======
<<<<<<< HEAD
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
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
                               .build();
            }

            exemplarSB.alterarStatus(id, novoStatus);
<<<<<<< HEAD
            
            return Response.ok("Status alterado com sucesso para: " + novoStatus).build();

        } catch (IllegalStateException e) {
=======
            return Response.ok("Status alterado com sucesso.").build();

        } catch (IllegalStateException e) {
            // 409 Conflict - Transição Inválida
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
            return Response.status(Response.Status.CONFLICT)
                           .entity(e.getMessage())
                           .build();

        } catch (IllegalArgumentException e) {
<<<<<<< HEAD
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity("Status inválido: " + novoStatus)
=======
            // 400 Bad Request - Status inválido
            return Response.status(Response.Status.BAD_REQUEST)
                           .entity(e.getMessage())
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
                           .build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro interno: " + e.getMessage()).build();
<<<<<<< HEAD
=======
=======
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
>>>>>>> 651da6e6746f03f28e9d376825981918c579544d
>>>>>>> 82f00c2176cc4cf14505665bf1521e78cb2c3c29
        }
    }
}