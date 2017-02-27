package dustorage;

/**
 *
 * @author seraf
 */
import java.util.Date;

public class Log {
    
    /**
     * ID лога
     */
    protected int id;
    
    /**
     * Уровень важности сообщения
     */
    protected LogLevel level;
    
    /**
     * Сообщение лога
     */
    protected String content;
    
    /**
     * Дата создания лога
     */
    protected Date created_at;
    
    public Log() {}

    public int getID() {
        return id;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
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
