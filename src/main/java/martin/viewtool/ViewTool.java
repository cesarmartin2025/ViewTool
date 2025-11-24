/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package martin.viewtool;
import martin.viewtool.core.ApiClient;
import martin.viewtool.ui.ViewToolApp;
import martin.viewtool.core.TokenService;
import martin.viewtool.ui.LoginJFrame;


/**
 *
 * @author cesar
 */
public class ViewTool {
    public static void main(String[] args) {
                TokenService tokenService = new TokenService();
                String token = tokenService.getToken();
                ApiClient apiClient =  new ApiClient("https://dimedianetapi9.azurewebsites.net/");
                try{
                    apiClient.getMe(token);
                   javax.swing.SwingUtilities.invokeLater(() -> new ViewToolApp(token).setVisible(true));
                   return;
                   
                }catch(Exception ex){
                    tokenService.deleteToken();
                    
                }

         javax.swing.SwingUtilities.invokeLater(() -> new LoginJFrame().setVisible(true));
    }
    
    
}
