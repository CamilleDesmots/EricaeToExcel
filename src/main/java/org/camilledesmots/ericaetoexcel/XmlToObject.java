/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericaetoexcel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.camilledesmots.ericae.xml.Entree;
import org.camilledesmots.ericae.xml.Entrees;
import org.camilledesmots.ericae.xml.Registre;

/**
 *
 * @author root
 */
public class XmlToObject {

    // Create a Logger 
    private static final Logger LOG = Logger.getLogger(XmlToObject.class.getName());

    private String originFolder;

    private List<Registre> listRegistre;
    
    
    public List<Registre> getRegistres(){
        return this.listRegistre;
    }
    
    public void setRegistre(List<Registre> listRegistre){
        this.listRegistre = listRegistre;
    }
    
    public XmlToObject() {
        this.listRegistre = new ArrayList<Registre>();
    }

    public void readDirectory(String directory) {
        //Creating a File object for directory
        File directoryPath = new File(directory);
        LOG.log(Level.INFO, "Lecture du répertoire \"" + directory + "\"");
        FileFilter textFilefilter;
        textFilefilter = new FileFilter() {
            @Override
            public boolean accept(File file) {

                if (file.getName().endsWith(".er2")) {
                    return file.isFile();
                } else {
                    return Boolean.FALSE;
                }
            }
        };

        //List of all the text files
        File files[] = directoryPath.listFiles(textFilefilter);

        LOG.info("Nombre de fichiers à lire : " + files.length);

        for (File file : files) {
            LOG.info("Lecture du fichier \"" + file.getAbsolutePath() + "\"");
            
            try {
                 FileReader fr;
               fr = new FileReader(file);
               LOG.info("File encoding is : " + fr.getEncoding()); 
                BufferedReader br = new BufferedReader(fr);
            
                JAXBContext jaxbContext = JAXBContext.newInstance(Registre.class);
       
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                
                // jaxbUnmarshaller.setProperty("target", LOG);
                //Registre registre = (Registre) jaxbUnmarshaller.unmarshal(file);
                Registre registre = (Registre) jaxbUnmarshaller.unmarshal(br);

                Entrees entrees =registre.getEntrees();
                List < Entree> listEntree = entrees.getEntree();
                LOG.info("Nombre de \"Entree\" dans ce registre : "+ listEntree.size());
                
                this.listRegistre.add(registre);
            } catch (JAXBException | FileNotFoundException e) {
                LOG.log(Level.SEVERE, null, e);
            }
        }

        LOG.info("Nombre de registres transformés : " + this.listRegistre.size());
    }

}
