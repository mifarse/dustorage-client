package client;

import dustorage.Photo;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

/**
 * Драйвер для работы с FTP сервером.
 * @author seraf
 */
public class Ftp {

    /**
     * FTPClient - объект из библиотеки apache
     */
    protected FTPClient client;

    /**
     * Адрес сервера
     */
    protected String hostname;

    /**
     * логин
     */
    protected String login;

    /**
     * пароль
     */
    protected String password;

    /**
     * состояние успешного подключения
     */
    protected Boolean successfulConnection = false;
    
    /**
     *
     */
    public Ftp(){
        client = new FTPClient();
        client.setConnectTimeout(5000);
    }
    
    private static void showServerReply(FTPClient ftpClient) {
    String[] replies = ftpClient.getReplyStrings();
    if (replies != null && replies.length > 0) {
        for (String aReply : replies) {
            System.out.println("SERVER: " + aReply);
        }
    }
    }
    
    /**
     * Совершает подключение к серверу
     * @param hostname
     * @param login
     * @param password
     * @return ответ от сервера (ftp код)
     */
    public int connect(String hostname, String login, String password){
        try {
            client.connect(hostname);
            client.login(login, password);
            client.setFileType(FTP.BINARY_FILE_TYPE);
            client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        } catch (SocketException e){
            System.out.println("Socket Exception!");
        } catch (IOException e) {
            System.out.println("IO Exception!");
        } finally {
            this.hostname = hostname;
            this.login = login;
            this.password = password;
            return client.getReplyCode();
        }
    }
    
    /**
     * Тестирует подключение с параметрами. 
     * @param hostname
     * @param login
     * @param password
     * @return Возвращает ответ сервера.
     */
    public int testConnection(String hostname, String login, String password) {
        Boolean t = false; int reply = 0;
        try {
            client.connect(hostname);
            client.login(login, password);
            showServerReply(client);
            System.out.println("sendNoOp: "+t);
        } catch (SocketException e){
            System.out.println("Socket Exception!");
        } catch (IOException e) {
            System.out.println("IO Exception!");
        } finally {
            showServerReply(client);
            reply = client.getReplyCode();
            try {
                client.disconnect();
            } catch (IOException ex) {
                System.out.println("IO Exception");
            }
            return reply;
        }
    }
    
    /**
     * Загружает объект Photo на сервер
     * @param photo
     * @return true - файл сохранен.
     */
    public boolean uploadPhoto(Photo photo){
        
        if (client.isConnected() == false) 
            this.connect(hostname, login, password);
        
        try {
            FileInputStream fis = new FileInputStream(photo.getFile());
            
            if (checkDirectoryExists("/dustorage") == false)
                client.makeDirectory("/dustorage");
                        
            if (client.storeFile("/dustorage/" + photo.getMd5() + photo.getExtension(), fis)) {
                return true;
            } else {
                System.out.println(client.getReplyCode());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    boolean checkDirectoryExists(String dirPath) throws IOException {
        client.changeWorkingDirectory(dirPath);
        int returnCode = client.getReplyCode();
        if (returnCode == 550) {
            return false;
        }
        return true;
    }
    
    boolean checkPhotoExists(Photo photo) throws IOException {
        
        InputStream inputStream = client.retrieveFileStream("/dustorage/" + photo.getMd5() + photo.getExtension());
        int returnCode = client.getReplyCode();
        if (inputStream == null || returnCode == 550) {
            return false;
        }
        return true;
    }
    
    String[] getFtpPhotos(){
        if (client.isConnected() == false) 
            this.connect(hostname, login, password);
        try {
            int j = 0;
            String[] names = client.listNames("/dustorage");
            String[] result = new String[names.length-2];
            for (int i = 0; i < names.length; i++){
                if (!names[i].equals(".") && !names[i].equals("..")){
                    result[j] = names[i];
                    j++;
                }
            }
            return result;
        } catch (IOException ex) {
            Logger.getLogger(Ftp.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    boolean retrieveFile(String remote, OutputStream os) throws IOException {
        return client.retrieveFile(remote, os);
    }
    
}
