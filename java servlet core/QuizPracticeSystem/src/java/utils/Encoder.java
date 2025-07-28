/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.Base64;

/**
 * @author TranHoan
 */
/*
Dùng để mã hóa các chuỗi cần thiết
VD: Giấu id đi không cho kẻ gian truy cập vào DB, tránh lộ dữ liệu nhạy cảm
 */
public class Encoder {
    public static String encode(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes());
    }

    public static String decode(String str) {
        return new String(Base64.getUrlDecoder().decode(str));
    }
}
