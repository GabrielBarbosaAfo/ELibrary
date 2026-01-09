package br.elibrary.rest.client;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import ssad.dto.LivroDTO;

public class TesteRestLivro {

    private static final String BASE_URI = "http://localhost:8080/ELibraryWeb/api";

    public static void main(String[] args) {
        System.out.println("=== 📚 INICIANDO BATERIA DE TESTES: LIVROS ===");
        
        Client client = ClientBuilder.newClient();
        
        // Gera um ISBN único para não travar o teste de duplicidade
        String isbnUnico = "978-TEST-" + System.currentTimeMillis();

        // -------------------------------------------------------------------
        // REQUISITO 1: POST /livros (Formulário HTML + Redirecionamento 303)
        // -------------------------------------------------------------------
        System.out.println("\n--- [1] Teste POST (Cadastro via Form) ---");
        
        Form form = new Form();
        form.param("titulo", "Livro Teste REST");
        form.param("isbn", isbnUnico);
        form.param("autor", "Tester da Silva");
        form.param("ano", "2024");

        WebTarget targetLivros = client.target(BASE_URI + "/livros");
        
        // JAX-RS Client segue redirecionamentos automaticamente.
        // Se o servidor retornar 303 apontando para /livros/{id}, o cliente vai seguir
        // e nos dar o status 200 do GET final.
        Response responsePost = targetLivros
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));

        System.out.println("Status Final: " + responsePost.getStatus() + " (Esperado 200 se seguiu o redirect 303)");
        
        LivroDTO livroCriado = null;
        if (responsePost.getStatus() == 200) {
            livroCriado = responsePost.readEntity(LivroDTO.class);
            System.out.println("✅ Sucesso! O servidor redirecionou e retornou o livro:");
            System.out.println("   ID: " + livroCriado.getId());
            System.out.println("   Título: " + livroCriado.getTitulo());
        } else {
            System.err.println("❌ Falha no cadastro: " + responsePost.readEntity(String.class));
            return; // Para o teste se falhar aqui
        }

        // -------------------------------------------------------------------
        // REQUISITO: Validar Unicidade ISBN (Deve dar 409 Conflict)
        // -------------------------------------------------------------------
        System.out.println("\n--- [2] Teste POST (ISBN Duplicado - Esperado 409) ---");
        Response responseDuplicado = targetLivros
                .request()
                .post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED));
        
        if (responseDuplicado.getStatus() == 409) {
            System.out.println("✅ Validação de Unicidade Funcionou! (Status 409)");
        } else {
            System.err.println("❌ Falha: Deveria ter retornado 409, mas veio " + responseDuplicado.getStatus());
        }

        // -------------------------------------------------------------------
        // REQUISITO: GET /livros (Filtros Opcionais)
        // -------------------------------------------------------------------
        System.out.println("\n--- [3] Teste GET (Filtro por Autor) ---");
        // Vamos buscar pelo autor que acabamos de criar "Tester da Silva"
        WebTarget targetFiltro = client.target(BASE_URI + "/livros").queryParam("autor", "Tester");
        
        String jsonLista = targetFiltro.request(MediaType.APPLICATION_JSON).get(String.class);
        System.out.println("Resultado da busca por 'Tester': " + jsonLista);
        
        if (jsonLista.contains("Livro Teste REST")) {
            System.out.println("✅ Filtro funcionando! Encontrou o livro pelo autor.");
        } else {
            System.err.println("❌ Filtro falhou.");
        }

        // -------------------------------------------------------------------
        // REQUISITO: PUT /livros/{id} (Atualizar)
        // -------------------------------------------------------------------
        System.out.println("\n--- [4] Teste PUT (Atualizar Título) ---");
        
        Long id = livroCriado.getId();
        livroCriado.setTitulo("Livro Teste REST - ATUALIZADO");
        
        WebTarget targetId = client.target(BASE_URI + "/livros/" + id);
        Response responsePut = targetId
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.json(livroCriado));
        
        if (responsePut.getStatus() == 200) {
            LivroDTO atualizado = responsePut.readEntity(LivroDTO.class);
            System.out.println("✅ Atualizado: " + atualizado.getTitulo());
        } else {
            System.err.println("❌ Erro ao atualizar: " + responsePut.getStatus());
        }

        client.close();
    }
}