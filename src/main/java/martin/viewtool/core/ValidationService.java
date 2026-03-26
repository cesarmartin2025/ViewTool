/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;

/**
 * Utility class with validation methods.
 *
 * @author cesar
 */
public final class ValidationService {
    private ValidationService(){}

    /**
     * Throws {@link IllegalArgumentException} if the string is null or blank.
     *
     * @param s     value to check
     * @param field field name used in the error message
     */
    public static void requireHasText(String s, String field) {
        if (s == null || s.isBlank())
            throw new IllegalArgumentException(field + " Can't be empty");
    }

    /**
     * Parses and validates a URL string.
     *
     * @param raw raw URL string to validate
     * @return the parsed {@link URI}
     * @throws IllegalArgumentException if the string is not a valid URL
     */
    public static URI requireValidUrl(String raw) {
        try {
            URI uri = URI.create(raw.trim());
            if (uri.getScheme() == null || uri.getHost() == null)
                throw new IllegalArgumentException("invalid URL");
            return uri;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("invalid URL");
        }
    }
    
    /**
     * Validates a download speed limit value.
     *
     * @param userInput raw input string 
     * @return normalised limit string, or {@code null} if no limit
     * @throws IllegalArgumentException if the input contains non-digit characters
     */
    public static String requireValidLimit(String userInput) {
    if (userInput == null) return null;
    String s = userInput.trim();
    if (s.isEmpty() || s.equals("0")) return null;
    if (s.matches("\\d+")) return s + "M";
    throw new IllegalArgumentException("invalid speed. Please, write only digits number example: 1 , 20 , 30 .");
}
}