package controller.assistant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class HandleOllamaAssistant {

    private final static String REQUEST_METHOD = "POST";
    private final static String CONTENT_TYPE = "application/json";
    private final static String OLLAMA_END_POINT = "http://localhost:11434/api/generate";
    private final static String PROMPT_FORMAT
            = """
            {
                "model": "gemma3:4b",
                "prompt": %s,
                "stream": true
            }""";
    private final Logger logger = Logger.getLogger(HandleOllamaAssistant.class.getName());

    /**
     * <h3>Gửi prompt tới mô hình AI (Gemma) và stream phản hồi trực tiếp về
     * phía trình duyệt (client).</h3>
     * <p>
     * Phương thức này được sử dụng sau khi đã chuẩn bị prompt bằng
     * {@link #preparePrompt(String)}.</p>
     * <p>
     * Dữ liệu phản hồi sẽ được đọc từng dòng từ AI và ghi trực tiếp vào output
     * stream của response HTTP.</p>
     *
     * @param resp Đối tượng {@link HttpServletResponse} dùng để ghi dữ liệu
     * phản hồi.
     * @param body Dữ liệu prompt đã được định dạng sẵn.
     * @throws IOException Nếu có lỗi I/O xảy ra trong quá trình kết nối hoặc
     * ghi phản hồi.
     * @throws ServletException Nếu có lỗi liên quan đến servlet trong quá trình
     * xử lý.
     * @throws URISyntaxException Nếu endpoint Gemma có lỗi định dạng URI.
     * @author HoanTX
     */
    public void streamAnswerToBrowser(HttpServletResponse resp, String body)
            throws IOException, ServletException, URISyntaxException {
        logger.log(Level.INFO, "streamAnswerToBrowser");
        var conn = prepareBeforeQuery(resp);
        conn.getOutputStream().write(body.getBytes());
        conn.getOutputStream().flush();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); PrintWriter writer = resp.getWriter()) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.println(line);
                writer.flush();
            }
        }
    }

    /**
     * <h3>Gửi prompt tới mô hình AI (Gemma) và nhận toàn bộ phản hồi dưới dạng
     * chuỗi để xử lý tiếp phía server.</h3>
     *
     * <p>
     * Dữ liệu phản hồi được đọc từ mô hình và ánh xạ từng dòng thành đối tượng
     * {@link ResponseData},</p>
     * <p>
     * sau đó nối toàn bộ phản hồi lại để trả về một chuỗi duy nhất.</p>
     *
     * @param resp Đối tượng {@link HttpServletResponse} để thiết lập thông tin
     * header.
     * @param body Dữ liệu prompt đã được định dạng sẵn.
     * @return Chuỗi phản hồi từ AI (dạng đầy đủ, đã nối lại từ nhiều phần).
     * @throws IOException Nếu có lỗi I/O trong quá trình kết nối hoặc đọc dữ
     * liệu.
     * @throws URISyntaxException Nếu endpoint Gemma có định dạng URI không hợp
     * lệ.
     * @author HoanTX
     */
    public String getAnswer(HttpServletResponse resp, String body)
            throws IOException, URISyntaxException {
        logger.log(Level.INFO, "getAnswer");
        var fullAnswer = new StringBuilder();
        var gson = new Gson();

        var conn = prepareBeforeQuery(resp);
        conn.getOutputStream().write(body.getBytes());
        conn.getOutputStream().flush();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                var data = gson.fromJson(line, ResponseData.class);
                if (data != null) {
                    fullAnswer.append(data.response);
                }
            }
        }
        return fullAnswer.toString();
    }

    /**
     * <h3>Chuẩn bị prompt đầu vào để gửi cho mô hình AI (Gemma), định dạng theo
     * cấu trúc chuẩn JSON.</h3>
     *
     * <p>
     * Thêm tiền tố "User: " vào trước prompt người dùng và đóng gói thành chuỗi
     * JSON theo định dạng yêu cầu của mô hình.</p>
     *
     * @param prompt Nội dung yêu cầu từ người dùng.
     * @return Chuỗi JSON chứa prompt đã được đóng gói để gửi đến AI.
     * @throws JsonProcessingException Nếu có lỗi trong quá trình chuyển đổi
     * chuỗi sang JSON.
     * @author HoanTX
     */
    public String preparePrompt(String prompt) throws JsonProcessingException {
        logger.log(Level.INFO, "preparePrompt");
        var pr = new StringBuilder();
        pr.append("User: ");
        pr.append(prompt);

        var requestBody = String.format(PROMPT_FORMAT, new ObjectMapper().writeValueAsString(pr));
        logger.log(Level.INFO, "Request body: {0}", requestBody);
        return requestBody;
    }

    /**
     * <h3>Cấu hình kết nối HTTP tới endpoint của mô hình AI Gemma trước khi gửi
     * prompt.</h3>
     *
     * <p>
     * Thiết lập content type, encoding và method cho HTTP request. Đồng thời
     * cấu hình response để trả về dữ liệu dạng UTF-8.</p>
     *
     * @param resp Đối tượng {@link HttpServletResponse} dùng để thiết lập định
     * dạng phản hồi.
     * @return Đối tượng {@link HttpURLConnection} đã được cấu hình sẵn.
     * @throws URISyntaxException Nếu endpoint Gemma có lỗi định dạng URI.
     * @throws IOException Nếu có lỗi I/O khi mở kết nối.
     * @author HoanTX
     */
    private HttpURLConnection prepareBeforeQuery(HttpServletResponse resp) throws URISyntaxException, IOException {
        resp.setContentType(CONTENT_TYPE);
        resp.setCharacterEncoding("UTF-8");

        var uri = new URI(OLLAMA_END_POINT);
        var conn = (HttpURLConnection) uri.toURL().openConnection();

        conn.setDoOutput(true);
        conn.setRequestMethod(REQUEST_METHOD);
        conn.setRequestProperty("Content-Type", CONTENT_TYPE);
        return conn;
    }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class ResponseData {

    String model;
    String created_at;
    String response;
    String done;

}
