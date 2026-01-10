package br.elibrary.rest.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ssad.dto.ExemplarDTO;

public class TesteRestExemplar {

    private static final String BASE_URI = "http://localhost:8080/ELibraryWeb/api";
    // IMPORTANTE: Certifique-se de que o Livro ID 1 existe no banco
    private static final Long ID_LIVRO_TESTE = 1L; 

    public static void main(String[] args) {
        System.out.println("=== 📖 INICIANDO BATERIA DE TESTES: EXEMPLARES ===");
        Client client = ClientBuilder.newClient();

        // 1. CRIAR EXEMPLAR (POST)
        System.out.println("\n--- [1] Criando Exemplar (POST) ---");
        WebTarget targetCriar = client.target(BASE_URI + "/livros/" + ID_LIVRO_TESTE + "/exemplares");
        
        Response responseCriar = targetCriar.request().post(null); // POST sem corpo, conforme sua implementação

        if (responseCriar.getStatus() != 201) {
            System.err.println("❌ Erro ao criar exemplar. Verifique se o Livro ID " + ID_LIVRO_TESTE + " existe.");
            System.err.println("Status: " + responseCriar.getStatus());
            return;
        }

        ExemplarDTO exemplar = responseCriar.readEntity(ExemplarDTO.class);
        Long idEx = exemplar.getId();
        System.out.println("✅ Exemplar criado! ID: " + idEx + " | Status Inicial: " + exemplar.getStatus());

        // -------------------------------------------------------------------
        // MÁQUINA DE ESTADOS: TESTES DE SUCESSO
        // -------------------------------------------------------------------
        
        // TENTATIVA 1: DISPONIVEL -> RESERVADO (Deve funcionar)
        System.out.println("\n--- [2] Teste Transição: DISPONIVEL -> RESERVADO (Permitido) ---");
        alterarStatus(client, idEx, "RESERVADO", 200);

        // TENTATIVA 2: RESERVADO -> EMPRESTADO (Deve funcionar)
        System.out.println("\n--- [3] Teste Transição: RESERVADO -> EMPRESTADO (Permitido) ---");
        alterarStatus(client, idEx, "EMPRESTADO", 200);

        // -------------------------------------------------------------------
        // MÁQUINA DE ESTADOS: TESTES DE ERRO (409 Conflict)
        // -------------------------------------------------------------------
        
        // TENTATIVA 3: EMPRESTADO -> RESERVADO (PROIBIDO!)
        System.out.println("\n--- [4] Teste Transição INVÁLIDA: EMPRESTADO -> RESERVADO (Proibido) ---");
        alterarStatus(client, idEx, "RESERVADO", 409);

        // TENTATIVA 4: EMPRESTADO -> DISPONIVEL (Deve funcionar - Ciclo completo)
        System.out.println("\n--- [5] Teste Transição: EMPRESTADO -> DISPONIVEL (Permitido) ---");
        alterarStatus(client, idEx, "DISPONIVEL", 200);

        // -------------------------------------------------------------------
        // LISTAGEM FILTRADA
        // -------------------------------------------------------------------
        System.out.println("\n--- [6] Teste GET com Filtro (?status=DISPONIVEL) ---");
        WebTarget targetListar = client.target(BASE_URI + "/livros/" + ID_LIVRO_TESTE + "/exemplares")
                                       .queryParam("status", "DISPONIVEL");
        
        String listaJson = targetListar.request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Lista retornada: " + listaJson);
        
        if (listaJson.contains(exemplar.getCodigoInterno())) {
            System.out.println("✅ O exemplar recém devolvido apareceu na lista de disponíveis!");
        }

        client.close();
    }

    // Método auxiliar para não repetir código
    private static void alterarStatus(Client client, Long id, String novoStatus, int statusEsperado) {
        WebTarget target = client.target(BASE_URI + "/exemplares/" + id + "/status")
                                 .queryParam("novoStatus", novoStatus);
        
        Response resp = target.request().put(Entity.json("")); // PUT vazio
        
        if (resp.getStatus() == statusEsperado) {
            System.out.println("✅ Sucesso! Retornou " + statusEsperado);
            if (statusEsperado == 200) {
                ExemplarDTO dto = resp.readEntity(ExemplarDTO.class);
                System.out.println("   Novo Status no Banco: " + dto.getStatus());
            } else {
                System.out.println("   Erro retornado (como esperado): " + resp.readEntity(String.class));
            }
        } else {
            System.err.println("❌ FALHA! Esperava " + statusEsperado + " mas veio " + resp.getStatus());
            System.err.println("   Msg: " + resp.readEntity(String.class));
        }
    }
}