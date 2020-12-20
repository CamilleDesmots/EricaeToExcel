/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "sorties")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sorties {

    @XmlElement(name = "sortie")
    private List<Sortie> listSortie;

    private static final Logger LOG = Logger.getLogger(Sorties.class.getName());

    @XmlElement(name = "sortie")
    public List<Sortie> getSortie() {
        return listSortie;
    }

    public void setSortie(List<Sortie> listSortie) {
        this.listSortie = listSortie;

    }

    @Override
    public String toString() {
        String txt;
        txt = " sorties: {" + this.getSortie() + "}";
        if (this.listSortie != null) {
            for (Sortie s : listSortie) {
                txt = txt + " " + s.toString();
            }
            txt = txt + "}";
        } else {
            txt = " sorties: {null}";
        }
        return txt;
    }
}
