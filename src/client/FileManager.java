package client;

import java.io.File;
import java.util.ArrayList;


/**
 *
 * @author seraf
 */
public class FileManager {
    
    protected static ArrayList<File> photos = new ArrayList<File>();
    protected static String storageDir = "C:/dustorage";
    
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
        new File(storageDir+"/originals").mkdir();
        for (File el : photos){
            String filename = el.getName();
            el.renameTo(new File(storageDir+"/originals/"+filename));
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
}
