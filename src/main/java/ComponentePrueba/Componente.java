/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePrueba;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author cesar
 */
public class Componente extends JPanel implements Serializable {

    private String apiUrl;
    private boolean running;
    private int pollingInterval; // seconds
    private String token;
    private String lastChecked; // date and time

    private ApiClient apiClient;

    public Componente() {
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        this.pollingInterval = pollingInterval;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLastChecked() {
        return lastChecked;
    }

    public void setLastChecked(String lastChecked) {
        this.lastChecked = lastChecked;
    }

    public String login(String email, String password) {
        if(email==null || email.isBlank()){
            throw new IllegalArgumentException("Email must be ingresed");
        }
        if(password==null || password.isBlank()){
            throw new IllegalArgumentException("You must to write a password");  
        }
        try{
            token = apiClient.login(email, password);
            return token;
        }catch(Exception e){
            throw new RuntimeException("Api Error: "+e.getMessage());
        }

    }

    public String getNickName(int id, String jwt){
        if(id<0){
            throw new IllegalArgumentException("Id must > 0");
        }
        if(jwt==null || jwt.isBlank()){
            throw new IllegalArgumentException("jwt is null");
        }
        try{
            String nickName =apiClient.getNickName(id, jwt);
            return nickName;
        }catch(Exception e){
            throw new RuntimeException("Api Error: "+e.getMessage());
        }
        
        
    }

    public List<Media> getMediaByUser(int userId, String jwt){
        if(userId<0){
            throw new IllegalArgumentException("id must be > 0");
        }
        if(jwt==null || jwt.isBlank()){
            throw new IllegalArgumentException("jwt is null");
        }
        try{
            List mediaListUser = apiClient.getMediaByUser(userId, jwt);
            return mediaListUser;
        }catch(Exception e){
            throw new RuntimeException("Api error: "+e.getMessage());
        }
        
    }

    public void download(int id, File destFile, String jwt){
        if(id<0){
            throw new IllegalArgumentException("id must be >0");
        }
        if(jwt==null || jwt.isBlank()){
            throw new IllegalArgumentException("jwt is null");
        }
        try{
            apiClient.download(id, destFile, jwt);
        }catch(Exception e){
            throw new RuntimeException("Api Error: "+e.getMessage());
        }
        

    }
    
    public String uploadFileMultipart(File file, String downloadFromUrl,String jwt){
        if(file==null){
            throw new IllegalArgumentException("File not found");
        }
        try{
           String response = apiClient.uploadFileMultipart(file, downloadFromUrl, jwt);
           return response;
        }catch(Exception e){
            throw new RuntimeException("Api error: "+e.getMessage());
        }
        
        
    }

}
