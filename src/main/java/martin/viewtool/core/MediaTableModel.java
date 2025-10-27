/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.time.Instant;
import java.util.List;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author cesar
 */
public final class MediaTableModel extends AbstractTableModel {
    private final List<MediaItem> files;
    private final String[] columns = {"Name", "Size (MB)", "Type", "Date"};

    public MediaTableModel(List<MediaItem> files) {
        this.files = files;
    }

    @Override public int getRowCount() { 
        return files.size(); }
    
    @Override public int getColumnCount() { 
        return columns.length; }
    
    @Override public String getColumnName(int column) { 
        return columns[column]; }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 -> String.class;
            case 1 -> Double.class;     
            case 2 -> String.class;
            case 3 -> Instant.class;     
            default -> Object.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        MediaItem file = files.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> file.getName();
            case 1 -> file.getSizeBytes() / (1024.0 * 1024.0);
            case 2 -> file.getMimeType();
            case 3 -> java.time.format.DateTimeFormatter
             .ofPattern("dd/MM/yyyy HH:mm")
             .withZone(java.time.ZoneId.systemDefault())
             .format(file.getDate());
            default -> "";
        };
    }

    public MediaItem getFile(int row) {
        return files.get(row); }
}
