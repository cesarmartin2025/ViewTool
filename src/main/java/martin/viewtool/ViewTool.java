/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package martin.viewtool;
import java.nio.file.Files;
import java.nio.file.Path;
import martin.viewtool.core.ApiClient;
import martin.viewtool.ui.LoginJFrame;
import martin.viewtool.ui.ViewToolApp;


/**
 *
 * @author cesar
 */
public class ViewTool {
    public static void main(String[] args) {
        
        Path p = Path.of("datos/token_txt.txt");
        try{
            if(Files.exists(p)){
                String token = Files.readString(p).trim();
                
                ApiClient apiClient =  new ApiClient("https://dimedianetapi9.azurewebsites.net/");
                try{
                    
                    apiClient.getMe(token);
                   javax.swing.SwingUtilities.invokeLater(() -> new ViewToolApp(token).setVisible(true));
                   return;
                   
                }catch(Exception ex){
                    Files.delete(p);
                    
                }
            }
        }catch(Exception ex){
            System.out.println(ex.getMessage());
        }
        
         javax.swing.SwingUtilities.invokeLater(() -> new LoginJFrame().setVisible(true));
    }
    
    
}
