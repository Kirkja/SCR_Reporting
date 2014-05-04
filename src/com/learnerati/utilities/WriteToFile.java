/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.learnerati.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ben S. Jones
 */
public class WriteToFile {
    
    
    
    public static void Text(String filename, String content) 
    {
        File file = new File(filename);
        try {
            try (BufferedWriter output = new BufferedWriter(new FileWriter(file))) {
                output.write(content.trim());
                output.flush();
            }
        } catch (IOException ex) {
            Logger.getLogger(WriteToFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
