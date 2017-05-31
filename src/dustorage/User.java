package dustorage;

import client.LocalStorage;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * @author seraf
 */
public class User {
    
    /**
     * Имя пользователя. Если ftp - то это логин для подключения
     */
    protected String username;

    /**
     * Пароль
     */
    protected String password;

    /**
     * имейл
     */
    protected String email;

    /**
     * Метод подключения. 2 - ftp, 1 - dustorage.
     */
    protected int method = 0;

    /**
     * Хост. Место, где будут хранится фотографии
     */
    protected String host;

    /**
     * Дата последней синхронизации
     */
    protected String last_sync;

    /**
     * Токен для dustorage сервера
     */
    protected String api_token;
    
    /**
     *
     */
    public User(){
        HashMap user = LocalStorage.getUser();
        if (user != null){
            this.username = (String) user.get("username");
            this.password = (String) user.get("password");
            this.email = (String) user.getOrDefault("email", "");
            this.method = (int) user.getOrDefault("method", 0);
            this.host = (String) user.get("host");
            this.last_sync = (String) user.getOrDefault("last_sync", "ни разу");
            this.api_token = (String) user.get("api_token");
        }
    }

    /**
     *
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     *
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return
     */
    public int getMethod() {
        return this.method;
    }

    /**
     *
     * @param method
     */
    public void setMethod(int method) {
        this.method = method;
    }

    /**
     *
     * @return
     */
    public String getHost() {
        return host;
    }

    /**
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     *
     * @return
     */
    public String getLast_sync() {
        return last_sync;
    }

    /**
     *
     */
    public void setLast_sync() {
        String timestamp;
        timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        this.last_sync = timestamp;
    }

    /**
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     *
     * @return
     */
    public String getApi_token() {
        return api_token;
    }

    /**
     *
     * @param api_token
     */
    public void setApi_token(String api_token) {
        this.api_token = api_token;
    }

    
    
    /**
     * Сохраняет состояние полей в базу данных.
     */
    public void commit() {
        LocalStorage.setUser(
                this.username,
                this.password,
                this.host,
                this.method,
                this.email,
                this.last_sync,
                this.api_token
        );
    }
    
}
