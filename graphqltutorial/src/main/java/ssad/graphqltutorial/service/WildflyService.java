package ssad.graphqltutorial.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import ssad.graphqltutorial.dto.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WildflyService {

    private final String WILDFLY_URL = "http://localhost:8080/ELibraryWeb/api";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<LivroDTO> listarTodosLivros() {
        try {
            LivroDTO[] response = restTemplate.getForObject(WILDFLY_URL + "/livros", LivroDTO[].class);
            List<LivroDTO> livros = response != null ? Arrays.asList(response) : new ArrayList<>();

            for (LivroDTO livro : livros) {
                try {
                    String urlExemplares = WILDFLY_URL + "/livros/" + livro.getId() + "/exemplares";
                    ExemplarDTO[] exemplares = restTemplate.getForObject(urlExemplares, ExemplarDTO[].class);
                    
                    if (exemplares != null) {
                        livro.setExemplares(Arrays.asList(exemplares));
                    } else {
                        livro.setExemplares(new ArrayList<>());
                    }
                } catch (Exception ex) {
                    livro.setExemplares(new ArrayList<>());
                }
            }
            return livros;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public LivroDTO buscarPorIsbn(String isbn) {
        return listarTodosLivros().stream()
                .filter(l -> isbn.equals(l.getIsbn()))
                .findFirst()
                .orElse(null);
    }

    public List<LivroDTO> listarComFiltro(LivroFiltro filtro) {
        List<LivroDTO> todos = listarTodosLivros(); 
        
        return todos.stream().filter(livro -> {
            boolean matchAutor = true;
            boolean matchStatus = true;

            if (filtro != null && filtro.getAutor() != null && !filtro.getAutor().isEmpty()) {
                if (livro.getAutor() == null) matchAutor = false;
                else matchAutor = livro.getAutor().toLowerCase().contains(filtro.getAutor().toLowerCase());
            }

            if (filtro != null && filtro.getStatus() != null && !filtro.getStatus().isEmpty()) {
                
                if (livro.getExemplares() == null || livro.getExemplares().isEmpty()) {
                    matchStatus = false;
                } else {
                    boolean temExemplar = livro.getExemplares().stream()
                        .anyMatch(ex -> ex.getStatus().equalsIgnoreCase(filtro.getStatus()));

                    if (temExemplar) {
                        List<ExemplarDTO> apenasOsBons = livro.getExemplares().stream()
                            .filter(ex -> ex.getStatus().equalsIgnoreCase(filtro.getStatus()))
                            .collect(Collectors.toList());
                        
                        livro.setExemplares(apenasOsBons); // Atualiza a lista do livro
                        matchStatus = true;
                    } else {
                        matchStatus = false;
                    }
                }
            } 
            else {
                 if (livro.getExemplares() != null) {
                     matchStatus = livro.getExemplares().stream()
                        .anyMatch(ex -> "DISPONIVEL".equalsIgnoreCase(ex.getStatus()));
                 }
            }

            return matchAutor && matchStatus;
        }).collect(Collectors.toList());
    }

    public ExemplarDTO alterarStatusExemplar(Long id, String status) {
        String url = WILDFLY_URL + "/exemplares/" + id + "/status?valor=" + status;
        
        try {
            System.out.println("Alterando status ID: " + id + " para " + status);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<String> request = new HttpEntity<>("{}", headers);

            restTemplate.postForObject(url, request, String.class);
            
            ExemplarDTO ex = new ExemplarDTO();
            ex.setId(id);
            ex.setStatus(status);
            return ex;
        } catch (Exception e) {
            System.err.println("Erro ao alterar status: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public LivroDTO cadastrarLivro(LivroInput input) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            HttpEntity<LivroInput> request = new HttpEntity<>(input, headers);
            return restTemplate.postForObject(WILDFLY_URL + "/livros", request, LivroDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public DashboardDTO getDashboard() {
        List<LivroDTO> livros = listarTodosLivros(); 
        
        DashboardDTO dash = new DashboardDTO();
        dash.setTotalLivros(livros.size());
        
        int exemplares = 0;
        int disponiveis = 0;
        int reservados = 0;
        int emprestados = 0;

        for (LivroDTO l : livros) {
            if (l.getExemplares() != null) {
                exemplares += l.getExemplares().size();
                for (ExemplarDTO ex : l.getExemplares()) {
                    String st = ex.getStatus() != null ? ex.getStatus().toUpperCase() : "";
                    if (st.equals("DISPONIVEL")) disponiveis++;
                    if (st.equals("RESERVADO")) reservados++;
                    if (st.equals("EMPRESTADO")) emprestados++;
                }
            }
        }
        
        dash.setTotalExemplares(exemplares);
        dash.setTotalDisponiveis(disponiveis);
        dash.setTotalReservados(reservados);
        dash.setTotalEmprestados(emprestados);
        
        return dash;
    }
    
    public List<EmprestimoDTO> emprestimosAtivos(Long usuarioId) {
        try {
            EmprestimoDTO[] response = restTemplate.getForObject(WILDFLY_URL + "/emprestimos", EmprestimoDTO[].class);
            List<EmprestimoDTO> todos = response != null ? Arrays.asList(response) : new ArrayList<>();

            return todos.stream()
                    .filter(e -> e.getUsuarioId() != null && e.getUsuarioId().equals(usuarioId))
                    .filter(e -> e.getDataDevolucao() == null)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Erro ao buscar emprestimos (endpoint existe?): " + e.getMessage());
            return new ArrayList<>();
        }
    }
}