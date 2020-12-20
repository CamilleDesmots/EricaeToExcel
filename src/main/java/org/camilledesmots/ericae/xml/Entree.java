/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import java.util.List;
import java.util.logging.Logger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "entree")
@XmlAccessorType(XmlAccessType.FIELD)
public class Entree implements Comparable< Entree> {

    @XmlAttribute(name = "Race")
    private String Race;
    @XmlAttribute(name = "date_entree")
    private Long dateEntree;
    @XmlAttribute(name = "id_sexe")
    private short _id_sexe;
    @XmlAttribute(name = "numero_ident")
    private String _numero_ident;
    @XmlAttribute(name = "nombre")
    private int _nombre;
    @XmlElement(name = "nature")
    private Nature NatureObject;
    @XmlElement(name = "provenance")
    private Provenance ProvenanceObject;
    @XmlElement(name = "origine")
    private Origine OrigineObject;
    @XmlElement(name = "espece")
    private Espece EspeceObject;
    @XmlElement(name = "pieces_jointes")
    private String piecesJointes;
    @XmlElement(name = "sorties")
    private Sorties SortiesObject;
    @XmlAttribute(name = "justificatifs")
    private String justificatifs;
    @XmlAttribute(name = "notes")
    private String _notes;
    @XmlAttribute(name = "barre")
    private short _barre;
    @XmlAttribute(name = "date_saisie")
    private Long _date_saisie;
    @XmlAttribute(name = "date_naissance")
    private Long _date_naissance;
    @XmlAttribute(name = "date_naissance_2")
    private String _date_naissance_2;
    @XmlAttribute(name = "NLOF")
    private String _NLOF;

    private static final Logger LOG = Logger.getLogger(Entree.class.getName());

    // Getter Methods
    public String getRace() {
        return this.Race;
    }

    public Long get_date_entree() {
        return this.dateEntree;
    }

    public short get_id_sexe() {
        return _id_sexe;
    }

    public String getSexe() {
        String sexe;
        sexe = switch (this._id_sexe) {
            case 1 ->
                "M";
            case 2 ->
                "F";
            case 3 ->
                "?";
            default ->
                "";
        };
        return sexe;
    }

    public String get_numero_ident() {
        return _numero_ident;
    }

    public int get_nombre() {
        return _nombre;
    }

    public Nature getNature() {
        return NatureObject;
    }

    public Provenance getProvenance() {
        return ProvenanceObject;
    }

    public Origine getOrigine() {
        return OrigineObject;
    }

    public Espece getEspece() {
        return EspeceObject;
    }

    public String getPiecesJointes() {
        return piecesJointes;
    }

    public String getJustificatifs() {
        return justificatifs;
    }

    public String get_notes() {
        return _notes;
    }

    public short get_barre() {
        return _barre;
    }
    
    public String get_barre_libelle(){
        if (this._barre == 0) {
            return "Valide !";
        } else {
            return "Ligne barrée !";
        }
    }

    public Long get_date_saisie() {
        return _date_saisie;
    }

    public Long get_date_naissance() {
        return _date_naissance;
    }

    public String get_date_naissance_2() {
        return _date_naissance_2;
    }

    public String get_NLOF() {
        return _NLOF;
    }

    // Setter Methods 
    public void set_Race(String _Race) {
        this.Race = _Race;
    }

    public void set_date_entree(Long _date_entree) {
        this.dateEntree = _date_entree;
    }

    public void set_id_sexe(short _id_sexe) {
        this._id_sexe = _id_sexe;
    }

    public void set_numero_ident(String _numero_ident) {
        this._numero_ident = _numero_ident;
    }

    public void set_nombre(int _nombre) {
        this._nombre = _nombre;
    }

    public void setNature(Nature natureObject) {
        this.NatureObject = natureObject;
    }

    public void setProvenance(Provenance provenanceObject) {
        this.ProvenanceObject = provenanceObject;
    }

    public void setOrigine(Origine origineObject) {
        this.OrigineObject = origineObject;
    }

    public void setEspece(Espece especeObject) {
        this.EspeceObject = especeObject;
    }

    public void setPiecesJointes(String piecesJointes) {
        this.piecesJointes = piecesJointes;
    }

    public Sorties getSortiesObject() {
        return SortiesObject;
    }

    public void setSortiesObject(Sorties SortiesObject) {
        this.SortiesObject = SortiesObject;
    }

    public void set_justificatifs(String _justificatifs) {
        this.justificatifs = _justificatifs;
    }

    public void set_notes(String _notes) {
        this._notes = _notes;
    }

    public void set_barre(short _barre) {
        this._barre = _barre;
    }

    public void set_date_saisie(Long _date_saisie) {
        this._date_saisie = _date_saisie;
    }

    public void set_date_naissance(Long _date_naissance) {
        this._date_naissance = _date_naissance;
    }

    public void set_date_naissance_2(String _date_naissance_2) {
        this._date_naissance_2 = _date_naissance_2;
    }

    public void set_NLOF(String _NLOF) {
        this._NLOF = _NLOF;
    }

    @Override
    public String toString() {
        return " Entree: {"
                + " Race: \"" + this.Race + "\""
                + ", date_entree: " + this.dateEntree
                + ", id_sexe: " + this._id_sexe
                + " numero_ident: \"" + this._numero_ident + "\""
                + ", nombre: " + this._nombre
                + ", justificatifs: \"" + this.justificatifs + "\""
                + ", notes: \"" + this._notes + "\""
                + ", barre: " + this._barre
                + ", date_saisie: " + this._date_saisie
                + ", date_naissance: " + this._date_naissance
                + ", date_naissance_2:\"" + this._date_naissance_2 + "\""
                + ", NLOFF:\"" + this._NLOF + "\""
                + this.NatureObject
                + this.ProvenanceObject
                + this.OrigineObject
                + this.EspeceObject
                + ", pieces_jointes: \"" + this.piecesJointes + "\""
                + this.SortiesObject
                + "}";

    }

    @Override
    public int compareTo(Entree o) {
        // Comparaison sur le ID
        int compareID = this.get_numero_ident().compareTo(o.get_numero_ident());
        if (compareID != 0) {
            return compareID;
        }

        // Comparaison sur la date d'entrée
        int compareDateEntree = this.get_date_entree().compareTo(o.get_date_entree());
        if (compareDateEntree != 0) {
            return compareDateEntree;
        }

        //TODO : Comparer les autres champs
        return 0;

    }
}
