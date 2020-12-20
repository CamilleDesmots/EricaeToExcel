/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.importation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description de la balise "<li>"
 *
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class ElementList {

    @XmlElement(name = "label")
    private String label;

    @XmlElement(name = "input")
    private Input input;
    
       @XmlElement(name = "strong")
    private String strong;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Input getInput() {
        return input;
    }

    public void setInput(Input input) {
        this.input = input;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
    }
    
    

}
