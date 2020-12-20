/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.camilledesmots.ericaetoexcel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.camilledesmots.ericae.xml.Registre;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.CodePageUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camilledesmots.ericae.xml.Entree;
import org.camilledesmots.ericae.xml.Entrees;
import org.camilledesmots.ericae.xml.Sortie;

/**
 * A partir des objets créés en important le XML va parmettre de créer un
 * fichier EXCEL
 *
 * @author Camille Desmots
 */
public class ObjectToExcel {

    private static final Logger LOG = Logger.getLogger(ObjectToExcel.class.getName());

    // Formatage de la date
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");

    // @ Répertoire ou va être généré le fichier EXCEL
    private String resultFolder;

    // Liste des registres
    private List< Registre> registres;

    // Liste de toutes les entrées des registres
    private List<Entree> entrees;

    // Nom du fichier EXCEL
    private String fileName;

    //Plus petite date d'entrée
    private Long minDateEntree;

    //Plus grande date d'entrée
    private Long maxDateEntree;

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * A partir de la liste d'éléments de la classe Registre va créer un fichier
     * EXCEL
     *
     * @param registres Le répertoire ou sont les registres
     * @param resultFolfer Le répertoire ou va être généré la feuille EXCEL
     * @param aucompletion La liste d'autocompletion
     */
    public ObjectToExcel(List<Registre> registres, String resultFolder) {
        this.registres = new ArrayList<Registre>();
        this.registres = registres;
        this.resultFolder = resultFolder;
        this.fileName = "";

        // Création de la liste des éléments de la classe "Entree"
        this.entrees = new ArrayList<Entree>();

        LOG.info("Nombre total de registres : " + registres.size());

        for (Registre registre : registres) {

            //LOG.info("Registre " + registre.get_nom());
            List<Entree> innerListEntree = new ArrayList<Entree>();
            Entrees innerEntrees = new Entrees();
            innerEntrees = registre.getEntrees();

            innerListEntree = innerEntrees.getEntree();

//            for (Entree e : innerListEntree) {
//                LOG.info("N° ident : " + e.get_numero_ident());
//                Sorties sorties = e.getSortiesObject();
//
//                LOG.info("Sorties : " + e.getSortiesObject().toString());
//
//                if (e.getSortiesObject().getSortie() != null) {
//
//                    for (Sortie s : sorties.getSortie()) {
//                        LOG.info("Sortie : " + s.toString());
//                        LOG.info("Sortie.date_sortie:" + s.getDateSortie());
//                        LOG.info("Sortie.date_sortie:" + s.getJustificatifsSortie());
//
//                    }
//                }
//            }
            entrees.addAll(innerListEntree);
        }

        LOG.info("Nombre d'entrée dans tous les registres au total :" + entrees.size());

        // Recherche de la date min et max dans les entrées 
        // Date maximum et minimum
        Comparator<Entree> comparator = Comparator.comparing(Entree::get_date_entree);

        this.minDateEntree = this.entrees.stream().min(comparator).get().get_date_entree();
        this.maxDateEntree = this.entrees.stream().max(comparator).get().get_date_entree();

        Collections.sort(this.entrees);

        //LOG.info("La date de l'entrée la plus vielle est : " + this.getLoclaDateTimeFromSeconds(this.minDateEntree));
        //LOG.info("La date de l'entrée la récente est : " + this.getLoclaDateTimeFromSeconds(this.maxDateEntree));
    }

    private void worksheetDonneesXlsx(Workbook wb) {
        String ongletName = "Données";

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);

        row_0.createCell(columnCpt++).setCellValue("N° d'identification interne à l'établissement");
        
        row_0.createCell(columnCpt++).setCellValue("Nom vernaculaire");
        row_0.createCell(columnCpt++).setCellValue("Nom latin");
        row_0.createCell(columnCpt++).setCellValue("Statut");
        
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Date de saisie");  
        
        // Identification du spécimen
        row_0.createCell(columnCpt++).setCellValue("Sexe");
        row_0.createCell(columnCpt++).setCellValue("Naissance");
        row_0.createCell(columnCpt++).setCellValue("Origine ou lieu de naissance");
        
        // Mouvement d'entrée
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Nature de l'entrée");
        row_0.createCell(columnCpt++).setCellValue("Provenance");
        row_0.createCell(columnCpt++).setCellValue("Justificatifs");
        row_0.createCell(columnCpt++).setCellValue("Barrée");
        row_0.createCell(columnCpt++).setCellValue("Observations");
        
        // Sortie 
        for (int i = 1; i < 4; i++) {
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Date de sortie");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Nombre");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Nature de la sortie");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Destination");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Justificatifs");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Cause de la mort");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Observations");
            row_0.createCell(columnCpt++).setCellValue("Sortie " + i + ": Date Saisie");
        }

        // Transformation de la Map en ExcelExcelRow 
        // Date initialement au format 2019-05-05T19:26:17.394
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS");

        CellStyle style = wb.createCellStyle();
        // Date au format 2019-05-05T19:26:17.394
        style.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy/mm/dd hh:mm:ss.SSS"));

//        for (CallLogItem i : this.callLogItemList) {
//            rowCpt++;
//            Row row_n = sheet.createRow(rowCpt);
//
//            Cell cell_n_0 = row_n.createCell(0);
//            cell_n_0.setCellValue(dtf.format(i.getLocalDateTime()));
//            cell_n_0.setCellStyle(style);
//
//            row_n.createCell(1).setCellValue(i.getNumber().replaceFirst("\\d{2}$", "XX"));
//            row_n.createCell(2).setCellValue(i.getType());
//            row_n.createCell(3).setCellValue(i.getDuration());
//        }
        // TODO Trier les entrées suivant le n° d'identification
        // Boucle sur les entrées 
        for (Entree e : this.entrees) {
            rowCpt++;
            columnCpt = 0;

            try {
                Row row_n = sheet.createRow(rowCpt);
                
                row_n.createCell(columnCpt++).setCellValue(e.get_numero_ident());
                
                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getNomVernac().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getNomScient().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getStatut().getBytes("ISO-8859-1"), "UTF-8"));
                         
                row_n.createCell(columnCpt++).setCellValue(this.getLoclaDateTimeFromSeconds(e.get_date_entree()).toString());
                row_n.createCell(columnCpt++).setCellValue(this.getLoclaDateTimeFromSeconds(e.get_date_saisie()).toString());
                
                
                row_n.createCell(columnCpt++).setCellValue(e.getSexe());
                row_n.createCell(columnCpt++).setCellValue(this.getLoclaDateTimeFromSeconds(e.get_date_naissance()).toString());
                row_n.createCell(columnCpt++).setCellValue(new String(e.get_date_naissance_2().getBytes("ISO-8859-1"), "UTF-8"));        
               
                // Mouvement d'entrée
                row_n.createCell(columnCpt++).setCellValue(e.get_nombre());
                row_n.createCell(columnCpt++).setCellValue(new String(e.getNature().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getProvenance().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getJustificatifs().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.get_barre_libelle().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.get_notes().getBytes("ISO-8859-1"), "UTF-8"));
                
                // Sortie 
                List<Sortie> listSortie = e.getSortiesObject().getSortie();

                if (listSortie != null) {

                    for (Sortie s : listSortie) {
                        row_n.createCell(columnCpt++).setCellValue(this.getLoclaDateTimeFromSeconds(s.getDateSortie()).toString());
                        row_n.createCell(columnCpt++).setCellValue(s.getNombre());
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getNature().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getDestination().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getJustificatifsSortie().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getCauseMort().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getNotes().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(this.getLoclaDateTimeFromSeconds(s.getDateSaisie()).toString());
                    }
                } else {
                    // Pas d'enregistrement de sortie
                }

            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);

            }
        }

    }

    /**
     * Générate the EXCEL file
     */
    public void generateFile() {

        // Création du nom de fichier si celui-ci n'a pas été défini au préalable
        if (this.fileName.isEmpty()) {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
            LocalDateTime now = LocalDateTime.now();

            this.fileName = this.resultFolder + "/Entrées ERICAE exportées du "
                    + dtf.format(this.getLoclaDateTimeFromSeconds(this.minDateEntree))
                    + " au "
                    + dtf.format(this.getLoclaDateTimeFromSeconds(this.maxDateEntree))
                    + " généré le " + dtf.format(now)
                    + ".xlsx";
        }

        LOG.info("Le fichier EXCEL sera généré sous \"" + this.fileName + "\"");

        // Nouvelle feuille EXCEL
        Workbook wb = new XSSFWorkbook();
        // Ajout de l'onglet "Données" à la feuille EXCEL.
        this.worksheetDonneesXlsx(wb);

//        Font font = wb.createFont();
//        font.setCharSet(FontCharset.EASTEUROPE.getValue());
        CodePageUtil cpu = new CodePageUtil();

        try {
            cpu.codepageToEncoding(CodePageUtil.CP_UTF8);
            //TODO Attacher le code page au workbook
        } catch (UnsupportedEncodingException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
        //TODO Ajouter les onglets ici
        // Write the output to a file
        try ( FileOutputStream fileOut = new FileOutputStream(this.fileName)) {
            wb.write(fileOut);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Converti un temps exprimé en seconde depuis le 1er janvier 1970
     *
     * @param epoch
     * @return
     */
    private LocalDateTime getLoclaDateTimeFromSeconds(Long secondes) {
        ZoneId zone = ZoneId.of("Europe/Paris");

        LocalDateTime ldt = LocalDateTime.ofEpochSecond(secondes, 0, ZoneOffset.of("+1"));

        int year = ldt.getYear();

        // Passage à l'heure d'été
        LocalDateTime beginDate = LocalDateTime.of(year, Month.MARCH, 28, 2, 0);
        // Passage à l'heure d'hiver
        LocalDateTime endDate = LocalDateTime.of(year, Month.OCTOBER, 22, 3, 0);

        if (ldt.isAfter(beginDate) && ldt.isBefore(endDate)) {
            ldt = ldt.plusHours(1);
        }

        return ldt;
    }
}
