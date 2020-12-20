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
@XmlRootElement(name = "espece")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class Espece {

    private String nomScient;
    private String nomVernac;
    private String statut;
    private int idUser;

    // Getter Methods 
    @XmlAttribute(name = "nom_scient")
    public String getNomScient() {
        return nomScient;
    }

    @XmlAttribute(name = "nom_vernac")
    public String getNomVernac() {
        return nomVernac;
    }

    @XmlAttribute(name = "statut")
    public String getStatut() {
        return statut;
    }

    @XmlAttribute(name = "id_user")
    public int getIdUser() {
        return idUser;
    }

    // Setter Methods 
    public void setNomScient(String nomScient) {
        this.nomScient = nomScient;
    }

    public void setNomVernac(String _nom_vernac) {
        this.nomVernac = _nom_vernac;
    }

    public void setStatut(String _statut) {
        this.statut = _statut;
    }

    public void setIdUser(int _id_user) {
        this.idUser = _id_user;
    }

    @Override
    public String toString() {
        return " espece: {"
                + "nom_scient: \"" + this.nomScient + "\""
                + ", nom_vernac: \"" + this.nomVernac + "\""
                + ", statut: \"" + this.statut + "\""
                + ", id_user: " + this.idUser + "}";
    }
}
