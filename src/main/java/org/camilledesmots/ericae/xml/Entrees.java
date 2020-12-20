/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "entrees")
public class Entrees {

    private List< Entree> listEntree;

    public void Entrees() {
        this.listEntree = new ArrayList<>();
    }

    @XmlElement
    public List<Entree> getEntree() {
        return this.listEntree;
    }

    public void setEntree(List<Entree> listEntree) {
        this.listEntree = listEntree;
    }

    @Override
    public String toString() {
        String txt = " entrees : {";

        for (Entree e : listEntree) {
            txt = txt + e.toString();
        }

        txt = txt + "}";

        return txt;
    }

}
