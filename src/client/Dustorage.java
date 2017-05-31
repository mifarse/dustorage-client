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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dustorage.Photo;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author seraf
 */
public class Dustorage {

    /**
     *
     */
    protected String access_token = "";

    /**
     *
     */
    protected String host = null;
    
    /**
     *
     * @param host
     */
    public void setHost(String host) {
        this.host = host;
    }
    
    /**
     * Отправка на сервер данных для регистрации пользователя
     * @param name
     * @param email
     * @param password
     * @return ответ сервера
     */
    public String regiter(String name, String email, String password){
        try {
            URL register = new URL(host+"/register");
            HttpURLConnection rc = (HttpURLConnection) register.openConnection();
            rc.setRequestMethod("POST");
            rc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            rc.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(rc.getOutputStream());
            wr.writeBytes("name="+name+"&email="+email+"&password="+password);
            wr.flush();
            wr.close();
            
            int responseCode = rc.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + register);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(rc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            JSONObject data = (JSONObject) JSON.parse(response.toString());
            System.out.println(data.toJSONString());
            new MessageDialog((String) data.get("status"));
        } catch (MalformedURLException ex) {
            new MessageDialog("Недоступный хост");
            Logger.getLogger(Dustorage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            new MessageDialog("Возникла неизвестная ошибка. "+ex.getMessage());
            Logger.getLogger(Dustorage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     * Совершение авторизации пользователя по его данным.
     * @param email
     * @param password
     * @return
     */
    public JSONObject auth(String email, String password){
        try {
            URL register = new URL(host+"/login");
            HttpURLConnection rc = (HttpURLConnection) register.openConnection();
            rc.setRequestMethod("POST");
            rc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            
            rc.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(rc.getOutputStream());
            wr.writeBytes("email="+email+"&password="+password);
            wr.flush();
            wr.close();
            
            int responseCode = rc.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + register);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(rc.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
            }
            in.close();

            //print result
            JSONObject data = (JSONObject) JSON.parse(response.toString());
            if (data != null) {
                return data;
            } else {
                new MessageDialog("Ошибка авторизации");
            }
            
        } catch (MalformedURLException ex) {
            new MessageDialog("Хост не доступен.");
        } catch (IOException ex) {
            new MessageDialog("Возникла неизвестная ошибка. "+ex.getMessage());
        }
        return null;
    }
    
    /**
     * Загрузка фотографии на сервер
     * @param photo - фотография
     * @param token - ключ пользователя
     * @return true - успешная загрузка
     */
    public boolean uploadPhoto(Photo photo, String token){
        String response_str = "";
        String url = host+"/photos?api_token="+token;
        System.out.println(url);
        try {
            MultipartUtility multipart = new MultipartUtility(url, "UTF-8");
            multipart.addFilePart("file", photo.getFile());
            List<String> response = multipart.finish();
            
            for (String line : response) {
                response_str += line;
            }
            System.out.println(response_str);
            JSONObject o = (JSONObject) JSON.parse(response_str);
            String status = (String) o.get("status");
            if (status != null){
                if (status.equals("ok")) {
                    return true;
                }
            }
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("IO");
            return false;
        }
        return false;
    }
    
}
