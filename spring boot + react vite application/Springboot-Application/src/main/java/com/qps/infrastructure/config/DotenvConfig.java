package com.qps.infrastructure.config;//package com.qps.config;
//
//import io.github.cdimascio.dotenv.Dotenv;
//import jakarta.annotation.PostConstruct;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//public class DotenvConfig {
//    private Dotenv dotenv;
//
//    @PostConstruct
//    public void init() {
//        Dotenv temp = null;
//        try {
//            temp = Dotenv.configure()
//                    .filename(".env")
//                    .load();
//        } catch (Exception e) {
//            log.error("Không thể load file .env: {}", e.getMessage());
//        }
//        dotenv = temp;
//    }
//
//    public String get(String key) {
//        if (dotenv == null) {
//            throw new IllegalStateException("Dotenv chưa được khởi tạo");
//        }
//        return dotenv.get(key);
//    }
//}
