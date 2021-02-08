package com.elega9t.tofu;

import com.elega9t.tofu.form.Main;

import javax.swing.JFileChooser;

/**
 *
 * @author elega9t
 */
public class App {

    public static JFileChooser fileChooser;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        App.fileChooser = new JFileChooser();
        Main main = new Main();
        main.setVisible(true);
    }
    
}
