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

import com.alee.laf.progressbar.WebProgressBar;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.border.EmptyBorder;
import layout.TableLayout;

/**
 * Небольшое окно с текстом и прогресс баром. Используется для визуального отображения
 * состояния синхронизации.
 * @author seraf
 */
public class SyncFrame extends JFrame {

    JLabel status;
    WebProgressBar processing;
    
    /**
     * Конструктор для окна синхронизации. При вызове формирует окно.
     */
    public SyncFrame() {
        setTitle("Синхронизация");
        setBounds(0, 0, 500, 120);
        setLocationRelativeTo(null);
        
        setLayout(new TableLayout(new double[][] {
            {TableLayout.FILL},
            {TableLayout.PREFERRED, TableLayout.FILL}}));
        
        status = new JLabel("Подключение к серверу");
        status.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        processing = new WebProgressBar();
        processing.setMaximum(1000);
        
        add(status, "0,0");
        add(processing, "0,1");
        
        setVisible(true);
    }
    
    /**
     * С помощью этого метода можно любым процессом обновить текст на фрейме.
     * @param text текст, который ожидается на окне.
     */
    public void setStatus(String text){
        status.setText(text);
    }

    /**
     * Устанавливает прогресс бар окна на определенном значении.
     * @param progress Принимает значения от 0 до 1000; Можно указать -1, тогда
     * прогресс бар станет бесконечным.
     */
    void setProgress(int progress) {
        if (progress == -1){
            processing.setIndeterminate(true);
        } else {
            processing.setIndeterminate(false);
            processing.setValue(progress);
        }
    }
    
    /**
     * Получить текущее состояние прогресс бара
     * @return число от -1 до 1000
     */
    int getProgress() {
        return processing.getValue();
    }
    
}
