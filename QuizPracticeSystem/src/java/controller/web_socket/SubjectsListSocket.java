package controller.web_socket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/answer")
public class SubjectsListSocket {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(SubjectsListSocket.class.getName());

    @OnOpen
    public void handleOpen(EndpointConfig endpointConfig, Session session) {
        logger.log(Level.INFO, "New connection established: {0} {1}", new Object[]{session.getId(), endpointConfig.getUserProperties().toString()});
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.log(Level.INFO, "Received message: {0} from session: {1}", new Object[]{message, session.getId()});
        sessions.put(message, session);
        
        try {
            String response = "Processed: " + message;
            session.getBasicRemote().sendText(response); // Phải có dòng này
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Send failed", e);
        }
    }

    @OnClose
    public void onClose(Session session) {
        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
        logger.log(Level.INFO, "Connection closed: {0}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.log(Level.SEVERE, "Error for " + session.getId(), throwable);
    }

    public static void notifyClient(String userId, String prompt) {
        Session session = sessions.get(userId);
        if (session == null) {
            logger.log(Level.WARNING, "No session found for user: {0}", userId);
            return;
        }

        if (!session.isOpen()) {
            logger.log(Level.WARNING, "Session exists but closed for user: {0}", userId);
            sessions.remove(userId);
            return;
        }
        
        try {
            logger.log(Level.INFO, "Prompt [{0}] has been notified.", prompt);
            session.getBasicRemote().sendText(prompt);
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
