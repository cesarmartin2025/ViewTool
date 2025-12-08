/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import MediaSyncPolling.Media;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 *
 * @author cesar
 */
public final class MediaTableModel extends AbstractTableModel {
    NetworkMediaService networkMediaService = new NetworkMediaService();
    private final List<Media> files;
    private final String[] columns = {"Location","Name", "User ID", "URL"};

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
            case 1 -> String.class;     
            case 2 -> int.class;
            case 3 -> String.class;     
            default -> Object.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Media file = files.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> resolveLocation(file);
            case 1 -> file.mediaFileName;
            case 2 -> file.userId;
            case 3 -> file.downloadedFromUrl;
            default -> "";
        };
    }
    
    private String resolveLocation(Media media) {
     
     Path localPath = networkMediaService.getDownloadBaseDir().resolve(media.mediaFileName);

    boolean existsLocal = Files.exists(localPath);
    boolean existsNetwork = (media.id > 0); 

    if (existsLocal && existsNetwork) return "Both";
    if (existsLocal) return "Local";
    if (existsNetwork) return "Network";
    return "None"; 
}
    
    

    public Media getFile(int row) {
        return files.get(row); }
}
