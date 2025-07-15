package controller;

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

// Phuong thuc mau, khong duoc chinh sua
@WebServlet("/ask")
public class OllamaController extends HttpServlet {

    private final HandleRequestBody hrb;
    private final HandleOllamaAssistant assistant;
    private final Logger logger = Logger.getLogger(OllamaController.class.getName());
    private static final String GREETING_PROMPT = """
            You are Miss, a friendly virtual tutor. Greet the student warmly in 1–2 short, polite sentences as if you're starting an online lesson.
            Use simple language. Call yourself “Miss” and refer to the user as “you”.
            ---
            Example: Hello, dear student! I'm Miss, your assistant. How can I help you today?
            """;
    private static final String KEYWORD_EXTRACTION_PROMPT = """
            Analyze the following sentence and extract the most relevant keywords for educational course search.
            Remove duplicates, stop words, and return the keywords as a single, comma-separated list in lowercase:
            
            "%s"
            
            Only return the list, without explanation or formatting.
            Response in English
            """;

    public OllamaController() {
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
            logger.log(Level.SEVERE, e.getMessage());
        } catch (Exception ex) {
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            logger.info("Received request to /ask endpoint");
            Map<String, Object> params = hrb.getDataFromRequest(req);
            String prompt = (String) params.get("prompt");
            logger.log(Level.INFO, "Prompt : {0}", prompt);

//            String realPrompt = KEYWORD_EXTRACTION_PROMPT.formatted(prompt);
            String body = assistant.preparePrompt(prompt);
            assistant.streamAnswerToBrowser(resp, body);
        } catch (URISyntaxException ex) {
            logger.log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(OllamaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
