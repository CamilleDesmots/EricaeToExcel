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
@XmlRootElement(name = "cause_mort")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Cause_mort {

    private String libelle;
    private short affichage;

    // Getter Methods 
    @XmlAttribute(name = "libelle")
    public String getLibelle() {
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
}
