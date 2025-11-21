/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package martin.viewtool;

import martin.viewtool.ui.TestFrameLogin;


/**
 *
 * @author cesar
 */
public class ViewTool {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new TestFrameLogin().setVisible(true));
         //javax.swing.SwingUtilities.invokeLater(() -> new ViewToolApp().setVisible(true));
    }
}
