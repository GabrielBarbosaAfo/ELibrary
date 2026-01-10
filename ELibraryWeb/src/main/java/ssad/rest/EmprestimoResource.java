package ssad.rest;

import jakarta.ejb.EJB;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import ssad.dto.EmprestimoDTO;
import ssad.interfaces.EmprestimoSBRemote;
import ssad.modelo.Emprestimo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/emprestimos") 
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmprestimoResource {

    @EJB
    private EmprestimoSBRemote emprestimoSB;

    @GET
    public Response listarTodos() {
        try {
            List<Emprestimo> lista = emprestimoSB.listarTodos();
            
            List<EmprestimoDTO> dtos = lista.stream()
                    .map(EmprestimoDTO::new) 
                    .collect(Collectors.toList());

            return Response.ok(dtos).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity("Erro ao listar empréstimos").build();
        }
    }
}