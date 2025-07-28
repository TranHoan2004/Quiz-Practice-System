package controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import controller.assistant.HandleOllamaAssistant;
import controller.utils.HandleRequestBody;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/stats")
public class DashboardOllamaController extends HttpServlet {

    private final HandleRequestBody hrb;
    private final HandleOllamaAssistant assistant;
    private final Logger logger = Logger.getLogger(DashboardOllamaController.class.getName());

    private static final String GREETING_PROMPT = """
        You are Miss, a friendly virtual tutor. Greet the student warmly in 1–2 short, polite sentences as if you're starting an online lesson.
        Use simple language. Call yourself “Miss” and refer to the user as “you”.
        ---
        Example: Hello, dear student! I'm Miss, your assistant. How can I help you today?
        """;

    private static final String FLEXIBLE_ANALYSIS_PROMPT = """
        You are Miss, a helpful and friendly AI assistant specialized in marketing, data analysis, and educational insights.

        👩‍💼 When data is available, please incorporate it in your analysis. If the data is incomplete or not directly related, still try your best to give relevant insights or helpful suggestions.

        📊 Provided Data (if any):
        %s

        ❓ User Question:
        %s

        👉 Your Tasks:
        - Answer in a concise and informative way, suitable for marketers or educators.
        - You can give insights, trends, ideas, or marketing actions based on the question.
        """;

    public DashboardOllamaController() {
        this.hrb = new HandleRequestBody();
        this.assistant = new HandleOllamaAssistant();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("doGet");

        try {
            String body = assistant.preparePrompt(GREETING_PROMPT);
            assistant.streamAnswerToBrowser(resp, body);
        } catch (ServletException | IOException | URISyntaxException e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            logger.info("Received request to /stats endpoint");

            Map<String, Object> params = hrb.getDataFromRequest(req);
            String prompt = (String) params.getOrDefault("prompt", "");
            String insightContextRaw = (String) params.getOrDefault("insightContext", "{}");

            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> contextMap = mapper.readValue(insightContextRaw, new TypeReference<Map<String, Object>>() {
            });

            StringBuilder contextBuilder = new StringBuilder();
            contextMap.forEach((k, v) -> {
                if (v != null && !v.toString().isBlank()) {
                    contextBuilder.append("• ").append(k).append(": ").append(v.toString()).append("\n");
                }
            });

            String finalContext = contextBuilder.toString().isBlank() ? "No additional data provided." : contextBuilder.toString();
            String combinedPrompt = FLEXIBLE_ANALYSIS_PROMPT.formatted(finalContext, prompt.trim());

            logger.log(Level.INFO, "Prompt being sent: \n{0}", combinedPrompt);

            String body = assistant.preparePrompt(combinedPrompt);
            assistant.streamAnswerToBrowser(resp, body);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Error handling POST /stats", ex);
        }
    }
}
