package leo.multithreadedfilecopier.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.FileCopier;

/**
 * This class reads values from config.properties file and stores them in FileCopierProperties
 * object.
 * 
 * @author Leo Kallonen
 */
public class PropertyLoader {
    public static FileCopierProperties readConfig() { 
        FileCopierProperties fcProps = new FileCopierProperties();
        
        try (InputStream is = FileCopier.class.getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            prop.load(is);
            fcProps.getBufferSize(Integer.parseInt(prop.getProperty("bufferSize")));
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