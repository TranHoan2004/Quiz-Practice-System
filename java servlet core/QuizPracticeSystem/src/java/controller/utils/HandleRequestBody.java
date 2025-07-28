package controller.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <h4>Tiện ích xử lý dữ liệu JSON từ thân yêu cầu HTTP</h4>
 *
 * <p>Lớp này cung cấp phương thức để đọc và phân tích nội dung <strong>request body</strong>
 * dạng JSON từ một đối tượng {@link HttpServletRequest}, sau đó chuyển thành {@link Map}&lt;String, String&gt;.</p>
 *
 * <p>Thích hợp dùng cho các API kiểu <code>POST</code>/<code>PUT</code> gửi dữ liệu dưới dạng JSON
 * trong phần thân yêu cầu (body).</p>
 *
 * <h5>Chức năng chính:</h5>
 * <ul>
 *   <li>Đọc toàn bộ dữ liệu từ input stream của request.</li>
 *   <li>Chuyển dữ liệu chuỗi thành đối tượng Map thông qua thư viện Jackson.</li>
 *   <li>Trả về Map chứa các cặp key–value từ JSON đã parse.</li>
 * </ul>
 *
 * <h5>Ví dụ JSON:</h5>
 * <pre>{@code
 * {
 *   "email": "user@example.com",
 *   "password": "123456"
 * }
 * }</pre>
 *
 * <p><strong>Lưu ý:</strong> class chỉ hỗ trợ JSON có cấu trúc phẳng kiểu key–value chuỗi.</p>
 *
 * @author HoanTX
 * @see com.fasterxml.jackson.databind.ObjectMapper
 * @see jakarta.servlet.http.HttpServletRequest
 */
public class HandleRequestBody {
    /**
     * Đọc toàn bộ nội dung JSON từ request body và chuyển thành Map&lt;String, String&gt;.
     *
     * @param req đối tượng {@link HttpServletRequest} chứa request từ client.
     * @return một {@link Map} chứa dữ liệu JSON đã parse.
     * @throws IOException nếu có lỗi khi đọc input stream hoặc parse JSON.
     */
    public Map<String, Object> getDataFromRequest(HttpServletRequest req) throws IOException {
        Logger logger = Logger.getLogger(HandleRequestBody.class.getName());
        var br = new BufferedReader(new InputStreamReader(req.getInputStream()));
        var sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        var json = sb.toString();
        var mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<>() {
        });
    }
}
