/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.utils;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Lenovo
 */
public class LoginAttempt {

    //so lan nhap tai khoan sai 
    private static final int MAX_ATTEMPT_FIRST = 5;
    private static final int MAX_ATTEMPT_SECOND = 10;
    //thoi gian bi khoa
    private static final long FIRST_LOCK_DURATION = 30_000; //30 giay
    private static final long SECOND_LOCK_DURATION = 30 * 60 * 1000; // 30 phut
    //Map de luu tru thoi gian va tai khoan bi khoa
    private static final Map<String, Integer> attempts = new HashMap<>(); //dem so lan dang nhap sai
    private static final Map<String, Long> lockTime = new HashMap<>(); //luu tru thoi diem bat dau khoa hien tai
    private static final Map<String, Long> lockDuration = new HashMap<>(); //so thoi gian bi khoa
    //check dang nhap sai 

    public static void loginFailed(String email) {
        int currentAttempts = attempts.getOrDefault(email, 0) + 1;
        attempts.put(email, currentAttempts);
        if (currentAttempts == MAX_ATTEMPT_FIRST) {
            lockTime.put(email, System.currentTimeMillis());
            lockDuration.put(email, FIRST_LOCK_DURATION);
        } else if (currentAttempts == MAX_ATTEMPT_SECOND) {
            lockTime.put(email, System.currentTimeMillis());
            lockDuration.put(email, SECOND_LOCK_DURATION);
        }
    }
    //Kiem tra tai khoan co dang bi khoa khong

    public static boolean isBlocked(String email) {
        if (!lockTime.containsKey(email)) {
            return false;
        }

        long elapsed = System.currentTimeMillis() - lockTime.get(email);
        long duration = lockDuration.getOrDefault(email, 0L);

        if (elapsed > duration) {
            lockTime.remove(email);
            lockDuration.remove(email);
            // Không reset số lần sai → tiếp tục tăng cho đến khi người đó đăng nhập đúng
            return false;
        }
        return true;
    }
    //Tinh Thoi Gian Con Lai 

    public static long getRemainingLockTime(String email) {
        if (!lockTime.containsKey(email)) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - lockTime.get(email);
        long duration = lockDuration.getOrDefault(email, 0L);
        long remaining = duration - elapsed;
        return remaining > 0 ? remaining : 0;
    }
    //Neu nguoi dung dang nhap thanh cong thi xoa tai khoan khoi hashmap

    public static void loginSucceeded(String email) {
        attempts.remove(email);
        lockTime.remove(email);
        lockDuration.remove(email);
    }

    public static int getAttempts(String email) {
        return attempts.getOrDefault(email, 0);
    }

}
