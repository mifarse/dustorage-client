package dustorage;

/**
 *
 * @author seraf
 */
public class Like {
    
    /**
     * ID фотки, которой поставлен лайк
     */
    protected int photo_id;
    
    /**
     * ID пользователя, который поставил лайк
     */
    protected int user_id;
    
    public Like() {}

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
}
