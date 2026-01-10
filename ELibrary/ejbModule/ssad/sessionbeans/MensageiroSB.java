package ssad.sessionbeans;

import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;

@Stateless
public class MensageiroSB {

    @Inject
    private JMSContext context;

    @Resource(lookup = "java:/jms/queue/ELibraryQueue")
    private Queue fila;

    public void enviarNotificacao(String mensagemJson) {
        try {
            context.createProducer().send(fila, mensagemJson);
            System.out.println(">>> [MensageiroSB] Mensagem enviada para fila: " + mensagemJson);
        } catch (Exception e) {
            System.err.println(">>> [MensageiroSB] Erro ao enviar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}