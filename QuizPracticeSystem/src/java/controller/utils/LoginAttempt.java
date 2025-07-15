/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller.utils;

import java.util.*;

/**
 * <h4>Quản lý số lần đăng nhập sai và cơ chế khóa tài khoản tạm thời</h4>
 *
 * <p>Lớp tiện ích này cung cấp cơ chế đơn giản để:</p>
 * <ul>
 *   <li>Đếm số lần đăng nhập thất bại của người dùng.</li>
 *   <li>Khóa tạm thời tài khoản nếu vượt quá số lần cho phép.</li>
 *   <li>Phân biệt mức khóa nhẹ và nặng (5 lần & 10 lần).</li>
 *   <li>Tính thời gian còn lại cho đến khi được đăng nhập lại.</li>
 *   <li>Xóa trạng thái khóa nếu đăng nhập thành công.</li>
 * </ul>
 *
 * <p>Thiết kế sử dụng các `Map<String, ...>` lưu trữ trạng thái cho từng email người dùng.</p>
 *
 * <h5>Chính sách khóa:</h5>
 * <ul>
 *   <li>Sai 5 lần đầu tiên: khóa 30 giây.</li>
 *   <li>Sai 10 lần: khóa 30 phút.</li>
 * </ul>
 *
 * <p>Lưu ý: sau khi hết thời gian khóa, số lần sai không reset — phải đăng nhập đúng mới được xóa sạch trạng thái.</p>
 *
 * @author TuanKD
 */
public class LoginAttempt {

    private static final int MAX_ATTEMPT_FIRST = 5; //so lan nhap tai khoan sai
    private static final int MAX_ATTEMPT_SECOND = 10;
    private static final long FIRST_LOCK_DURATION = 30_000; //thoi gian bi khoa: 30 giay
    private static final long SECOND_LOCK_DURATION = 30 * 60 * 1000; // 30 phut
    //Map de luu tru thoi gian va tai khoan bi khoa
    private static final Map<String, Integer> attempts = new HashMap<>(); //dem so lan dang nhap sai
    private static final Map<String, Long> lockTime = new HashMap<>(); //luu tru thoi diem bat dau khoa hien tai
    private static final Map<String, Long> lockDuration = new HashMap<>(); //so thoi gian bi khoa

    /**
     * <h4>Gọi khi người dùng đăng nhập thất bại.</h4>
     * Tăng số lần sai, và nếu vượt mốc thì bắt đầu khóa tạm thời.
     *
     * @param email địa chỉ email người dùng.
     */
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

    /**
     * <h4>Kiểm tra tài khoản hiện tại có đang bị khóa không.</h4>
     *
     * @param email email người dùng.
     * @return true nếu vẫn đang bị khóa, false nếu đã hết thời gian hoặc chưa bị khóa.
     */
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

    /**
     * <h4>Tính thời gian còn lại đến khi tài khoản được mở khóa.</h4>
     *
     * @param email email người dùng.
     * @return thời gian còn lại (ms). Nếu không bị khóa, trả về 0.
     */
    public static long getRemainingLockTime(String email) {
        if (!lockTime.containsKey(email)) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - lockTime.get(email);
        long duration = lockDuration.getOrDefault(email, 0L);
        long remaining = duration - elapsed;
        return remaining > 0 ? remaining : 0;
    }

    /**
     * <h4>Gọi khi người dùng đăng nhập thành công.</h4>
     * Xóa mọi trạng thái sai/thời gian khóa.
     *
     * @param email email người dùng.
     */
    public static void loginSucceeded(String email) {
        attempts.remove(email);
        lockTime.remove(email);
        lockDuration.remove(email);
    }

    /**
     * <h4>Lấy số lần đăng nhập sai hiện tại của người dùng.</h4>
     *
     * @param email email người dùng.
     * @return số lần sai hiện tại.
     */
    public static int getAttempts(String email) {
        return attempts.getOrDefault(email, 0);
    }

}
