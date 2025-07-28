package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.logging.Logger;

/**
 * <h4>Khởi tạo trò chuyện với Ollama khi ứng dụng khởi động</h4>
 *
 * <p>Lớp này triển khai {@link ServletContextListener} để gửi một prompt ban đầu
 * đến mô hình ngôn ngữ chạy cục bộ thông qua Ollama khi ứng dụng được khởi động.</p>
 *
 * <p>Prompt khởi tạo mô tả vai trò của trợ lý ảo "Miss" — một người hỗ trợ thân thiện, ngắn gọn và dễ hiểu
 * nhằm hỗ trợ người học trong nền tảng thi trắc nghiệm trực tuyến.</p>
 *
 * <h5>Hành vi chính:</h5>
 * <ul>
 *   <li>Kết nối đến <code><a href="http://localhost:11434/api/generate">...</a></code> (API mặc định của Ollama).</li>
 *   <li>Gửi prompt dưới dạng JSON, yêu cầu phản hồi <code>stream: false</code>.</li>
 *   <li>Log thông tin về kết quả gửi thành công hay thất bại.</li>
 * </ul>
 *
 * <h5>Ví dụ prompt:</h5>
 * <pre>{@code
 * You are a virtual assistant named “Miss” ...
 * }</pre>
 *
 * <p>Đảm bảo rằng Ollama đang chạy và có sẵn model <code>gemma3:4b</code> trước khi khởi động ứng dụng.</p>
 *
 * @author HoanTX
 * @see <a href="https://github.com/ollama/ollama">Ollama - Local LLM API</a>
 */
@WebListener
public class OllamaInitialization implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(OllamaInitialization.class.getName());
    private static final String INITIAL_PROMPT = """
            You are a virtual assistant named “Miss” for a website offering online courses and quizzes. Your personality is kind, caring, and supportive, like a gentle teacher.
            
            Your mission is to help users (whom you call “you”) learn by answering their questions in a **very short**, clear, and friendly way.
            
            🚫 Never give long, multi-paragraph responses. \s
            ✅ Always reply in **1–3 short sentences**, using very simple language. \s
            ✅ Stay on-topic, avoid giving too many suggestions at once. \s
            ✅ Be warm, encouraging, and patient like a tutor. \s
            ✅ If a user needs more info, wait for them to ask — don’t give everything at once.
            
            Start each session with a gentle greeting like: \s
            **“Hello, dear student! I’m Miss, your virtual assistant. How can I help you today?”**
            
            Always keep responses minimal, supportive, and easy to understand. If unsure, politely ask the user to clarify.
            
            ---
            
            You’re now ready to chat with learners!
            """;

    /**
     * <h4>Gửi prompt khởi tạo đến Ollama khi ứng dụng được khởi động.</h4>
     *
     * @param sce sự kiện khởi tạo context, không được sử dụng.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("🔥 Sending initial prompt to Ollama...");

            var url = new URI("http://localhost:11434/api/generate");
            var conn = (HttpURLConnection) url.toURL().openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");

            var initialPrompt = String.format("""
                    {
                        "model": "gemma3:4b",
                        "prompt": %s,
                        "stream": false
                    }
                    """, new ObjectMapper().writeValueAsString(INITIAL_PROMPT));

            try (var os = conn.getOutputStream()) {
                os.write(initialPrompt.getBytes());
                os.flush();
            }

            var responseCode = conn.getResponseCode();
            logger.info("🌱 Initial prompt sent. Ollama responded with code: " + responseCode);
        } catch (Exception e) {
            logger.warning("🚨 Failed to send initial prompt to Ollama: " + e.getMessage());
        }
    }
}
