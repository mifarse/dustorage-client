package client;

import com.alee.laf.progressbar.WebProgressBar;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * Драйвер для работы с файлами в хранилище
 * @author seraf
 */
public class FileManager {
    
    /**
     * Содержит список файлов с фото.
     */
    protected static ArrayList<File> photos = new ArrayList<File>();

    /**
     * Путь до директории с хранилищем
     */
    protected static String storageDir = System.getProperty("user.home")+"/dustorage";

    /**
     * Путь до директории с программными файлами
     */
    protected static String propertiesDir = System.getProperty("user.home")+"/.dustorage";

    
    /**
     * Функция считает размер оригинальных фото
     * @param format степень для данных
     * Например: 20 - вернет в Мегабайтах, 10 - в Килобайтах.
     * @return размер хранимых данных.
     */
    public static double getOriginalsSize(int format){
        double sum = 0;
        File f = new File(storageDir+"/originals");
        for (File el : f.listFiles()){
            sum += el.length();
        }
        return (double) Math.round(sum * 100 / Math.pow(2, format))/100;
    }
    /**
     * Возвращает размер оригинальных фото в мегабайтах.
     * @return Количество мегабайт, занимаемых оригиналами.
     */
    public static double getOriginalsSize(){
        double sum = 0;
        File f = new File(storageDir+"/originals");
        for (File el : f.listFiles()){
            sum += el.length();
        }
        return (double) Math.round(sum * 100 / Math.pow(2, 20))/100;
    }
    
    /**
     * Эта функция пересортировывает файлы, которые закинули в папку, таким
     * образом, что в результатие выходит папка со списком фотографий.
     */
    public static void orderFiles(){
        photos.clear();
        listPath(storageDir);
        for (File el : photos){
            String filename = el.getName();
            File f = new File(storageDir + "/", filename);
            el.renameTo(f);
        }
        
        for (File f : new File(storageDir).listFiles()){
            if (f.isDirectory() && !"originals".equals(f.getName())) {
                System.out.println(f.getName());
                delete(f);
            }
        }
    }
    
    /**
     * Функция удаляет все содержимое указанной папки.
     * @param path - путь к директории
     */
    public static void delete(File path) {
        File[] l = path.listFiles();
        for (File f : l) {
            if (f.isDirectory()) {
                delete(f);
            } else {
                f.delete();
            }
        }
        path.delete();
    }
    
    /**
     * Эта функция рекурсивно собирает все файлы в список, а файлы 
     * каким-то образом разбросаны по папкам.
     * @param path путь для обзора 
     */
    private static void listPath(String path){
        File f = new File(path);
        
        if (f.isDirectory()){
            for (File el : f.listFiles()){
                if (el.isDirectory()){
                    listPath(el.getAbsolutePath());
                } else {
                    photos.add(el);
                }
            }
        }
    }

    /**
     *
     * @return
     */
    public static String getStorageDir() {
        return storageDir;
    }
    
    /**
     * Создает миниатюру и сохраняет в базу данных данные о фото
     * @param original - файл оригинал изображения
     */
    public static void createThumbnail(File original){
        try {
            
            FileInputStream fis = new FileInputStream(original);
            String md5 = DigestUtils.md5Hex(fis);
            fis.close();
            File withoutPrefix = null;
            
            if (!LocalStorage.isPhotoThere(md5)) {
                
                int synced = 0;
                withoutPrefix = new File(storageDir+"/"+original.getName().replace("SERVER_", ""));
                
                if (original.getName().contains("SERVER_")){
                    synced = 1;
                }
                
                System.out.println("New photo: "+original.getName());
                
                BufferedImage img = new BufferedImage(228, 228, BufferedImage.TYPE_INT_RGB);
                BufferedImage orig = ImageIO.read(original);
                int w = orig.getWidth();
                int h = orig.getHeight();
                if (w >= h){
                    orig = orig.getSubimage((w-h)/2, 0, h, h);
                } else {
                    orig = orig.getSubimage(0, (h-w)/2, w, w);
                }
                img.createGraphics().drawImage(
                        orig.getScaledInstance(228, 228, Image.SCALE_SMOOTH), 
                        0, 0, null);

                String path = propertiesDir + "/thumbnails/" + withoutPrefix.getName() + ".jpg";

                File output = new File(path);
                output.getParentFile().mkdirs();
                output.createNewFile();

                ImageIO.write(img, "jpg", output);

                int size = (int) original.length();
                original.renameTo(withoutPrefix);
                LocalStorage.addPhoto(md5, withoutPrefix.getAbsolutePath(), output.getAbsolutePath(), size, synced);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NullPointerException ex){
            System.err.println("passed file: "+original.getName());
        }
    }
    
    /**
     * Многопоточно сохраняет фотографии в базу данных
     * @param photos - файловый массив с фотографиями
     * @param progress - прогресс бар для обновления статуса
     */
    public static void savePhotos(File[] photos, WebProgressBar progress){
        
        if (photos != null) {
            int cores = Runtime.getRuntime().availableProcessors();
            final int chunk = photos.length / cores;
            progress.setMaximum(1000);
            ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(cores);
            for (int i = 0; i < cores; i++) {
                int from = i * chunk;
                int to = i == cores - 1 ? photos.length : (i + 1) * chunk;
                File[] piece = Arrays.copyOfRange(photos, from, to);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (File el : piece) {
                            int percent = (int) Math.round((float)1000/photos.length);
                            FileManager.createThumbnail(el);
                            progress.setString("Индексирование фото, "+progress.getValue()/10+"%");
                            progress.setValue(progress.getValue()+percent);
                        }
                        System.out.println("Thread has finished!");
                    }
                };

                ses.execute(runnable);

            }

            ses.shutdown();
            try {
                ses.awaitTermination(365, TimeUnit.DAYS);
                System.out.println("FINISHED");
            } catch (InterruptedException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    /**
     * Перегрузка функции сохранения фото в бд.
     * @param photos
     */
    public static void savePhotos(File[] photos){
        
        if (photos != null) {
            int cores = Runtime.getRuntime().availableProcessors();
            final int chunk = photos.length / cores;
            ScheduledExecutorService ses = new ScheduledThreadPoolExecutor(cores);
            for (int i = 0; i < cores; i++) {
                int from = i * chunk;
                int to = i == cores - 1 ? photos.length : (i + 1) * chunk;
                File[] piece = Arrays.copyOfRange(photos, from, to);
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        for (File el : piece) {
                            int percent = (int) Math.round((float)1000/photos.length);
                            FileManager.createThumbnail(el);
                        }
                        System.out.println("Thread has finished!");
                    }
                };

                ses.execute(runnable);

            }

            ses.shutdown();
            try {
                ses.awaitTermination(365, TimeUnit.DAYS);
                System.out.println("FINISHED");
            } catch (InterruptedException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}
