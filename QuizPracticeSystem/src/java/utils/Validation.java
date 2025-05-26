/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.util.regex.Pattern;

/**
 *
 * @author TranHoan
 */
public class Validation {

    // Method to check if the email is valid
    public static boolean isValidEmail(String email) {
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);

        // Check if email matches the pattern
        return email != null && p.matcher(email).matches();
    }

    public static boolean isValidVietnamesePhone(String phone) {
        //Vietnamese phone numer regex 
        String regex = "^(\\+84|0)(3|5|7|8|9)\\d{8}$";

        return phone != null && phone.matches(regex);
    }

}
