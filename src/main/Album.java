package main;

/**
 * Класс альбомов
 * @author seraf
 */
public class Album {
    /**
     * ID альбома
     */
    protected int id;
    
    /**
     * Имя альбома
     */
    protected String name;

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
    
}
