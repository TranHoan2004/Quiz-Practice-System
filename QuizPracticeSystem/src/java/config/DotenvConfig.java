package config;

import io.github.cdimascio.dotenv.Dotenv;

import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static String get(String key) {
        if (dotenv == null) {
            throw new IllegalStateException("Dotenv chưa được khởi tạo");
        }
        return dotenv.get(key);
    }
}
