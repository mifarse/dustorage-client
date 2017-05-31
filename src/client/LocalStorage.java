/*
 * The MIT License
 *
 * Copyright 2017 seraf.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package client;

import dustorage.Album;
import dustorage.Photo;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;

/**
 * Драйвер для работы с локальным хранилищем - базой данных sqlite.
 * @author seraf
 */
public class LocalStorage {
    
    static Connection c = null;

    /**
     * Инициация базы данных. Создается подключение и необходимые таблицы.
     */
    public static void init() {
    
        Statement stmt = null;
        
        try {
            connect();
            stmt = c.createStatement();
            String sql1 = "CREATE TABLE IF NOT EXISTS user"
                    + "(username varchar(50)    NOT NULL,"
                    + " password varchar(50)    NOT NULL,"
                    + " host     varchar(50)    NOT NULL,"
                    + " method   int(1)   NOT NULL,"
                    + " email    varchar(50),"
                    + " last_sync varchar(20),"
                    + " api_token varchar(100))";
            String sql2 = "CREATE TABLE IF NOT EXISTS photos"
                    + "(md5_hash    varchar(32)    NOT NULL,"
                    + " original    varchar(100)   NOT NULL,"
                    + " thumbnail   varchar(100)   NOT NULL,"
                    + " size        int            NOT NULL,"
                    + " timestamp   datetime       NOT NULL,"
                    + " synced      int            DEFAULT 0,"
                    + " UNIQUE (md5_hash) ON CONFLICT REPLACE)";
            String sql3 = "CREATE TABLE IF NOT EXISTS albums"
                    + "(id      integer primary key autoincrement,"
                    + " name    varchar(50) NOT NULL"
                    + ")";
            String sql4 = "CREATE TABLE IF NOT EXISTS album_photo"
                    + "(album_id    integer     NOT NULL,"
                    + " photo_md5   varchar(32) NOT NULL"
                    + ")";
            
            stmt.executeUpdate(sql1);
            stmt.executeUpdate(sql2);
            stmt.executeUpdate(sql3);
            stmt.executeUpdate(sql4);
            stmt.close();
            System.out.println("OK");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }    
    }
    
    /**
     * Получает запись о пользователе в карту.
     * @return структура с полями объекта User.
     */
    public static HashMap getUser(){
        try {
            
            if (c.isClosed()) connect(); 

            Statement stmt = c.createStatement();
            
            HashMap user = new HashMap();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM user;");
            
            while (rs.next()) {
                user.put("username", rs.getString("username"));
                user.put("password", rs.getString("password"));
                user.put("host", rs.getString("host"));
                user.put("method", rs.getInt("method"));
                user.put("email", rs.getString("email"));
                user.put("last_sync", rs.getString("last_sync"));
                user.put("api_token", rs.getString("api_token"));
            }
            System.out.println(user);
            rs.close();
            stmt.close();
            c.close();
            
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private static void connect(){
        try {
            
            c = DriverManager.getConnection("jdbc:sqlite:"
                    + System.getProperty("user.home")
                    + "/.dustorage/dustorage.db");
//            c.setAutoCommit(false);
        } catch (SQLException ex) {
            Logger.getLogger(LocalStorage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Устанавливает данные пользователя в базе, поля объекта User являются атрибутами таблицы user.
     * @param username
     * @param password
     * @param host
     * @param method
     * @param email
     * @param last_sync
     * @param api_token
     */
    public static void setUser(String username, String password, String host, int method, String email, String last_sync, String api_token) {
        
        try {
                        
            if (c.isClosed()) connect(); 
            
            Statement stmt = c.createStatement();

            stmt = c.createStatement();
            
            String sql = "DELETE FROM user;";
            stmt.execute(sql);
            
            sql = "INSERT INTO user "
                       + "VALUES ('"
                            + username + "', '"
                            + password + "', '"
                            + host + "', '"
                            + method + "', '"
                            + email + "', '"
                            + last_sync + "', '"
                            + api_token
                       + "');";
            System.out.println(sql);
            stmt.executeUpdate(sql);

            stmt.close();
            c.close();
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        
    }
    
    /**
     * Добавляет фотографию в базу данных.
     * @param md5_hash - контрольная сумма оригинала
     * @param path_orig - путь до оригинала
     * @param path_thumb - путь до миниатюры
     * @param size - размер фотографии
     * @param synced - статус синхронизации 1 или 0
     */
    public static void addPhoto(String md5_hash, String path_orig, String path_thumb, int size, int synced){
        
        String timestamp;
        timestamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date());
        
        try {
            
            if (c.isClosed()) connect(); 
           
            
            String sql = "INSERT INTO photos "
                       + "VALUES ('"+md5_hash+"', '"+path_orig+"', '"+path_thumb+"', "+size+", '"+timestamp+"', "+synced+");";
            
            Statement stmt = c.createStatement();            
                        
            stmt.executeUpdate(sql);
            stmt.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    /**
     * Булевая функция показывает, существует ли фотография в базе данных
     * @param md5_hash - контрольная сумма оригинала фото
     * @return true - фото есть в базе.
     */
    public static boolean isPhotoThere(String md5_hash){
        
        boolean result = false;
        
        try {
            
            if (c.isClosed()) connect(); 
            
            String sql = "SELECT count(*) FROM photos WHERE md5_hash = '"+md5_hash+"';";
            
            Statement stmt = c.createStatement();            
                        
            ResultSet o = stmt.executeQuery(sql);
            result = o.getInt(1) == 0 ? false : true;
            stmt.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return result; 
    }

    /**
     * Возвращает список фотографий (Объект Photo) в порядке от новых к старым.
     * @return
     */
    public static ArrayList<Photo> getPhotos() {
        try {
            
            if (c.isClosed()) connect(); 

            Statement stmt = c.createStatement();
            
            ArrayList<Photo> photos = new ArrayList<Photo>();
            
            ResultSet rs = stmt.executeQuery("SELECT * FROM photos ORDER BY timestamp desc;");
            
            while (rs.next()) {
                Photo photo = new Photo();
                photo.setOriginal(rs.getString("original"));
                photo.setThumbnail(rs.getString("thumbnail"));
                photo.setTimestamp(rs.getString("timestamp"));
                photo.setSyncStatus(rs.getInt("synced"));
                photo.setSize(rs.getInt("size"));
                photo.setFile(rs.getString("original"));
                photo.setMd5(rs.getString("md5_hash"));
                photos.add(photo);
            }
            rs.close();
            stmt.close();
            c.close();
            
            return photos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Перегруженная функция, которая возвращает список фото из альбома
     * @param album_id - Id альбома
     * @return список фото.
     */
    public static ArrayList<Photo> getPhotos(int album_id) {
        try {
            
            if (c.isClosed()) connect(); 

            Statement stmt = c.createStatement();
            
            ArrayList<Photo> photos = new ArrayList<Photo>();
            
            ResultSet rs = stmt.executeQuery(
                    "SELECT photos.* FROM photos, album_photo \n" +
                    "WHERE album_photo.photo_md5 = photos.md5_hash\n" +
                    "AND album_photo.album_id = " + album_id + " " +
                    "ORDER BY timestamp desc;");
            
            while (rs.next()) {
                Photo photo = new Photo();
                photo.setOriginal(rs.getString("original"));
                photo.setThumbnail(rs.getString("thumbnail"));
                photo.setTimestamp(rs.getString("timestamp"));
                photo.setSyncStatus(rs.getInt("synced"));
                photo.setSize(rs.getInt("size"));
                photo.setFile(rs.getString("original"));
                photo.setMd5(rs.getString("md5_hash"));
                photos.add(photo);
            }
            rs.close();
            stmt.close();
            c.close();
            
            return photos;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Позволяет получить размер хранилища в байтах
     * @return число байт
     */
    public static int getStorageSize() {
        try {
            
            if (c.isClosed()) connect(); 

            Statement stmt = c.createStatement();
            
            ArrayList<Photo> photos = new ArrayList<Photo>();
            
            ResultSet rs = stmt.executeQuery("SELECT sum(size) as size FROM photos;");
            
            int size = rs.getInt("size");
            
            rs.close();
            stmt.close();
            c.close();
            
            return size;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Обновляет статус синхронизации по контрольной сумме фотографии
     * @param syncStatus - 1 или 0 статус.
     * @param md5 - контрольная сумма фото
     */
    public static void setPhotoSyncStatus(int syncStatus, String md5) {
        try {
            
            if (c.isClosed()) connect(); 
            
            String sql = "UPDATE photos SET synced="+syncStatus+" WHERE md5_hash = '"+md5+"';";
            
            Statement stmt = c.createStatement();            
                        
            stmt.executeUpdate(sql);
            stmt.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * Удаляет фотографию по ее контрольной сумме.
     * @param md5 
     */
    static void deletePhoto(String md5) {        
        try {
            
            if (c.isClosed()) connect(); 
            
            String sql = "DELETE FROM photos WHERE md5_hash = '"+md5+"';"
                    + "DELETE FROM album_photo WHERE photo_md5 = '"+md5+"';";
            
            Statement stmt = c.createStatement();            
                        
            stmt.executeUpdate(sql);
            
            stmt.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        } 
    }

    static int createAlbum(String text) {
        
        int result = 0;
        
        try {
            
            if (c.isClosed()) connect(); 
           
            String sql = "INSERT INTO albums (name)"
                       + "VALUES ('"+text+"');";
            Statement stmt1 = c.createStatement();   
            stmt1.executeUpdate(sql);
            stmt1.close();
            
            sql = "SELECT id FROM albums WHERE name = '"+text+"'";
            Statement stmt2 = c.createStatement();    
            ResultSet rs = stmt2.executeQuery(sql);
            result = rs.getInt(1);
            stmt2.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return result;
    }

    static void linkPhotoToAlbum(String md5, int album_id) {
        try {
            if (c.isClosed()) connect(); 
           
            String sql = "INSERT INTO album_photo "
                       + "VALUES ("+album_id+", '"+md5+"');";
            System.out.println(sql);
            Statement stmt1 = c.createStatement();   
            stmt1.executeUpdate(sql);
            stmt1.close();
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }
    
    static ArrayList<Album> getAlbums(){
        try {
            if (c.isClosed()) connect(); 
           
            String sql = "SELECT id, name, count(*) as 'size' FROM albums\n" +
                "JOIN album_photo on album_photo.album_id = id\n" +
                "GROUP by id;";
            System.out.println(sql);
            Statement stmt1 = c.createStatement();   
            ResultSet rs = stmt1.executeQuery(sql);
            
            ArrayList<Album> albums = new ArrayList<Album>();
            
            while(rs.next()){
                Album album = new Album();
                album.setId(rs.getInt("id"));
                album.setName(rs.getString("name"));
                album.setSize(rs.getInt("size"));
                albums.add(album);
            }
            
            stmt1.close();
            return albums;
            
        } catch (SQLException e){
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
        return null;
    }
    
}
