/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier.properties;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreaded.file.copier.FileCopier;

/**
 *
 * @author Omistaja
 */
public class PropertyLoader {
    public static FileCopierProperties readConfig() { 
        FileCopierProperties fcProps = new FileCopierProperties();
        
        try (InputStream is = FileCopier.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            
            fcProps.setQueueReadTimeoutInSeconds(Integer.parseInt(prop.getProperty("queueReadTimeoutInSeconds")));
            fcProps.setQueueWriteTimeoutInSeconds(Integer.parseInt(prop.getProperty("queueWriteTimeoutInSeconds")));
            fcProps.setQueueSize(Integer.parseInt(prop.getProperty("queueSize")));
        } catch (IOException ex) { 
            Logger.getLogger(PropertyLoader.class.getName()).log(Level.SEVERE, "Config file not found!", ex);
        } catch (NumberFormatException ex2) {
            Logger.getLogger(PropertyLoader.class.getName()).log(Level.WARNING, "Config property not a number!", ex2);
        } catch (NullPointerException ex3) {
            Logger.getLogger(PropertyLoader.class.getName()).log(Level.WARNING, "Unable to find properties!", ex3);
        }
         
        return fcProps;
    }
}