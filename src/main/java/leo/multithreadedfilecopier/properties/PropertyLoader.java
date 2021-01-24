/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.FileCopier;

/**
 *
 * @author Leo Kallonen
 */
public class PropertyLoader {
    public static FileCopierProperties readConfig() { 
        FileCopierProperties fcProps = new FileCopierProperties();
        
        try (InputStream is = FileCopier.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            
            fcProps.setStackReadTimeoutInSeconds(Integer.parseInt(prop.getProperty("stackReadTimeoutInSeconds")));
            fcProps.setStackWriteTimeoutInSeconds(Integer.parseInt(prop.getProperty("stackWriteTimeoutInSeconds")));
            fcProps.setStackSize(Integer.parseInt(prop.getProperty("stackSize")));
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