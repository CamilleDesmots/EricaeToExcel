/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion;

import org.camilledesmots.ericae.autocompletion.importation.Fieldset;
import org.camilledesmots.ericae.autocompletion.importation.Root;
import org.camilledesmots.ericae.autocompletion.importation.OrdonnedList;
import org.camilledesmots.ericae.autocompletion.importation.Input;
import org.camilledesmots.ericae.autocompletion.importation.ElementList;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.camilledesmots.ericae.autocompletion.fichier.Element;
import org.camilledesmots.ericae.autocompletion.fichier.Listes;
import org.camilledesmots.ericae.autocompletion.fichier.Categorie;
import org.camilledesmots.ericae.xml.Entree;
import org.camilledesmots.ericae.xml.Entrees;
import org.camilledesmots.ericae.xml.Registre;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

/**
 * A partir de la page
 * https://ericae.fr/index.php?module=preferences&page=autocompletion extraction
 * des valeurs de liste
 *
 * @author Camille Desmots
 */
public class AutocompletionExtraction {

    private static final Logger LOG = Logger.getLogger(AutocompletionExtraction.class.getName());

    public static void main(String args[]) {

        // Nom du fichier html de la page autocompletion
        String autocompletionFileOrigin = System.getProperty("autocompletionFileOrigin", "/Users/camilledesmots/Documents/Associations/Volée de Piafs/Logiciel ERICAE/Export ERICAE/Extrait_page_autocompletion.xml");

        // Répertoire de destination ou va être créé le fichier XML
        String resultFolder = System.getProperty("resultFolder", "/Users/camilledesmots/Documents/Associations/Volée de Piafs/Logiciel ERICAE/");

        // Transformation XML en Object 
        File inputHTMLFile = new File(autocompletionFileOrigin);

        LOG.info("Lecture du fichier \"" + inputHTMLFile.getAbsolutePath() + "\"");
        
        TreeMap <Integer, String > listeDeValeur = new TreeMap<>();

        try {
            FileReader fr;
            fr = new FileReader(inputHTMLFile);
            LOG.info("File encoding is : " + fr.getEncoding());
            BufferedReader br = new BufferedReader(fr);

            JAXBContext jaxbContext = JAXBContext.newInstance(Root.class);

            // Permet d'accéder au DTD distantes.
            System.setProperty("javax.xml.accessExternalDTD", "all");

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            Root root = (Root) jaxbUnmarshaller.unmarshal(br);

            List< Fieldset> listFieldset = root.getListFieldset();
            LOG.info("Nombre de \"Fieldset\" dans ce fichier : " + listFieldset.size());
            
            Listes listes = new Listes();
            
            List<Categorie> listCategorie = new ArrayList<>();

            for (Fieldset f : listFieldset) {
                Categorie categorie = new Categorie(f.getLegend());
                
                List<Element> listElement = new ArrayList<>();
                
                
                System.out.println("Legend : " + f.getLegend());
                OrdonnedList ordonnedList = f.getOrdonnedList();
                if (ordonnedList != null) {
                    List<ElementList> listElementList = ordonnedList.getListElementList();

                    for (ElementList li : listElementList) {
                        Input input = li.getInput();
                        System.out.println("Légend : " + f.getLegend() + ", liste : " + li.getStrong() + ", value : " + input.getValue());
                        listeDeValeur.put(input.getValue(), li.getStrong());
                        Element element = new Element(input.getValue(), li.getStrong());
                       listElement.add(element);
                        
                    }
                } else {
                    System.out.println("Pas de liste ordonnée.");
                }
                categorie.setElement(listElement);
                categorie.sortElement();
                listCategorie.add(categorie);
            }
            
            listes.setCategorie(listCategorie);
            listes.sortCategorie();
            
            //TODO : Trier la liste avant 
            //Collections.sort((List) listes);
                  
            
            LOG.info("Nombre d'élément dans la liste : " + listeDeValeur.size());
            // Supression des sauts de lignes
            // Suppression des espaces en trop
            listeDeValeur.entrySet().stream().forEach(e -> {System.out.println("- " + e.getKey() + " > " + e.getValue().replaceAll("\\t|\\n|\\r", "").replaceAll("\\s{2,}  ", " "));});
            
            
            // TODO : Ecrire la liste dans un fichier
  
                File fileDest = new File("target/test.xml");
                JAXB.marshal(listes, fileDest);
            
            

        } catch (JAXBException | FileNotFoundException e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

}
