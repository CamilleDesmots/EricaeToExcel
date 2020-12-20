/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.camilledesmots.ericae.autocompletion.fichier.Listes;

/**
 * Gestion de l'équivalent des listes d'autocomplétion d'ERICAE
 *
 * @author Camille Desmots
 */
public class Autocompletion {

    private static final Logger LOG = Logger.getLogger(Autocompletion.class.getName());

    private Listes liste;

    private File file;

    private Map<Integer, String> listeAutocompletion;

    /**
     *
     * @param file Fichier XML contenant les listes d'autocomplétion à charger
     */
    public Autocompletion(String file) {
        this.listeAutocompletion = new HashMap<>();
        this.Load(file);
    }

    public void Load(String file) {
        this.file = new File(file);
        Listes listes = new Listes();

        LOG.info("Chargement des listes à partir du fichier \"" + this.file.getAbsolutePath() + "\"");

        try {
            FileReader fr;
            fr = new FileReader(this.file);

            BufferedReader br = new BufferedReader(fr);

            JAXBContext jaxbContext = JAXBContext.newInstance(Listes.class);

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            listes = (Listes) jaxbUnmarshaller.unmarshal(br);

        } catch (JAXBException | FileNotFoundException e) {
            LOG.log(Level.SEVERE, null, e);
        }

        listes.getCategorie().forEach(c -> {
            c.getElement().forEach(e -> {
                this.listeAutocompletion.put(e.getId(), e.getLibelle());
            });
        });

        LOG.info("Nombre d'éléments dans la liste : " + this.listeAutocompletion.size());

    }

    /**
     * Donne le libellé correspondant à un id
     * @param id
     * @return 
     */
    public String getFromId(Integer id) {

        String reponse = "";

        reponse = this.listeAutocompletion.get(id);

        if (reponse == null) {
            reponse = "Pas de libellé correspondant pour l'ID \"" + id + "\".";
            LOG.log(Level.WARNING, reponse);
        }
        return reponse;
    }

}
