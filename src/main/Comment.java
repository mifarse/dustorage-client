package main;

/**
 *
 * @author seraf
 */
import java.util.Date;

public class Comment {
    
    /**
     * ID  комментария
     */
    protected int id;
    
    /**
     * ID фотки, на которую дан данный комментария
     */
    protected int photo_id;
    
    /**
     * ID пользователя, который оставил данный комментарий
     */
    protected int user_id;
    
    /**
     * Текст комментария
     */
    protected String content;
    
    /**
     * Дата создания комментария
     */
    protected Date created_at;
    
    public Comment() {}

    public int getID() {
        return id;
    }

    public int getPhotoID() {
        return photo_id;
    }

    public void setPhotoID(int photo_id) {
        this.photo_id = photo_id;
    }

    public int getUserID() {
        return user_id;
    }

    public void setUserID(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreatedAt() {
        return created_at;
    }
}
