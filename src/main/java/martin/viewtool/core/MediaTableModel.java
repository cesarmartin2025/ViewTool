/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.time.Instant;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import MediaSyncPolling.Media;

/**
 *
 * @author cesar
 */
public final class MediaTableModel extends AbstractTableModel {
    private final List<Media> files;
    private final String[] columns = {"NAME", "User ID", "MimeType", "URL"};

    public MediaTableModel(List<Media> files) {
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
            case 1 -> int.class;     
            case 2 -> String.class;
            case 3 -> String.class;     
            default -> Object.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Media file = files.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> file.mediaFileName;
            case 1 -> file.userId;
            case 2 -> file.mediaMimeType;
            case 3 -> file.downloadedFromUrl;
            default -> "";
        };
    }

    public Media getFile(int row) {
        return files.get(row); }
}
