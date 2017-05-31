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

import dustorage.Photo;
import dustorage.User;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Тред для синхронизации. При вызове синхронизации создается отдельный поток,
 * для того чтобы не остановить интерфейс. В этом потоке сперва загружаются файлы
 * а после выкачиваются. В итоге программа устанавливает такое состояние, при
 * котором и на сервере и на клиенте имеются те же файлы.
 * @author seraf
 */
public class SyncThread extends Thread {

    Ftp ftp;
    SyncFrame sf;
    
    SyncThread(Ftp ftp, SyncFrame sf) {
        
        this.ftp = ftp;
        this.sf = sf;
        
    }
    
    @Override
    public void run(){
        User u = new User();
        
        sf.setProgress(-1);
        sf.setStatus("Индексирование фото...");
        
        File path = new File(FileManager.getStorageDir());
        FileManager.orderFiles();
        File[] f = path.listFiles();

        FileManager.savePhotos(f);
        Overview.getPr().update();
                
        if (u.getMethod() == 1){
            Dustorage du = new Dustorage();
            du.setHost(u.getHost());
            ArrayList<Photo> photos = LocalStorage.getPhotos();
            int n = photos.size();
            int step = 1000 / n;
            int i = 1;
            int success = 0;
            for (Photo photo : photos) {
                sf.setStatus("<html>Загрузка фотографии (" + i + " / " + n + ")<br>" + photo.getFile().getName() + "<br>Успешных: " + success + "</html>");

                if (photo.getSyncStatus() == 0) {
                    if (du.uploadPhoto(photo, u.getApi_token()) == true) {
                        photo.setSyncStatus(1);
                        success += 1;
                    }
                } else {
                    success += 1;
                }

                sf.setProgress(sf.getProgress() + step);
                i++;
            }
            
            sf.setProgress(-1);
            sf.setStatus("<html>Загрузка отсутсвующих фото c сервера</html>");
            
            try {
                sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(SyncThread.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            sf.setProgress(-1);
            sf.setStatus("Подключение к серверу...");
            System.out.println(u.getHost());
            System.out.println(u.getUsername());
            ftp.connect(u.getHost(), u.getUsername(), u.getPassword());
            sf.setStatus("Подключено.");

            ArrayList<Photo> photos = LocalStorage.getPhotos();
            int n = photos.size();
            
            if (n > 0) {
                int step = 1000 / n;
                int i = 1;
                int success = 0;
                for (Photo photo : photos) {
                    sf.setStatus("<html>Загрузка фотографии (" + i + " / " + n + ")<br>" + photo.getFile().getName() + "<br>Успешных: " + success + "</html>");

                    if (photo.getSyncStatus() == 0) {
                        if (ftp.uploadPhoto(photo) == true) {
                            photo.setSyncStatus(1);
                            success += 1;
                        }
                    } else {
                        success += 1;
                    }

                    sf.setProgress(sf.getProgress() + step);
                    i++;
                }
            }
            
            sf.setProgress(-1);
            sf.setStatus("<html>Загрузка отсутсвующих фото c FTP-сервера</html>");
            String[] ftpPhotos = ftp.getFtpPhotos();
            int downloadCounter = 0;
            for (String s : ftpPhotos){
                String md5 = "";
                int pos = s.lastIndexOf(".");
                if (pos > 0) {
                    md5 = s.substring(0, pos);
                }
                System.out.println(md5);
                if (LocalStorage.isPhotoThere(md5) == false){
                    try {
                        File newPhoto = new File(FileManager.getStorageDir(), "SERVER_"+s);
                        OutputStream os = new BufferedOutputStream(new FileOutputStream(newPhoto));
                        boolean saved = ftp.retrieveFile("/dustorage/" + s, os);
                        if (saved) {
                            downloadCounter += 1;
                        }
                        os.close();
                        sf.setStatus("<html>Загрузка отсутсвующих фото c FTP-сервера<br>"+downloadCounter+" скачано</html>");
                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } 
                    
                }
            }
        }
        
        sf.setStatus("Синхронизация выполнена.");
        sf.setProgress(1000);
        u.setLast_sync();
        u.commit();
        Menu.updateFields();
        Overview.startIndexing();
        
    }
}
