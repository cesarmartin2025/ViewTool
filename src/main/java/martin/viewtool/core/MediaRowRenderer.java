/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

/**
 *
 * @author cesar
 */
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class MediaRowRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {

        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Obtenemos el valor de la columna "Location" 
        String location = table.getValueAt(row, 0).toString();

        if (!isSelected) {
            switch (location) {
                case "Both":
                    c.setBackground(new Color(200, 255, 200)); // Verde claro
                    break;
                case "Local":
                    c.setBackground(new Color(200, 230, 255)); // Azul claro
                    break;
                case "Network":
                    c.setBackground(Color.WHITE); // Blanco estándar
                    break;
                default:
                    c.setBackground(table.getBackground());
                    break;
            }
        } else {
            // Color cuando la fila está seleccionada (el gris que ya he configurado en el design)
            c.setBackground(new Color(153, 153, 153));
        }

        return c;
    }
}
