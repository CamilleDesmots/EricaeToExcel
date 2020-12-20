/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.importation;

import org.camilledesmots.ericae.autocompletion.importation.OrdonnedList;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description de la balise HTML "fieldset"
 *
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Fieldset {

    @XmlElement(name = "legend")
    private String legend;

    @XmlElement(name = "ol")
    private OrdonnedList ordonnedList;

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public OrdonnedList getOrdonnedList() {
        return ordonnedList;
    }

    public void setOrdonnedList(OrdonnedList ordonnedList) {
        this.ordonnedList = ordonnedList;
    }

}
