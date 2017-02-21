package main;

/**
 *
 * @author seraf
 */
import java.util.Date;

public class Photo {
    
    /**
     * ID пользователя
     */
    protected int id;
    
    /**
     * Имя файла на диске
     */
    protected String filename;
    
    /**
     * ID альбома, к которому прикреплена фотография
     */
    protected int album_id;
    
    /**
     * Размер фотографии
     */
    protected long size;
    
    /**
     * Время создания фотографии
     */
    protected Date created_at;

    public Photo() {}
    
    public int getID() {
        return id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getAlbumID() {
        return album_id;
    }

    public void setAlbumID(int album_id) {
        this.album_id = album_id;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public Date getCreatedAt() {
        return created_at;
    }
}