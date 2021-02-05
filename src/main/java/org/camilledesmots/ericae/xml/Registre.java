/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import org.camilledesmots.ericae.xml.Adresse;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "registre")
@XmlAccessorType(XmlAccessType.FIELD)
public class Registre {
    //@XmlElement(name="entrees")
    private Entrees entrees;
    @XmlElement(name="pieces_jointes")
    private String pieces_jointes;
    @XmlElement(name="adresses")
    List<Adresse> Adresses;
    @XmlAttribute(name = "nom")
    private String _nom;
    @XmlAttribute(name = "nom_responsable")
    private String _nom_responsable;
    @XmlAttribute(name = "essai")
    private short _essai;
    @XmlAttribute(name = "date_ouverture")
    private long _date_ouverture;
    @XmlAttribute(name = "date_fermeture")
    private long _date_fermeture;
    @XmlAttribute(name = "date_derniere_lecture")
    private long _date_derniere_lecture;
    @XmlAttribute(name = "id_type")
    private short _id_type;

    // Getter Methods 
    public Entrees getEntrees() {
        return this.entrees;
    }

    public String getPieces_jointes() {
        return pieces_jointes;
    }

    public List<Adresse> getAdresses() {
        return Adresses;
    }

    public String get_nom() {
        return _nom;
    }

    public String get_nom_responsable() {
        return _nom_responsable;
    }

    public short get_essai() {
        return _essai;
    }

    public long get_date_ouverture() {
        return _date_ouverture;
    }

    public long get_date_fermeture() {
        return _date_fermeture;
    }

    public long get_date_derniere_lecture() {
        return _date_derniere_lecture;
    }

    public short get_id_type() {
        return _id_type;
    }

    // Setter Methods 
    public void setEntrees(Entrees entrees) {
        this.entrees = entrees;
    }

    public void setPieces_jointes(String pieces_jointes) {
        this.pieces_jointes = pieces_jointes;
    }

    public void setAdresses(List<Adresse> adresses) {
        this.Adresses = adresses;
    }

    public void set_nom(String _nom) {
        this._nom = _nom;
    }

    public void set_nom_responsable(String _nom_responsable) {
        this._nom_responsable = _nom_responsable;
    }

    public void set_essai(short _essai) {
        this._essai = _essai;
    }

    public void set_date_ouverture(long _date_ouverture) {
        this._date_ouverture = _date_ouverture;
    }

    public void set_date_fermeture(long _date_fermeture) {
        this._date_fermeture = _date_fermeture;
    }

    public void set_date_derniere_lecture(long _date_derniere_lecture) {
        this._date_derniere_lecture = _date_derniere_lecture;
    }

    public void set_id_type(short _id_type) {
        this._id_type = _id_type;
    }
}
