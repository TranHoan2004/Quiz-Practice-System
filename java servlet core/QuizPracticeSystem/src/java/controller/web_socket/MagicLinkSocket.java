package controller.web_socket;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/magic-link")
public class MagicLinkSocket {

    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Logger logger = Logger.getLogger(MagicLinkSocket.class.getName());

    @OnOpen
    public void onOpen(Session session) {
        logger.log(Level.INFO, "New connection established: {0}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.log(Level.INFO, "Received message: {0} from session: {1}", new Object[]{message, session.getId()});
        sessions.put(message, session);

        try {
            session.getBasicRemote().sendText("CONNECTED");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error sending ACK", e);
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        sessions.values().removeIf(s -> s.getId().equals(session.getId()));
        logger.log(Level.INFO, "Connection closed: {0}", session.getId());
        session.close();
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        logger.log(Level.SEVERE, "Error for " + session.getId(), throwable);
    }

    public static void notifyClient(String userId, boolean status) {
        logger.log(Level.INFO, "Trying to notify client: {0}", userId);
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
            String message = status ? "VALID_TOKEN" : "INVALID_TOKEN";
            logger.log(Level.INFO, "Sending message: {0} to user: {1}", new Object[]{message, userId});
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error notifying client " + userId, e);
            sessions.remove(userId);
        }
    }
}
