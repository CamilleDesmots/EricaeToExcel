/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.importation;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author root
 */

@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class Root {
    
    @XmlElement(name = "fieldset")
    List<Fieldset> listFieldset;

    public List<Fieldset> getListFieldset() {
        return listFieldset;
    }

    public void setListFieldset(List<Fieldset> listFieldset) {
        this.listFieldset = listFieldset;
    }
    
    
    
}
