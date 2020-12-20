/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.importation;

import org.camilledesmots.ericae.autocompletion.importation.ElementList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description de la balise "\<ol\>"
 *
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class OrdonnedList {

    @XmlAttribute(name = "class")
    private String classe;

    @XmlElement(name = "li")
    private List<ElementList> listElementList;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public List<ElementList> getListElementList() {
        return listElementList;
    }

    public void setListElementList(List<ElementList> listElementList) {
        this.listElementList = listElementList;
    }

}
