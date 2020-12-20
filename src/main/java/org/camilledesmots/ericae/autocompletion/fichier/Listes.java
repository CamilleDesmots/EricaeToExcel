/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.fichier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Noeud racine du fichier XML contenant les listes d'autocomplétion
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "root")
//@XmlAccessorType(XmlAccessType.FIELD)
public class Listes {

    //@XmlElement(name = "categorie")
    private List<Categorie> categorie;
    
    
    public Listes(){
        this.categorie = new ArrayList<>();
    }

    public List<Categorie> getCategorie() {
        return categorie;
    }

    public void setCategorie(List<Categorie> categorie) {
        this.categorie = categorie;
    }
    
    public void sortCategorie(){
        Collections.sort((List) this.categorie);
    }

}
