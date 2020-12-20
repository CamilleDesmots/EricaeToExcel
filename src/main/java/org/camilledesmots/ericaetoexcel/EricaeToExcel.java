/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericaetoexcel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.camilledesmots.ericae.autocompletion.Autocompletion;
import org.camilledesmots.ericae.xml.Entree;
import org.camilledesmots.ericae.xml.Entrees;
import org.camilledesmots.ericae.xml.Registre;

/**
 *
 * @author root
 */
public class EricaeToExcel {

    private static final Logger LOG = Logger.getLogger(EricaeToExcel.class.getName());

    public EricaeToExcel() {

    }

    public static void main(String args[]) {

        // Répertoire à analyser ou sont les fichiers .er2
        String originFolder = System.getProperty("originFolder", "/Users/camilledesmots/Documents/Associations/Volée de Piafs/Logiciel ERICAE/Export ERICAE");

        // Répertoire de destination ou va être créé le fichier EXCEL
        String resultFolder = System.getProperty("resultFolder", "target/");
        
        XmlToObject xmlToObject = new XmlToObject();
        xmlToObject.readDirectory(originFolder);
        
        // Liste des registres sous la forme d'objet
        List <Registre> listRegistre = xmlToObject.getRegistres();
        
        LOG.info("Création des statistiques et de la feuille EXCEL");
        ObjectToExcel objectToExcel;
        objectToExcel = new ObjectToExcel(listRegistre, resultFolder);
        // Création du fichier EXCEL
        objectToExcel.generateFile();

    }

}
