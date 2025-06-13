package controller.web_socket;

import jakarta.websocket.EndpointConfig;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint(value = "/magic-link")
public class MagicLinkSocket {
    private static final Map<String, Session> sessions = Collections.synchronizedMap(new HashMap<>());
    private static final Logger logger = Logger.getLogger(MagicLinkSocket.class.getName());

    @OnOpen
    public void handleOpen(EndpointConfig endpointConfig, Session session) {
        logger.info("New connection established: " + session.getId() + " " + endpointConfig.getUserProperties().toString());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("Received message: " + message + " from session: " + session.getId());
        sessions.put(message, session);
    }

    public static void notifyClient(String userId, boolean status) {
        logger.info("Client " + userId + " has been notified.");
        Session session = sessions.get(userId);
        try {
            if (session != null && session.isOpen()) {
                if (status) {
                    session.getBasicRemote().sendText("VALID_TOKEN");
                } else {
                    session.getBasicRemote().sendText("INVALID_TOKEN");
                }
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }
}
