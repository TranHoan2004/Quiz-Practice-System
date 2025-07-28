package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * <h2>Cấu hình biến môi trường với Dotenv</h2>
 *
 * <p>Class tiện ích giúp load và truy cập các biến môi trường được định nghĩa trong file <code>.env</code>
 * thông qua thư viện <strong>dotenv-java</strong>.</p>
 *
 * <p>Biến <code>.env</code> được load ngay khi class này được nạp vào JVM. Nếu không thể load được file,
 * chương trình sẽ log lỗi với cấp độ <code>SEVERE</code>.</p>
 *
 * <h3>Ghi chú:</h3>
 * <ul>
 *   <li>Nếu không tìm thấy biến, phương thức <code>dotenv.get(key)</code> sẽ trả về <code>null</code>.</li>
 *   <li>Đảm bảo rằng file <code>.env</code> nằm trong thư mục gốc của project hoặc đường dẫn phù hợp.</li>
 * </ul>
 *
 * @author HoanTX
 */
public class DotenvConfig {
    private static final Dotenv dotenv;
    private static final Logger logger = Logger.getLogger(DotenvConfig.class.getName());

    static {
        Dotenv temp = null;
        try {
            temp = Dotenv.configure()
                    .filename(".env")
                    .load();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Không thể load file .env: " + e.getMessage());
        }
        dotenv = temp;
    }

    /**
     * <h4>Lấy giá trị của một biến môi trường theo key.</h4>
     *
     * @param key tên biến môi trường cần truy cập.
     * @return giá trị tương ứng với <code>key</code>, hoặc <code>null</code> nếu không tồn tại.
     * @throws IllegalStateException nếu file <code>.env</code> chưa được load thành công.
     */
    public static String get(String key) {
        if (dotenv == null) {
            throw new IllegalStateException("Dotenv chưa được khởi tạo");
        }
        return dotenv.get(key);
    }
}
