package ssad.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

// Isso define que todos os seus endpoints começarão com /api
// Exemplo: http://localhost:8080/ELibraryWeb/api/livros
@ApplicationPath("/api")
public class RestApplication extends Application {
}