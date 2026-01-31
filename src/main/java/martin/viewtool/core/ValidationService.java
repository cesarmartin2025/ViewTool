/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;

/**
 *
 * @author cesar
 */
public final class ValidationService {
    private ValidationService(){}

    public static void requireHasText(String s, String field) {
        if (s == null || s.isBlank())
            throw new IllegalArgumentException(field + " Can't be empty");
    }

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
    
    public static String requireValidLimit(String userInput) {
    if (userInput == null) return null;
    String s = userInput.trim();
    if (s.isEmpty() || s.equals("0")) return null;

    if (s.matches("\\d+")) return s + "M";
    
    throw new IllegalArgumentException("invalid speed. Please, write only digits number example: 1 , 20 , 30 .");
}
}