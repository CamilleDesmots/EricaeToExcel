/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "provenance")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Provenance {

    private String libelle;
    private short affichage;

    // Getter Methods 
    @XmlAttribute(name = "libelle")
    public String getLibelle() {
        if (this.libelle == null ) this.libelle = "";
        return libelle;
    }

    @XmlAttribute(name = "affichage")
    public short getAffichage() {
        return affichage;
    }

    // Setter Methods 
    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setAffichage(short affichage) {
        this.affichage = affichage;
    }

    @Override
    public String toString() {
        return " provenance : {"
                + " libelle: \"" + libelle + "\""
                + ", affichage: " + affichage + "}";

    }
}
