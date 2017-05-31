package dustorage;

import client.LocalStorage;
import java.util.ArrayList;

/**
 * Класс альбомов
 * @author seraf
 */
public class Album {
    /**
     * ID альбома
     */
    protected int id = 0;
    
    /**
     * Имя альбома
     */
    protected String name;
    
    /**
     * Размер альбома
     */
    protected int size = 0;
    

    /**
     * Получить id альбома
     * @return 
     */
    public int getId() {
        return id;
    }
    
    /**
     * Установить id альбома
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Получить имя альбома
     * @return 
     */
    public String getName() {
        return name;
    }

    /**
     * Установить имя альбома
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     *
     * @param size
     */
    public void setSize(int size) {
        this.size = size;
    }
    
    
    
}
