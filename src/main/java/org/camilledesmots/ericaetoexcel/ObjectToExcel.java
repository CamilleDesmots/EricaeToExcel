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
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

    private Map< String, Entree> mapEntrees;

    // Liste de toutes les entrées des registres
    private List< Entree> listEntree;

    // Nom du fichier EXCEL
    private String fileName;

    //Plus petite date d'entrée
    private Long minDateEntree;

    //Plus grande date d'entrée
    private Long maxDateEntree;

    // Liste des espèces
    private TreeSet<String> treeSetEspeces;

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
        this.registres = new ArrayList<>();
        this.registres = registres;
        this.resultFolder = resultFolder;
        this.fileName = "";

        this.treeSetEspeces = new TreeSet<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        // Création de la liste des éléments de la classe "Entree"
        this.mapEntrees = new HashMap<>();

        LOG.info("Nombre total de registres : " + registres.size());

        for (Registre registre : registres) {

            for (Entree entree : registre.getEntrees().getEntree()) {
                mapEntrees.put(entree.get_numero_ident(), entree);
            }
        }

        LOG.info("Nombre d'entrée dans tous les registres au total :" + mapEntrees.size());

        // Recherche de la date min et max dans les entrées 
        // Date maximum et minimum
        Comparator<Entree> comparator = Comparator.comparing(Entree::get_date_entree);

        this.listEntree = new ArrayList<Entree>(this.mapEntrees.values());

        this.minDateEntree = this.listEntree.stream().min(comparator).get().get_date_entree();
        this.maxDateEntree = this.listEntree.stream().max(comparator).get().get_date_entree();

        Collections.sort(this.listEntree);

        //LOG.info("La date de l'entrée la plus vielle est : " + this.getLocalDateTimeFromSeconds(this.minDateEntree));
        //LOG.info("La date de l'entrée la récente est : " + this.getLocalDateTimeFromSeconds(this.maxDateEntree));
        for (Entree e : this.listEntree) {
            this.treeSetEspeces.add(e.getEspece().getNomVernac());
        }

        System.out.println("Liste des espèces : ");

        for (String e : this.treeSetEspeces) {
            try {
                System.out.println(" - " + new String(e.toString().getBytes("ISO-8859-1"), "UTF-8"));
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);

            }
        }
        System.out.println(" ");

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
        for (Entree e : this.listEntree) {
            rowCpt++;
            columnCpt = 0;

            try {
                Row row_n = sheet.createRow(rowCpt);

                row_n.createCell(columnCpt++).setCellValue(e.get_numero_ident());

                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getNomVernac().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getNomScient().getBytes("ISO-8859-1"), "UTF-8"));
                row_n.createCell(columnCpt++).setCellValue(new String(e.getEspece().getStatut().getBytes("ISO-8859-1"), "UTF-8"));

                row_n.createCell(columnCpt++).setCellValue(this.getLocalDateTimeFromSeconds(e.get_date_entree()).toString());
                row_n.createCell(columnCpt++).setCellValue(this.getLocalDateTimeFromSeconds(e.get_date_saisie()).toString());

                row_n.createCell(columnCpt++).setCellValue(e.getSexe());
                row_n.createCell(columnCpt++).setCellValue(this.getLocalDateTimeFromSeconds(e.get_date_naissance()).toString());
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
                        row_n.createCell(columnCpt++).setCellValue(this.getLocalDateTimeFromSeconds(s.getDateSortie()).toString());
                        row_n.createCell(columnCpt++).setCellValue(s.getNombre());
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getNature().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getDestination().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getJustificatifsSortie().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getCauseMort().getLibelle().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(new String(s.getNotes().getBytes("ISO-8859-1"), "UTF-8"));
                        row_n.createCell(columnCpt++).setCellValue(this.getLocalDateTimeFromSeconds(s.getDateSaisie()).toString());
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
                    + dtf.format(this.getLocalDateTimeFromSeconds(this.minDateEntree))
                    + " au "
                    + dtf.format(this.getLocalDateTimeFromSeconds(this.maxDateEntree))
                    + " généré le " + dtf.format(now)
                    + ".xlsx";
        }

        LOG.info("Le fichier EXCEL sera généré sous \"" + this.fileName + "\"");

        // Nouvelle feuille EXCEL
        Workbook wb = new XSSFWorkbook();
        // Ajout de l'onglet "Données" à la feuille EXCEL.
        this.worksheetDonneesXlsx(wb);

        this.worksheetEntreesParJoursXlsx(wb);
        this.worksheetEntreesParMoisXlsx(wb);
        this.worksheetEntreesParAnXlsx(wb);

        this.worksheetEntreesParEspecesXlsx(wb);

        this.worksheetEntreesParJourEspeceXlsx(wb);
        this.worksheetEntreesParMoisEspeceXlsx(wb);
        this.worksheetEntreesParAnEspeceXlsx(wb);
        this.worksheetAnimauxParJourXlsx(wb);
        this.worksheetAnimauxParEspeceParJourXlsx(wb);
        this.worksheetDuréeParEspeceXlsx(wb);
        this.worksheetDuréeParEspeceParAnXlsx(wb);
        this.worksheetDuréeParEspeceEtSortieXlsx(wb);
        this.worksheetNombreParEspeceEtSortieXlsx(wb);
        this.worksheetNatureSortieParJourXlsx(wb);
        this.worksheetEspeceNatureSortieParAnXlsx(wb);
        this.worksheetNatureSortieParAnXlsx(wb);

//        Integer cpt = 1;
//
//        for (String str : this.treeSetEspeces) {
//            //LOG.info("TreeSet Element : " + cpt + " - " + str);
//            this.worksheetEntreesParMoisEspeceXlsx(wb, str, cpt);
//            cpt++;
//        }
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
    private LocalDateTime getLocalDateTimeFromSeconds(Long secondes) {
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

    /**
     * Converti un temps exprimé en seconde depuis le 1er janvier 1970 en
     * supprimant l'heure
     *
     * @param epoch
     * @return
     */
    private LocalDateTime getLocalDateTimeToDayFromSeconds(Long secondes) {
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

        ldt.truncatedTo(ChronoUnit.DAYS);

        return ldt;
    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par jour
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParJoursXlsx(Workbook wb) {
        String ongletName = "Entrées x jours";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre d'entrée par jour");

        Map<String, DoubleSummaryStatistics> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())).toString(),
                        Collectors.summarizingDouble(e -> e.get_nombre())
                )
                );

        Map<String, DoubleSummaryStatistics> treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        treeMap.forEach(new BiConsumer<String, DoubleSummaryStatistics>() {
            int i = 1;

            @Override
            public void accept(String k, DoubleSummaryStatistics v) {

                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(k);
                row.createCell(1).setCellValue(v.getCount());
            }
        }
        );
    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par mois
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParMoisXlsx(Workbook wb) {
        String ongletName = "Entrées x mois";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre d'entrée par mois");

        Map<String, DoubleSummaryStatistics> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())).toString(),
                        Collectors.summarizingDouble(e -> e.get_nombre())
                )
                );

        Map<String, DoubleSummaryStatistics> treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        treeMap.forEach(new BiConsumer<String, DoubleSummaryStatistics>() {
            int i = 1;

            @Override
            public void accept(String k, DoubleSummaryStatistics v) {

                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(k);
                row.createCell(1).setCellValue(v.getCount());
            }
        }
        );

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par mois
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParAnXlsx(Workbook wb) {
        String ongletName = "Entrées x an";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre d'entrée par an");

        Map<String, DoubleSummaryStatistics> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())).toString(),
                        Collectors.summarizingDouble(e -> e.get_nombre())
                )
                );

        Map<String, DoubleSummaryStatistics> treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        treeMap.forEach(new BiConsumer<String, DoubleSummaryStatistics>() {
            int i = 1;

            @Override
            public void accept(String k, DoubleSummaryStatistics v) {

                Row row = sheet.createRow(i++);
                row.createCell(0).setCellValue(k);
                row.createCell(1).setCellValue(v.getCount());
            }
        }
        );

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées x Espece
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParEspecesXlsx(Workbook wb) {
        String ongletName = "Entrées x espèce";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Nombre d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Moyenne d'entrée ");
        row_0.createCell(columnCpt++).setCellValue("Nombre max. d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre min. d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Cumul d'entrée");

        Map<String, DoubleSummaryStatistics> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(
                        e -> e.getEspece().getNomVernac(),
                        Collectors.summarizingDouble(e -> e.get_nombre())
                )
                );

        Map<String, DoubleSummaryStatistics> treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        treeMap.forEach(new BiConsumer<String, DoubleSummaryStatistics>() {
            int i = 1;

            @Override
            public void accept(String k, DoubleSummaryStatistics v) {
                try {
                    Row row = sheet.createRow(i++);
                    row.createCell(0).setCellValue(new String(k.getBytes("ISO-8859-1"), "UTF-8"));
                    row.createCell(1).setCellValue(v.getAverage());
                    row.createCell(1).setCellValue(v.getCount());
                    row.createCell(1).setCellValue(v.getMax());
                    row.createCell(1).setCellValue(v.getMin());
                    row.createCell(1).setCellValue(v.getSum());
                } catch (UnsupportedEncodingException ex) {
                    LOG.log(Level.SEVERE, null, ex);

                }

            }
        }
        );

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par Espece
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParJourEspeceXlsx(Workbook wb) {
        Integer occurence;
        String espece_UTF8 = "";

        String ongletName = "Entrées x espèce x jour";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");
        row_0.createCell(columnCpt++).setCellValue("Espèce : " + espece_UTF8);

        Map< String, Map< String, DoubleSummaryStatistics>> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> e.getEspece().getNomVernac(),
                        Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())),
                                Collectors.summarizingDouble(e -> e.get_nombre()))));

        Map< String, Map< String, DoubleSummaryStatistics>> treeMap;
        treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        rowCpt++;

        for (Map.Entry<String, Map<String, DoubleSummaryStatistics>> entry : treeMap.entrySet()) {
            try {
                espece_UTF8 = new String(entry.getKey().replaceAll("'", " ").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            for (Map.Entry<String, DoubleSummaryStatistics> subentry : entry.getValue().entrySet()) {

                Row row = sheet.createRow(rowCpt++);
                row.createCell(0).setCellValue(espece_UTF8);
                row.createCell(1).setCellValue(subentry.getKey());
                row.createCell(2).setCellValue(subentry.getValue().getAverage());
                row.createCell(3).setCellValue(subentry.getValue().getCount());
                row.createCell(4).setCellValue(subentry.getValue().getMax());
                row.createCell(5).setCellValue(subentry.getValue().getMin());
                row.createCell(6).setCellValue(subentry.getValue().getSum());

            }

        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par Espece
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParMoisEspeceXlsx(Workbook wb) {
        Integer occurence;
        String espece_UTF8 = "";

        String ongletName = "Entrées x espèce x mois";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");

        Map< String, Map< String, DoubleSummaryStatistics>> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> e.getEspece().getNomVernac(),
                        Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())),
                                Collectors.summarizingDouble(e -> e.get_nombre()))));

        Map< String, Map< String, DoubleSummaryStatistics>> treeMap;
        treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        rowCpt++;

        for (Map.Entry<String, Map<String, DoubleSummaryStatistics>> entry : treeMap.entrySet()) {
            try {
                espece_UTF8 = new String(entry.getKey().replaceAll("'", " ").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            for (Map.Entry<String, DoubleSummaryStatistics> subentry : entry.getValue().entrySet()) {

                Row row = sheet.createRow(rowCpt++);
                row.createCell(0).setCellValue(espece_UTF8);
                row.createCell(1).setCellValue(subentry.getKey());
                row.createCell(2).setCellValue(subentry.getValue().getAverage());
                row.createCell(3).setCellValue(subentry.getValue().getCount());
                row.createCell(4).setCellValue(subentry.getValue().getMax());
                row.createCell(5).setCellValue(subentry.getValue().getMin());
                row.createCell(6).setCellValue(subentry.getValue().getSum());

            }

        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques d'entrées par Espece
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEntreesParAnEspeceXlsx(Workbook wb) {
        Integer occurence;
        String espece_UTF8 = "";

        String ongletName = "Entrées x espèce x an";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");
        row_0.createCell(columnCpt++).setCellValue("Espèce : " + espece_UTF8);

        Map< String, Map< String, DoubleSummaryStatistics>> map;

        // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
        map = this.listEntree.stream()
                .collect(Collectors.groupingBy(e -> e.getEspece().getNomVernac(),
                        Collectors.groupingBy(e -> dtfDay.format(this.getLocalDateTimeFromSeconds(e.get_date_entree())),
                                Collectors.summarizingDouble(e -> e.get_nombre()))));

        Map< String, Map< String, DoubleSummaryStatistics>> treeMap;
        treeMap = new TreeMap<>(
                (Comparator<String>) (o2, o1) -> o2.compareTo(o1)
        );

        treeMap.putAll(map);

        rowCpt++;

        for (Map.Entry<String, Map<String, DoubleSummaryStatistics>> entry : treeMap.entrySet()) {
            try {
                espece_UTF8 = new String(entry.getKey().replaceAll("'", " ").getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            for (Map.Entry<String, DoubleSummaryStatistics> subentry : entry.getValue().entrySet()) {

                Row row = sheet.createRow(rowCpt++);
                row.createCell(0).setCellValue(espece_UTF8);
                row.createCell(1).setCellValue(subentry.getKey());
                row.createCell(2).setCellValue(subentry.getValue().getAverage());
                row.createCell(3).setCellValue(subentry.getValue().getCount());
                row.createCell(4).setCellValue(subentry.getValue().getMax());
                row.createCell(5).setCellValue(subentry.getValue().getMin());
                row.createCell(6).setCellValue(subentry.getValue().getSum());

            }

        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistique du nombre d'animaux
     * présents par jour.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetAnimauxParJourXlsx(Workbook wb) {
        String ongletName = "Animaux présents x jour";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        short rowCpt = 0;
        short columnCpt = 0;

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        //TODO Appliquer un style et une mise en forme à la 1ère ligne 
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre");

        rowCpt++;

        // On par du jour le plus petit jusqu'au jour le plus récent
        // Un jour = 60 x 60 x 24 = 86 400 seconde
        for (long dateSec = this.minDateEntree; dateSec <= this.maxDateEntree; dateSec = dateSec + 86400) {

            long nombreEntree = 0;

            for (Entree entree : this.listEntree) {
                if (entree.get_date_entree() <= dateSec) {

                    long dateSortieMax = 0;

                    if (entree.getSortiesObject().getSortie() != null) {

                        for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                            // On recherche la date de sortie la plus grande 
                            if (sortie.getDateSortie() > dateSortieMax) {
                                dateSortieMax = sortie.getDateSortie();
                            }
                        }
                    }

                    if (dateSortieMax >= dateSec || dateSortieMax == 0) {
                        // TODO Filtrer les dates par rapport à la date dateSec
                        // Cumuler le nombre d'entrée

                        // TODO : Ajouter dans une map la date dateSec et le nombre d'occurence.
                        nombreEntree = nombreEntree + entree.get_nombre();
                    }

                }

            }

            if (nombreEntree > 0) {
                Row row = sheet.createRow(rowCpt++);
                row.createCell(0).setCellValue(dtfDay.format(this.getLocalDateTimeFromSeconds(dateSec)));
                row.createCell(1).setCellValue(nombreEntree);

            }

        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistique du nombre d'animaux
     * présents par jour.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetAnimauxParEspeceParJourXlsx(Workbook wb) {
        String ongletName = "Animaux x espèce présents x jour";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");
        LOG.log(Level.INFO, "Plage de date " + dtfDay.format(this.getLocalDateTimeFromSeconds(this.minDateEntree)) + " " + dtfDay.format(this.getLocalDateTimeFromSeconds(this.maxDateEntree)));

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Date d'entrée");
        row_0.createCell(columnCpt++).setCellValue("Nombre");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            Map<Long, Integer> mapJourNombre = new HashMap();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac() + " nombre: " + entree.get_nombre() + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                        }
                    }
                }

                if (dateSortieMax == 0) {
                    dateSortieMax = this.maxDateEntree;
                }

                // Itération entre la date d'entrée et la date de sortie.
                for (long date = this.getLocalDateTimeToDayFromSeconds(entree.get_date_entree()).toEpochSecond(ZoneOffset.UTC); date <= dateSortieMax; date = date + 86400) {
                    // Pour chaque jour entre la date d'entrée et la date de sortie on créé une occurence

                    if (mapJourNombre.containsKey(date)) {
                        // La date existe déjà dans la map
                        LOG.finest("Pour  espèce " + espece + " la date " + dtf.format(this.getLocalDateTimeToDayFromSeconds(date)) + " existe déjà.");
                        int nouvelleValeur = (int) mapJourNombre.get(date);
                        nouvelleValeur = nouvelleValeur + entree.get_nombre();
                        LOG.finest("Ancienne valeur : " + mapJourNombre.get(date) + " nuovelle valeur : " + nouvelleValeur);
                        mapJourNombre.put(date, nouvelleValeur);
                    } else {
                        // La date n'existe pas encore 
                        LOG.finest("Pour  espèce " + espece + " la date " + dtf.format(this.getLocalDateTimeToDayFromSeconds(date)) + " n'existe pas.");
                        mapJourNombre.put(date, entree.get_nombre());
                    }

                }

            }

            LOG.finest("Nombre d'élément dans la map pour l'espèce : " + espece + " : " + mapJourNombre.size());

            // TODO : Cumuler la liste listJourNombre par jour
            if (mapJourNombre.size() > 0) {
                for (Map.Entry m : mapJourNombre.entrySet()) {
                    Long date = (Long) m.getKey();
                    Integer nombre = (Integer) m.getValue();

                    Row row = sheet.createRow(rowCpt++);
                    row.createCell(0).setCellValue(espece_UTF8);
                    row.createCell(1).setCellValue(dtfDay.format(this.getLocalDateTimeFromSeconds(date)));
                    row.createCell(2).setCellValue(nombre);

                }

            }

            mapJourNombre.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistique de durée de séjour par
     * espèce présents par jour.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetDuréeParEspeceXlsx(Workbook wb) {
        String ongletName = "Durée séjour x espèce";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            List<Long> listDuree = new ArrayList();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac() + " nombre: " + entree.get_nombre() + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                        }
                    }
                }

                if (dateSortieMax == 0) {
                    dateSortieMax = this.maxDateEntree;
                }

                // On converti en minutes
                listDuree.add((dateSortieMax - entree.get_date_entree()) / 86400);

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + listDuree.size());

            // TODO : Cumuler la liste listJourNombre par jour
            if (listDuree.size() > 0) {

                DoubleSummaryStatistics summaryStatistics;

                // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
                summaryStatistics = listDuree.stream()
                        .collect(Collectors.summarizingDouble(e -> e.longValue()));

                Row row = sheet.createRow(rowCpt++);
                row.createCell(0).setCellValue(espece_UTF8);
                row.createCell(1).setCellValue(summaryStatistics.getAverage());
                row.createCell(2).setCellValue(summaryStatistics.getCount());
                row.createCell(3).setCellValue(summaryStatistics.getMax());
                row.createCell(4).setCellValue(summaryStatistics.getMin());
                row.createCell(5).setCellValue(summaryStatistics.getSum());

            }

            listDuree.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistique de durée de séjour par
     * espèce présents par jour.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetDuréeParEspeceParAnXlsx(Workbook wb) {
        String ongletName = "Durée séjour x espèce x an";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        DateTimeFormatter dtfYear = DateTimeFormatter.ofPattern("yyyy");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Année");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            Map<String, List<Long>> mapAnneeDuree = new HashMap();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "  Entrée à tester: espece: " + entree.getEspece().getNomVernac() + " nombre: " + entree.get_nombre() + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                String annee = dtfYear.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree()));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                        }
                    }
                }

                if (dateSortieMax == 0) {
                    dateSortieMax = this.maxDateEntree;
                }

                // Conversion en minutes
                Long duree = (dateSortieMax - entree.get_date_entree()) / 86400;

                if (mapAnneeDuree.containsKey(annee)) {
                    // L'année existe déjà.
                    mapAnneeDuree.get(annee).add(duree);
                } else {
                    // Année à créer.
                    List<Long> listDuree = new ArrayList<>();
                    listDuree.add(duree);
                    mapAnneeDuree.put(annee, listDuree);
                }

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + mapAnneeDuree.size());

            // TODO : Cumuler la liste listJourNombre par jour
            if (mapAnneeDuree.size() > 0) {

                for (Map.Entry<String, List<Long>> entry : mapAnneeDuree.entrySet()) {

                    DoubleSummaryStatistics summaryStatistics;

                    // sorted((i1, i2) -> i2.get_date_entree().compareTo(i1.get_date_entree()))
                    summaryStatistics = entry.getValue().stream()
                            .collect(Collectors.summarizingDouble(e -> e.longValue()));

                    Row row = sheet.createRow(rowCpt++);
                    row.createCell(0).setCellValue(espece_UTF8);
                    row.createCell(1).setCellValue(entry.getKey());
                    row.createCell(2).setCellValue(summaryStatistics.getAverage());
                    row.createCell(3).setCellValue(summaryStatistics.getCount());
                    row.createCell(4).setCellValue(summaryStatistics.getMax());
                    row.createCell(5).setCellValue(summaryStatistics.getMin());
                    row.createCell(6).setCellValue(summaryStatistics.getSum());

                }

                mapAnneeDuree.clear();
            }
        }
    }

    /**
     * Création d'une feuille EXCEL avec les statistiques de durée de séjour par
     * espèce présents par sortie.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetDuréeParEspeceEtSortieXlsx(Workbook wb) {
        String ongletName = "Durée séjour x espèce x nature sortie";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Nature de sortie");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            Map< String, List< Long>> mapSortie = new TreeMap<>();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac()
                        + " nombre: " + entree.get_nombre()
                        + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                String libelleSortieMax = "";

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                            libelleSortieMax = sortie.getNature().getLibelle();
                        }
                    }
                }

                if (dateSortieMax == 0) {
                    dateSortieMax = this.maxDateEntree;
                }

                Long duree = (dateSortieMax - entree.get_date_entree()) / 86400;

                // On converti en minutes
                if (mapSortie.containsKey(libelleSortieMax)) {
                    // La Map contient déjà ce motif de sortie.
                    mapSortie.get(libelleSortieMax).add(duree);
                } else {
                    // La Map ne contient pas encore ce motif de sorte.
                    List< Long> listSimple = new ArrayList<>();
                    listSimple.add(duree);
                    mapSortie.put(libelleSortieMax, listSimple);
                }

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + mapSortie.size());

            if (mapSortie.size() > 0) {

                for (Map.Entry<String, List<Long>> setDuree : mapSortie.entrySet()) {
                    DoubleSummaryStatistics summaryStatistics;

                    summaryStatistics = setDuree.getValue().stream()
                            .collect(Collectors.summarizingDouble(e -> e.longValue()));

                    String motifSortie = "";
                    try {
                        motifSortie = new String(setDuree.getKey().getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }

                    Row row = sheet.createRow(rowCpt++);
                    row.createCell(0).setCellValue(espece_UTF8);

                    row.createCell(1).setCellValue(motifSortie);
                    row.createCell(2).setCellValue(summaryStatistics.getAverage());
                    row.createCell(3).setCellValue(summaryStatistics.getCount());
                    row.createCell(4).setCellValue(summaryStatistics.getMax());
                    row.createCell(5).setCellValue(summaryStatistics.getMin());
                    row.createCell(6).setCellValue(summaryStatistics.getSum());

                }

            }

            mapSortie.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec les statistiques de nombre de séjour
     * par espèce présents par sortie.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetNombreParEspeceEtSortieXlsx(Workbook wb) {
        String ongletName = "Espèce x nature sortie";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm.ss.SSS");
        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Nature de sortie");
        row_0.createCell(columnCpt++).setCellValue("Moyenne");
        row_0.createCell(columnCpt++).setCellValue("Nombre");
        row_0.createCell(columnCpt++).setCellValue("Max");
        row_0.createCell(columnCpt++).setCellValue("Min");
        row_0.createCell(columnCpt++).setCellValue("Somme");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            Map< String, List< Integer>> mapSortie = new TreeMap<>();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac()
                        + " nombre: " + entree.get_nombre()
                        + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                String libelleSortieMax = "";
                Integer nombreMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                            libelleSortieMax = sortie.getNature().getLibelle();
                            nombreMax = sortie.getNombre();
                        }
                    }
                }

                // On converti en minutes
                if (mapSortie.containsKey(libelleSortieMax)) {
                    // La Map contient déjà ce motif de sortie.
                    mapSortie.get(libelleSortieMax).add(nombreMax);
                } else {
                    // La Map ne contient pas encore ce motif de sorte.
                    List< Integer> listSimple = new ArrayList<>();
                    listSimple.add(nombreMax);
                    mapSortie.put(libelleSortieMax, listSimple);
                }

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + mapSortie.size());

            if (mapSortie.size() > 0) {

                for (Map.Entry<String, List<Integer>> setDuree : mapSortie.entrySet()) {
                    DoubleSummaryStatistics summaryStatistics;

                    summaryStatistics = setDuree.getValue().stream()
                            .collect(Collectors.summarizingDouble(e -> e.intValue()));

                    String motifSortie = "";
                    try {
                        motifSortie = new String(setDuree.getKey().getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }

                    Row row = sheet.createRow(rowCpt++);
                    row.createCell(0).setCellValue(espece_UTF8);

                    row.createCell(1).setCellValue(motifSortie);
                    row.createCell(2).setCellValue(summaryStatistics.getAverage());
                    row.createCell(3).setCellValue(summaryStatistics.getCount());
                    row.createCell(4).setCellValue(summaryStatistics.getMax());
                    row.createCell(5).setCellValue(summaryStatistics.getMin());
                    row.createCell(6).setCellValue(summaryStatistics.getSum());

                }

            }

            mapSortie.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec la nature de sorties par jour. espèce
     * présents x sortie.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetNatureSortieParJourXlsx(Workbook wb) {
        String ongletName = "Espèce x nature sortie x jour";

        DateTimeFormatter dtfDay = DateTimeFormatter.ofPattern("yyyy/MM/dd");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Nature de sortie");
        row_0.createCell(columnCpt++).setCellValue("Date");
        row_0.createCell(columnCpt++).setCellValue("Nombre");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            // Nature sortie, Date, Nombre de sortie
            Map< String, Map< Long, Integer>> mapSortie = new TreeMap<>();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac()
                        + " nombre: " + entree.get_nombre()
                        + " date arrivée: " + dtfDay.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                long dateSortieMax = 0;

                String libelleSortieMax = "";
                Integer nombreMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (sortie.getDateSortie() > dateSortieMax) {
                            dateSortieMax = sortie.getDateSortie();
                            libelleSortieMax = sortie.getNature().getLibelle();
                            nombreMax = sortie.getNombre();
                        }
                    }
                }

                // On converti en minutes
                if (mapSortie.containsKey(libelleSortieMax)) {
                    // La Map contient déjà ce motif de sortie.
                    if (mapSortie.get(libelleSortieMax).containsKey(dateSortieMax)) {
                        // La date existe déjà
                        // TODO Ajouter le nombre d'entrée à cette date 

                        Integer nouveauCumul = mapSortie.get(libelleSortieMax).get(dateSortieMax) + nombreMax;

                        mapSortie.get(libelleSortieMax).put(dateSortieMax, nouveauCumul);
                    } else {
                        // Il faut créer 
                        mapSortie.get(libelleSortieMax).put(dateSortieMax, nombreMax);
                    }

                } else {
                    // La Map ne contient pas encore ce motif de sortie.
                    Map<Long, Integer> mapSimple = new HashMap<>();
                    mapSimple.put(dateSortieMax, nombreMax);

                    mapSortie.put(libelleSortieMax, mapSimple);
                }

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + mapSortie.size());

            if (mapSortie.size() > 0) {

                for (Map.Entry<String, Map<Long, Integer>> mapNatureSortie : mapSortie.entrySet()) {
                    for (Map.Entry<Long, Integer> mapDateCumul : mapNatureSortie.getValue().entrySet()) {

                        String motifSortie = "";
                        try {
                            motifSortie = new String(mapNatureSortie.getKey().getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }

                        Row row = sheet.createRow(rowCpt++);
                        row.createCell(0).setCellValue(espece_UTF8);
                        row.createCell(1).setCellValue(motifSortie);
                        row.createCell(2).setCellValue(dtfDay.format(this.getLocalDateTimeToDayFromSeconds(mapDateCumul.getKey())));
                        row.createCell(3).setCellValue(mapDateCumul.getValue());

                    }
                }

            }

            mapSortie.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec la nature de sorties par espèce par an
     * présents par sortie.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetEspeceNatureSortieParAnXlsx(Workbook wb) {
        String ongletName = "Espèce x Nature sortie x an";

        DateTimeFormatter dtfYear = DateTimeFormatter.ofPattern("yyyy");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Espèce");
        row_0.createCell(columnCpt++).setCellValue("Nature de sortie");
        row_0.createCell(columnCpt++).setCellValue("Date");
        row_0.createCell(columnCpt++).setCellValue("Nombre");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        for (String espece : this.treeSetEspeces) {
            LOG.log(Level.INFO, "Itération espèce " + espece);

            // Nature sortie, Date, Nombre de sortie
            Map< String, Map< String, Integer>> mapSortie = new TreeMap<>();

            try {
                espece_UTF8 = new String(espece.getBytes("ISO-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }

            // On par du jour le plus petit jusqu'au jour le plus récent
            // Un jour = 60 x 60 x 24 = 86 400 seconde
            List< Entree> entreesFiltrees = this.listEntree.stream()
                    .filter(e -> e.getEspece().getNomVernac().contentEquals(espece))
                    .collect(Collectors.toList());

            LOG.info("Nombre d'entrée pour l'espèce " + espece + " : " + entreesFiltrees.size());

            for (Entree entree : entreesFiltrees) {

                LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac()
                        + " nombre: " + entree.get_nombre()
                        + " date arrivée: " + dtfYear.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

                // Recherche de la date de sortie la plus grande
                String dateSortieMax = "";

                String libelleSortieMax = "";
                Integer nombreMax = 0;

                if (entree.getSortiesObject().getSortie() != null) {

                    for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                        // On recherche la date de sortie la plus grande 
                        if (dtfYear.format(this.getLocalDateTimeToDayFromSeconds(sortie.getDateSortie())).compareTo(dateSortieMax) != 0) {
                            dateSortieMax = dtfYear.format(this.getLocalDateTimeToDayFromSeconds(sortie.getDateSortie()));
                            libelleSortieMax = sortie.getNature().getLibelle();
                            nombreMax = sortie.getNombre();
                        }
                    }
                }

                // On converti en minutes
                if (mapSortie.containsKey(libelleSortieMax)) {
                    // La Map contient déjà ce motif de sortie.
                    if (mapSortie.get(libelleSortieMax).containsKey(dateSortieMax)) {
                        // La date existe déjà
                        // TODO Ajouter le nombre d'entrée à cette date 

                        Integer nouveauCumul = mapSortie.get(libelleSortieMax).get(dateSortieMax) + nombreMax;

                        mapSortie.get(libelleSortieMax).put(dateSortieMax, nouveauCumul);
                    } else {
                        // Il faut créer 
                        mapSortie.get(libelleSortieMax).put(dateSortieMax, nombreMax);
                    }

                } else {
                    // La Map ne contient pas encore ce motif de sortie.
                    Map<String, Integer> mapSimple = new HashMap<>();
                    mapSimple.put(dateSortieMax, nombreMax);

                    mapSortie.put(libelleSortieMax, mapSimple);
                }

            }

            LOG.finest("Nombre d'élément dans la List pour l'espèce : " + espece + " : " + mapSortie.size());

            if (mapSortie.size() > 0) {

                for (Map.Entry<String, Map<String, Integer>> mapNatureSortie : mapSortie.entrySet()) {
                    for (Map.Entry<String, Integer> mapDateCumul : mapNatureSortie.getValue().entrySet()) {

                        String motifSortie = "";
                        try {
                            motifSortie = new String(mapNatureSortie.getKey().getBytes("ISO-8859-1"), "UTF-8");
                        } catch (UnsupportedEncodingException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }

                        Row row = sheet.createRow(rowCpt++);
                        row.createCell(0).setCellValue(espece_UTF8);
                        row.createCell(1).setCellValue(motifSortie);
                        row.createCell(2).setCellValue(mapDateCumul.getKey());
                        row.createCell(3).setCellValue(mapDateCumul.getValue());

                    }
                }

            }

            mapSortie.clear();
        }

    }

    /**
     * Création d'une feuille EXCEL avec la nature de sorties par espèce par an
     * présents par sortie.
     *
     * @param wb Onglet EXCEL
     */
    private void worksheetNatureSortieParAnXlsx(Workbook wb) {
        String ongletName = "Nature sortie x an";

        DateTimeFormatter dtfYear = DateTimeFormatter.ofPattern("yyyy");

        int rowCpt = 0;
        short columnCpt = 0;
        String espece_UTF8 = "";

        LOG.log(Level.INFO, "Création de l'onglet \"" + ongletName + "\"");

        CreationHelper creationHelper = wb.getCreationHelper();
        Sheet sheet = wb.createSheet(ongletName);

        // Titre de colonnes
        Row row_0 = sheet.createRow(rowCpt);
        row_0.createCell(columnCpt++).setCellValue("Nature de sortie");
        row_0.createCell(columnCpt++).setCellValue("Date");
        row_0.createCell(columnCpt++).setCellValue("Nombre");

        rowCpt++;

        LOG.info("Nombre d'entrée au total :" + this.mapEntrees.size());

        // Nature sortie, Date, Nombre de sortie
        Map<String, Map< String, Integer>> mapSortie = new TreeMap<>();

        for (Entree entree : this.listEntree) {

            LOG.log(Level.FINEST, "      Entrée à tester: espece: " + entree.getEspece().getNomVernac()
                    + " nombre: " + entree.get_nombre()
                    + " date arrivée: " + dtfYear.format(this.getLocalDateTimeFromSeconds(entree.get_date_entree())));

            // Recherche de la date de sortie la plus grande
            String dateSortieMax = "";

            String libelleSortieMax = "";
            Integer nombreMax = 0;

            if (entree.getSortiesObject().getSortie() != null) {

                for (Sortie sortie : entree.getSortiesObject().getSortie()) {
                    // On recherche la date de sortie la plus grande 
                    if (dtfYear.format(this.getLocalDateTimeToDayFromSeconds(sortie.getDateSortie())).compareTo(dateSortieMax) != 0) {
                        dateSortieMax = dtfYear.format(this.getLocalDateTimeToDayFromSeconds(sortie.getDateSortie()));
                        libelleSortieMax = sortie.getNature().getLibelle();
                        nombreMax = sortie.getNombre();
                    }
                }
            }

            // On converti en minutes
            if (mapSortie.containsKey(libelleSortieMax)) {
                // La Map contient déjà ce motif de sortie.
                if (mapSortie.get(libelleSortieMax).containsKey(dateSortieMax)) {
                    // La date existe déjà
                    Integer nouveauCumul = mapSortie.get(libelleSortieMax).get(dateSortieMax) + nombreMax;

                    mapSortie.get(libelleSortieMax).put(dateSortieMax, nouveauCumul);
                } else {
                    // Il faut créer 
                    mapSortie.get(libelleSortieMax).put(dateSortieMax, nombreMax);
                }

            } else {
                // La Map ne contient pas encore ce motif de sortie.
                Map<String, Integer> mapSimple = new HashMap<>();
                mapSimple.put(dateSortieMax, nombreMax);

                mapSortie.put(libelleSortieMax, mapSimple);
            }

        }

        if (mapSortie.size() > 0) {

            for (Map.Entry<String, Map<String, Integer>> mapNatureSortie : mapSortie.entrySet()) {
                for (Map.Entry<String, Integer> mapDateCumul : mapNatureSortie.getValue().entrySet()) {

                    String motifSortie = "";
                    try {
                        motifSortie = new String(mapNatureSortie.getKey().getBytes("ISO-8859-1"), "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        LOG.log(Level.SEVERE, null, ex);
                    }

                    Row row = sheet.createRow(rowCpt++);
                    row.createCell(0).setCellValue(motifSortie);
                    row.createCell(1).setCellValue(mapDateCumul.getKey());
                    row.createCell(2).setCellValue(mapDateCumul.getValue());

                }
            }

        }

        mapSortie.clear();
    }

}
