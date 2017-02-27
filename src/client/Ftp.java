package client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
/**
 *
 * @author seraf
 */
public class Ftp {
    protected FTPClient client;
    protected String hostname;
    protected String login;
    protected String password;
    
    public Ftp(){
        client = new FTPClient();
    }
    
    public void connect(String hostname, String login, String password){
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
        }
    }
    
    public void t() {
        
        this.connect(hostname, login, password);
        
        FileInputStream fis = null;
        File photo = new File("C:\\dustorage\\originals\\214938261.jpg");
        
        try {
            fis = new FileInputStream(photo);
            System.out.println(fis.available());
            //
            // Store file to server
            //
            if (client.storeFile("/home/pi/"+photo.getName(), fis)) {
                System.out.println("Stored!");
            } else {
                System.out.println(client.getReplyCode());
            }
            client.logout();
        } catch (IOException e) {
            System.out.println("IO Exception");
        }
    }
    
}
