/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePrueba;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 *
 * @author cesar
 */
public class Componente extends JPanel implements Serializable {

    private String apiUrl;
    private boolean running = false;
    private int pollingInterval = 5; // seconds
    private String token;
    private String lastChecked; // date and time

    private ApiClient apiClient;

    private JLabel jLabel;

    private Timer timer;

    private ArrayList<CustomEventListener> listeners = new ArrayList<>();

    public Componente() {
        setLayout(new BorderLayout());
        jLabel = new JLabel("Server Media");
        jLabel.setIcon(new ImageIcon(getClass().getResource("/images/iconimagen.png")));
        

        add(jLabel, BorderLayout.CENTER);
        

    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        if (apiUrl == null || apiUrl.isBlank()) {
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
        int intervalMs = pollingInterval * 1000;
        if (timer == null) {
            timer = new Timer(intervalMs, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    checkNewMedia();
                }
            });

        } else {
            timer.setDelay(intervalMs);
            timer.setInitialDelay(intervalMs);
        }

        if (running) {
            timer.start();
        } else {
            timer.stop();
        }
    }

    public int getPollingInterval() {
        return pollingInterval;
    }

    public void setPollingInterval(int pollingInterval) {
        if (pollingInterval <= 0) {
            throw new IllegalArgumentException("this number must be >0 ");
        }
        this.pollingInterval = pollingInterval;

        if (timer != null) {
            int intervalMs = pollingInterval * 1000;
            timer.setDelay(intervalMs);
            timer.setInitialDelay(intervalMs);
        }
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

    public void addCustomEventListener(CustomEventListener listener) {
        listeners.add(listener);
    }

    public void removeCustomEventListener(CustomEventListener listener) {
        listeners.remove(listener);
    }

    public void checkNewMedia() {
        //Need to check IllegalArgumentExceptions
        String formIso = Instant.now().toString();
        try {
            if (lastChecked == null || lastChecked.isEmpty()) {
                setLastChecked(formIso);
                return;
            }
            if (token == null || token.isBlank()) {
                return;
            }
            if (apiClient == null) {
                return;
            }
            List<Media> mediaList = apiClient.getMediaAddedSince(lastChecked, token);
            if (mediaList.isEmpty()) {
                setLastChecked(formIso);
            } else {
                setLastChecked(formIso);
                for (CustomEventListener listener : listeners) {
                    CustomEvent customEvent = new CustomEvent(this, mediaList, lastChecked);
                    listener.customEventReceived(customEvent);
                }

            }
        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String login(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be ingresed");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("You must to write a password");
        }
        try {
            token = apiClient.login(email, password);
            return token;
        } catch (Exception e) {
            throw new RuntimeException("Api Error: " + e.getMessage());
        }

    }

    public String getNickName(int id, String jwt) {
        if (id < 0) {
            throw new IllegalArgumentException("Id must > 0");
        }
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("jwt is null");
        }
        try {
            String nickName = apiClient.getNickName(id, jwt);
            return nickName;
        } catch (Exception e) {
            throw new RuntimeException("Api Error: " + e.getMessage());
        }

    }

    public List<Media> getAllMedia(String jwt) {
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("jwt is null");
        }
        try {
            List mediaList = apiClient.getAllMedia(jwt);
            return mediaList;
        } catch (Exception e) {
            throw new RuntimeException("Api error: " + e.getMessage());
        }

    }

    public void download(int id, File destFile, String jwt) {
        if (id < 0) {
            throw new IllegalArgumentException("id must be >0");
        }
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("jwt is null");
        }
        try {
            apiClient.download(id, destFile, jwt);
        } catch (Exception e) {
            throw new RuntimeException("Api Error: " + e.getMessage());
        }

    }

    public String uploadFileMultipart(File file, String downloadFromUrl, String jwt) {
        if (file == null) {
            throw new IllegalArgumentException("File not found");
        }
        if (jwt == null || jwt.isBlank()) {
            throw new IllegalArgumentException("jwt is null");
        }
        try {
            String response = apiClient.uploadFileMultipart(file, downloadFromUrl, jwt);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Api error: " + e.getMessage());
        }

    }

}
