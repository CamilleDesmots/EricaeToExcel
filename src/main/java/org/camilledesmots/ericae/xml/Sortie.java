/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericae.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Camille Desmots
 */
@XmlRootElement(name = "sortie")
@XmlAccessorType(XmlAccessType.FIELD)
public class Sortie {

    @XmlAttribute(name = "date_sortie")
    private long dateSortie;
    @XmlAttribute(name = "nombre")
    private int nombre;
    @XmlAttribute(name = "id_nature")
    private int idNature;
    @XmlAttribute(name = "id_destination")
    private int idDestination;
    @XmlAttribute(name = "justificatifs_sortie")
    private String justificatifsSortie;
    @XmlAttribute(name = "id_cause_mort")
    private int idCauseMort;
    @XmlAttribute(name = "notes")
    private String notes;
    @XmlAttribute(name = "date_saisie")
    private long dateSaisie;
    @XmlAttribute(name = "barre")
    private Boolean barre;

    @XmlElement(name = "destination")
    private Destination destinationObject;
    @XmlElement(name = "cause_mort")
    private Cause_mort causeMortObject;
    @XmlElement(name = "nature")
    private Nature natureObject;
    
    // Getter Methods 
    public Destination getDestination() {
        return destinationObject;
    }

    public Cause_mort getCauseMort() {
        return causeMortObject;
    }

    public Nature getNature() {
        return natureObject;
    }

    public long getDateSortie() {
        return dateSortie;
    }

    public int getNombre() {
        return nombre;
    }

    public int getIdNature() {
        return idNature;
    }

    public int getIdDestination() {
        return idDestination;
    }

    public String getJustificatifsSortie() {
        return justificatifsSortie;
    }

    public int getIdCauseMort() {
        return idCauseMort;
    }

    public String getNotes() {
        return notes;
    }

    public long getDateSaisie() {
        return dateSaisie;
    }

    public Boolean getBarre() {
        return barre;
    }

    // Setter Methods 
    public void setDestination(Destination destinationObject) {
        this.destinationObject = destinationObject;
    }

    public void setCauseMort(Cause_mort cause_mortObject) {
        this.causeMortObject = cause_mortObject;
    }

    public void setNature(Nature natureObject) {
        this.natureObject = natureObject;
    }

    public void setDateSortie(long _date_sortie) {
        this.dateSortie = _date_sortie;
    }

    public void setNombre(int _nombre) {
        this.nombre = _nombre;
    }

    public void setIdNature(int _id_nature) {
        this.idNature = _id_nature;
    }

    public void setIdDestination(int idDestination) {
        this.idDestination = idDestination;
    }

    public void setJustificatifsSortie(String justificatifsSortie) {
        this.justificatifsSortie = justificatifsSortie;
    }

    public void setIdCauseMort(int idCauseMort) {
        this.idCauseMort = idCauseMort;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setDateSaisie(long dateSaisie) {
        this.dateSaisie = dateSaisie;
    }

    public void setBarre(Boolean barre) {
        this.barre = barre;
    }

    public Destination getDestinationObject() {
        return destinationObject;
    }

    public void setDestinationObject(Destination destinationObject) {
        this.destinationObject = destinationObject;
    }

    public Cause_mort getCauseMortObject() {
        return causeMortObject;
    }

    public void setCauseMortObject(Cause_mort causeMortObject) {
        this.causeMortObject = causeMortObject;
    }

    public Nature getNatureObject() {
        return natureObject;
    }

    public void setNatureObject(Nature natureObject) {
        this.natureObject = natureObject;
    }

}
