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
            throw new IllegalArgumentException(field + " no puede estar vacío");
    }

    public static URI requireValidUrl(String raw) {
        requireHasText(raw, "URL");
        try {
            URI uri = URI.create(raw.trim());
            if (uri.getScheme() == null || uri.getHost() == null)
                throw new IllegalArgumentException("URL inválida");
            return uri;
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("URL inválida: " + ex.getMessage(), ex);
        }
    }
}