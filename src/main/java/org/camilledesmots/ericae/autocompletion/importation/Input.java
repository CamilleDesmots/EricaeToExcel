/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.autocompletion.importation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Description de la balise <input>
 * @author Camille Desmots
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Input {
    
    @XmlAttribute(name = "class")
    private String classe;
    
    @XmlAttribute(name = "id")
    private String id;

    @XmlAttribute(name = "name")
    private String name;

    @XmlAttribute(name = "value")
    private Integer value;

   @XmlAttribute(name = "checked")
    private String checked;    
    
   @XmlElement(name = "strong")
   private String strong;

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getStrong() {
        return strong;
    }

    public void setStrong(String strong) {
        this.strong = strong;
    }
    
    
    
    
}
