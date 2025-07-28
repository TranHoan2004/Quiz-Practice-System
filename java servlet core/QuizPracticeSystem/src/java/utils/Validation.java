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
        String regex = "^(\\+84|0)([35789])\\d{8}$";

        return phone != null && phone.matches(regex);
    }

    public static String validatePricePackage(int listPrice, int salePrice) {
        if (listPrice < 0) {
            return "List price cannot be negative.";
        }
        if (salePrice < 0 || salePrice > 100) {
            return "Sale price is between 0% and 100%";
        }
        return null; 
    }

}
