/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author cesar
 */
public class TokenService {

    private final Path TOKEN_PATH = Path.of("datos/token_txt.txt");
    private String token;

    public TokenService() {
    }

    public void saveToken(String token) {

        try {
            if (!Files.exists(TOKEN_PATH.getParent())) {
                Files.createDirectories(TOKEN_PATH.getParent());
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        BufferedWriter out;
        try {
            out = new BufferedWriter(new FileWriter(TOKEN_PATH.toFile()));
            out.write(token);
            out.close();

        } catch (IOException ex) {
            System.out.print(ex.getMessage());
        }

    }

    public boolean deleteToken() {
        try {
            if (Files.exists(TOKEN_PATH)) {
                Files.delete(TOKEN_PATH);
                return true;
            } else {
                return false;

            }

        } catch (Exception ex) {
            System.out.print(ex.getMessage());
        }
        return false;
    }

    public String getToken() {
        if (Files.exists(TOKEN_PATH)) {
            try {
                token = Files.readString(TOKEN_PATH).trim();
            } catch (IOException ex) {
                System.out.print(ex.getMessage());
            }
            return token;
        } else {
            return null;
        }
    }
}
    
  
