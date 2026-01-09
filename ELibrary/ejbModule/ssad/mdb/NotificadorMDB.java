package ssad.mdb;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.TextMessage;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ssad.modelo.NotificacaoHistorico;

@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:/jms/queue/ELibraryQueue"),
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue")
})
public class NotificadorMDB implements MessageListener {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                String textoJson = ((TextMessage) message).getText();
                
                System.out.println(">>> [MDB/EMAIL] Notificação recebida: " + textoJson);

                NotificacaoHistorico historico = new NotificacaoHistorico(0L, "EVENTO_ASSINCRONO", textoJson);
                em.persist(historico);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}