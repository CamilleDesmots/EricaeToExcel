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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Categorie implements Comparable<Categorie> {

    @XmlAttribute(name = "libelle")
    private String libelle;

    @XmlElement(name = "element")
    private List<Element> element;

    public Categorie() {
        this.element = new ArrayList<>();
    }

    public String getLibelle() {
        return this.libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public List<Element> getElement() {
        return element;
    }

    public void setElement(List<Element> element) {
        this.element = element;
    }

    public Categorie(String libelle) {
        this.libelle = libelle;
    }

    public void sortElement() {
        Collections.sort((List) this.element);
    }

    @Override
    public int compareTo(Categorie o) {
        int categorie = this.getLibelle().compareTo(o.getLibelle());
        if (categorie != 0) {
            return categorie;
        }

        return 0;

    }
}
