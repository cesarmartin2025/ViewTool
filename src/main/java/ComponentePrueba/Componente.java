/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePrueba;

import java.awt.BorderLayout;
import java.io.File;
import java.io.Serializable;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

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
    
    private JLabel jLabel;
    
    private Timer timer;

    public Componente() {
        setLayout(new BorderLayout());
        jLabel = new JLabel("Server Media");
        add(jLabel,BorderLayout.CENTER);
        
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        if(apiUrl==null || apiUrl.isBlank()){
            throw new IllegalArgumentException("apiUrl cannot be null");
        }
        this.apiUrl = apiUrl;
        apiClient = new ApiClient(apiUrl);
        
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
        if(running){
            timer.start();
        }else{
            timer.stop();
        }
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
    
    public void checkListMedia(){}

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
