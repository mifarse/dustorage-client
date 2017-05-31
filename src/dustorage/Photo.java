package dustorage;

/**
 *
 * @author seraf
 */
import client.LocalStorage;
import java.io.File;

/**
 *
 * @author seraf
 */
public class Photo {
    
    /**
     * ID пользователя
     */
    protected int id;
    
    /**
     * Путь на диске к оригиналу
     */
    protected String original;
    
    /**
     * Путь на диске к миниатюре
     */
    protected String thumbnail;
    
    /**
     * ID альбома, к которому прикреплена фотография
     */
    protected int album_id;
    
    /**
     * Размер фотографии
     */
    protected long size;
    
    /**
     * Время индексирования фотографии
     */
    protected String timestamp;
    
    /**
     * Время индексирования фотографии
     */
    protected int syncStatus;  
    
    /**
     *
     */
    protected File file;
    
    /**
     *
     */
    protected String md5;

    /**
     *
     */
    public Photo() {
        
    }
    
    /**
     *
     * @return
     */
    public int getSyncStatus() {
        return syncStatus;
    }

    /**
     *
     * @param syncStatus
     */
    public void setSyncStatus(int syncStatus) {
        this.syncStatus = syncStatus;
        LocalStorage.setPhotoSyncStatus(syncStatus, md5);
    }
    
    /**
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return
     */
    public String getOriginal() {
        return original;
    }

    /**
     *
     * @param original
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     *
     * @return
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     *
     * @param thumbnail
     */
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    /**
     *
     * @return
     */
    public int getAlbum_id() {
        return album_id;
    }

    /**
     *
     * @param album_id
     */
    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    /**
     *
     * @return
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     *
     * @param timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     *
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(long size) {
        this.size = size;
    }
    
    /**
     *
     * @return
     */
    public File getFile() {
        return file;
    }

    /**
     *
     * @param filepath
     */
    public void setFile(String filepath) {
        this.file = new File(filepath);
    }

    /**
     *
     * @return
     */
    public String getMd5() {
        return md5;
    }

    /**
     *
     * @param md5
     */
    public void setMd5(String md5) {
        this.md5 = md5;
    }
    
    /**
     * Получить расширение изображение
     * @return строка с точкой
     */
    public String getExtension(){
        String extension = "";
        String fn = this.file.getName();
        int i = fn.lastIndexOf('.');
        if (i > 0) {
            extension = fn.substring(i);
        }
        return extension;
    }
    
}