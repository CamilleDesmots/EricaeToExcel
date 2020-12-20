/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.fichier;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Element implements Comparable<Element> {

    @XmlAttribute(name = "id")
    private Integer id;
    @XmlValue
    private String libelle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Element() {
    }

    public Element(Integer id, String libelle) {
        this.id = id;
        this.libelle = libelle;

    }

    @Override
    public int compareTo(Element o) {
        int element = this.getId().compareTo(o.getId());
        if (element != 0) {
            return element;
        }

        return 0;
    }

}
