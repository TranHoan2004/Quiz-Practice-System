package com.qps.adapter.websocket.endpoints;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Slf4j
@Component
@ServerEndpoint("/websocket/{conversationId}/{role}")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatWebSocketEndpoints {
    static Map<String, Set<Session>> conversations = Collections.synchronizedMap(new HashMap<>());

    @OnOpen
    public void onOpen(@PathParam("conversationId") String conversationId,
                       @PathParam("role") String role, Session session) {
        // Store the role in the session properties
        session.getUserProperties().put("role", role);
        conversations.computeIfAbsent(conversationId, k -> Collections.synchronizedSet(new HashSet<>())).add(session);
    }

    @OnMessage
    public void onMessage(@PathParam("conversationId") String conversationId, String msg, Session session) {
        // Retrieve the sender's role from the session properties
//        String role = (String) session.getUserProperties().get("role");

        // Create and populate ChatDetail object
//        ChatDetail cd = new ChatDetail();
//        Chat chat = new Chat();
//        chat.setId(Integer.parseInt(conversationId));
//        cd.setChat(chat);
//        cd.setMessage(msg);
//        cd.setSender(role);
//        cd.setTimestamp(new Date());
//
//         Save chat details to the database
//        ChatDetailRepositoryImpl chatDetailRepository = SpringContext.getBean(ChatDetailRepositoryImpl.class);
//        try {
//            chatDetailRepository.saveChatDetail(cd);
//            System.out.println("Message saved to DB for conversationId: " + conversationId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        broadcast(conversationId, msg, session);
    }

    @OnClose
    public void onClose(@PathParam("conversationId") String conversationId, Session session) {
        var conversationSessions = conversations.get(conversationId);
        if (conversationSessions != null) {
            conversationSessions.remove(session);
            if (conversationSessions.isEmpty()) {
                conversations.remove(conversationId);
            }
        }
    }

    private void broadcast(String conversationId, String message, Session senderSession) {
        var conversationSessions = conversations.get(conversationId);
        if (conversationSessions != null) {
            for (var client : conversationSessions) {
                if (client.isOpen() && !client.equals(senderSession)) {
                    try {
                        client.getBasicRemote().sendText(message);
                    } catch (IOException e) {
                        log.warn("Error broadcasting message: {}", e.getMessage());
                    }
                }
            }
        }
    }
}
