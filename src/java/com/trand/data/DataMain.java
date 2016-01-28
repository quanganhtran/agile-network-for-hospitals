/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.trand.data;

import com.trand.model.Cat;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anh
 */
public class DataMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String subfolder = "java_tmp";
        String filename = "Cat.dat";
        File file = new File("C:" + File.separator + subfolder + File.separator + filename);
        writeCat(file);
    }
    
    public static void writeCat(File file) {
        ObjectOutputStream os = null;
        try {
            os = new ObjectOutputStream(new FileOutputStream(file));
            os.writeObject(new Cat("Wheezy"));
            os.writeObject(new Cat("Durio"));
            os.writeObject(new Cat("Click"));
            os.writeObject(new Cat("Merlin"));
            os.writeObject(new Cat("Grumpie"));
            os.writeObject(new Cat("Pope"));
            os.writeObject(new Cat("Arthur"));
        } catch (IOException ex) {
            Logger.getLogger(DataMain.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                    Logger.getLogger(DataMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
}
