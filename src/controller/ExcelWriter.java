package controller;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Footer;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import model.Mitarbeiter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ExcelWriter {

    private int lastRow = 5;
    Workbook workbook;
    Sheet sheet;
    private String startDate;
    private String endDate;
    private String path;
    private Mitarbeiter[] mitarbeiterDB;
    private static String filename;
    
    public static String getFilename() {
        return filename;
    }


    public ExcelWriter(String startDate, String endDate, String path, Mitarbeiter[] mitarbeiterDB){
    	this.startDate = startDate;
    	this.endDate = endDate;
    	this.path = path;
    	this.mitarbeiterDB = mitarbeiterDB;
    	filename = path + "Fehlzeitenerfassung_" + this.startDate + "-" + this.endDate + ".xls";
    }
    
    
    public void createExcel() {
        
        workbook = new HSSFWorkbook();
        sheet = workbook.createSheet("Urlaubs- und Krankheits-Auswertung");

        sheet.setMargin(Sheet.LeftMargin, 0.75);
        sheet.setMargin(Sheet.RightMargin, 0.75);
        sheet.setMargin(Sheet.TopMargin, 0.4);
        sheet.setMargin(Sheet.BottomMargin, 0.4);

        sheet.setColumnWidth(0, 6 * 256);
        sheet.setColumnWidth(1, 26 * 256);
        sheet.setColumnWidth(2, 21 * 256);
        sheet.setColumnWidth(3, 8 * 256);
        sheet.setColumnWidth(4, 21 * 256);
        sheet.setColumnWidth(5, 8 * 256);
        
        addTitles();
    }

    protected void addTitles() {
        lastRow += 1;

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);

        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setFont(font);

        Cell cell1 = sheet.createRow(lastRow).createCell(0);
        Cell cell2 = sheet.createRow(lastRow).createCell(1);
        Cell cell3 = sheet.createRow(lastRow).createCell(2);
        Cell cell4 = sheet.createRow(lastRow).createCell(3);
        Cell cell5 = sheet.createRow(lastRow).createCell(4);
        Cell cell6 = sheet.createRow(lastRow).createCell(5);

        cell1.setCellStyle(style);
        cell2.setCellStyle(style);
        cell3.setCellStyle(style);
        cell4.setCellStyle(style);
        cell5.setCellStyle(style);
        cell6.setCellStyle(style);

        cell1.setCellValue("Nr.");
        cell2.setCellValue("Mitarbeiter");
        cell3.setCellValue("Urlaub");
        cell4.setCellValue("Summe");
        cell5.setCellValue("Krankheit");
        cell6.setCellValue("Summe");

        lastRow += 1;

        try {
            FileOutputStream output = new FileOutputStream(path + "Fehlzeitenerfassung_" + this.startDate + "-" + this.endDate + ".xls");   
            workbook.write(output);
            output.close();
        } catch (Exception e) {
            System.out.println("Fehler beim erstellen des Exceldokuments");
            e.printStackTrace();
        }
    }

    public void addHeader() {

        Font ueberschrift_font = workbook.createFont();
        Font startEndDatum_font = workbook.createFont();
        Font auswertungsZeitraum_font = workbook.createFont();

        sheet.addMergedRegion(new CellRangeAddress(0, 1, 3, 5));
        sheet.addMergedRegion(new CellRangeAddress(3, 3, 3, 4));
        sheet.addMergedRegion(new CellRangeAddress(4, 4, 3, 4));

        ueberschrift_font.setFontName("Calibri");
        ueberschrift_font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        ueberschrift_font.setFontHeightInPoints((short) 13);

        auswertungsZeitraum_font.setFontName("Calibri");
        auswertungsZeitraum_font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        auswertungsZeitraum_font.setFontHeightInPoints((short) 11);

        startEndDatum_font.setFontName("Calibri");
        startEndDatum_font.setFontHeightInPoints((short) 11);

        CellStyle ueberschrift_style = workbook.createCellStyle();
        CellStyle startEndDatum_style = workbook.createCellStyle();
        CellStyle auswertungsZeitraum_style = workbook.createCellStyle();

        ueberschrift_style.setFont(ueberschrift_font);
        startEndDatum_style.setFont(startEndDatum_font);
        auswertungsZeitraum_style.setFont(auswertungsZeitraum_font);

        Cell ueberschrift_cell = sheet.createRow(0).createCell(3);
        Cell auswertungszeitraum_cell = sheet.createRow(3).createCell(3);
        Cell startDatum_cell = sheet.createRow(4).createCell(3);

        ueberschrift_cell.setCellStyle(ueberschrift_style);
        auswertungszeitraum_cell.setCellStyle(auswertungsZeitraum_style);
        startDatum_cell.setCellStyle(startEndDatum_style);

        ueberschrift_cell.setCellValue("Urlaubs- & Krankheitsauswertung");
        auswertungszeitraum_cell.setCellValue("Auswertungszeitraum");
        startDatum_cell.setCellValue(startDate + " - " + endDate);
 
        try {
            FileOutputStream output = new FileOutputStream(path + "Fehlzeitenerfassung_" + startDate + "-" + endDate + ".xls");
            workbook.write(output);
            output.close();
        } catch (Exception e) {
            System.out.println("Fehler beim erstellen des Exceldokuments");
            e.printStackTrace();
        }
    }

    public void addImage() throws IOException {
        InputStream is = ExcelWriter.class.getResourceAsStream("hecom_logo.jpg");
        byte[] bytes = IOUtils.toByteArray(is);
        int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
        is.close();

        CreationHelper helper = workbook.getCreationHelper();

        Drawing drawing = sheet.createDrawingPatriarch();

        ClientAnchor anchor = helper.createClientAnchor();
        anchor.setCol1(0);
        anchor.setRow1(0);
        Picture pict = drawing.createPicture(anchor, pictureIdx);
        pict.resize();

        FileOutputStream output = new FileOutputStream(path + "Fehlzeitenerfassung_" + this.startDate + "-" + this.endDate + ".xls");
        workbook.write(output);
        output.close();
    }

    public void fillExcel() {
        //  pnr = 0 ; Name = 1 ; UrlaubVonBis = 2 ; UrlaubSum = 3 ; KrankheitVonBis = 4 ; KrankheitSum = 5
        for (Mitarbeiter mitarbeiter : mitarbeiterDB) {
        	
            CellStyle greyStyle = workbook.createCellStyle();
            CellStyle whiteStyle = workbook.createCellStyle();
            CellStyle redStyle = workbook.createCellStyle();

            Font bold = workbook.createFont();
            bold.setFontName("Calibri");
            bold.setFontHeightInPoints((short) 11);

            greyStyle.setShrinkToFit(false);
            greyStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            greyStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            greyStyle.setBorderBottom(CellStyle.BORDER_THIN);
            greyStyle.setBorderTop(CellStyle.BORDER_THIN);
            greyStyle.setBorderLeft(CellStyle.BORDER_THIN);
            greyStyle.setBorderRight(CellStyle.BORDER_THIN);
            greyStyle.setAlignment(CellStyle.ALIGN_CENTER);
            greyStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            greyStyle.setFont(bold);

            whiteStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            whiteStyle.setShrinkToFit(false);
            whiteStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            whiteStyle.setBorderBottom(CellStyle.BORDER_THIN);
            whiteStyle.setBorderTop(CellStyle.BORDER_THIN);
            whiteStyle.setBorderLeft(CellStyle.BORDER_THIN);
            whiteStyle.setBorderRight(CellStyle.BORDER_THIN);
            whiteStyle.setAlignment(CellStyle.ALIGN_CENTER);
            whiteStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            whiteStyle.setFont(bold);

            redStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
            redStyle.setShrinkToFit(false);
            redStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            redStyle.setBorderBottom(CellStyle.BORDER_THIN);
            redStyle.setBorderTop(CellStyle.BORDER_THIN);
            redStyle.setBorderLeft(CellStyle.BORDER_THIN);
            redStyle.setBorderRight(CellStyle.BORDER_THIN);
            redStyle.setAlignment(CellStyle.ALIGN_CENTER);
            redStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
            redStyle.setFont(bold);

            int length = Core.getMaxVonBis(mitarbeiter);
            //System.out.println("Länge: " + length);

            //System.out.println("Mitarbeiter: " + mitarbeiter.getName() + " lastRow: " + lastRow + " LastRow + Len: " + (lastRow + length));
            
            
            switch (lastRow%53){
	            case 50:
	                lastRow += 5;
	                addTitles();
	                lastRow += 1;
	                break;
	            case 51:
	                lastRow += 4;
	                addTitles();
	                lastRow += 1;
	                break;
	            case 52:
	                lastRow += 3;
	                addTitles();
	                lastRow += 1;
	                break;
	            case 0:
	                lastRow += 2;
	                addTitles();
	                lastRow += 1;
	                break;
	            case 1:
	                lastRow += 1;
	                addTitles();
	                lastRow += 1;
	                break;
	            default:
                    lastRow += 1;
                    break;     
            }
            
            for (int i = 0; i <= length; i++) {
                //Zellen müssen in der Schleife neu angelegt werden
                Cell pnrCell = sheet.createRow(lastRow + i).createCell(0);
                Cell nameCell = sheet.createRow(lastRow + i).createCell(1);
                Cell urlaubCell = sheet.createRow(lastRow + i).createCell(2);
                Cell urlaubSumCell = sheet.createRow(lastRow + i).createCell(3);
                Cell krankheitCell = sheet.createRow(lastRow + i).createCell(4);
                Cell krankheitSumCell = sheet.createRow(lastRow + i).createCell(5);

                if (mitarbeiter.getFlag()) {
                    //den Zellen einen Style geben
                    pnrCell.setCellStyle(redStyle);
                    nameCell.setCellStyle(redStyle);
                    nameCell.setCellStyle(redStyle);
                    urlaubCell.setCellStyle(redStyle);
                    urlaubSumCell.setCellStyle(redStyle);
                    krankheitCell.setCellStyle(redStyle);
                    krankheitSumCell.setCellStyle(redStyle);
                } else {
                    if (i % 2 != 0) {
                        //den Zellen einen Style geben
                        pnrCell.setCellStyle(whiteStyle);
                        nameCell.setCellStyle(whiteStyle);
                        nameCell.setCellStyle(whiteStyle);
                        urlaubCell.setCellStyle(whiteStyle);
                        urlaubSumCell.setCellStyle(whiteStyle);
                        krankheitCell.setCellStyle(whiteStyle);
                        krankheitSumCell.setCellStyle(whiteStyle);
                    } else{
                        //den Zellen einen Style geben
                        pnrCell.setCellStyle(greyStyle);
                        nameCell.setCellStyle(greyStyle);
                        nameCell.setCellStyle(greyStyle);
                        urlaubCell.setCellStyle(greyStyle);
                        urlaubSumCell.setCellStyle(greyStyle);
                        krankheitCell.setCellStyle(greyStyle);
                        krankheitSumCell.setCellStyle(greyStyle);
                    }
                }

                if (length == 0){
                    length += 1; //Falls kein Eintrag werden trotzdem 2 Reihen/Mitarbeiter angelegt
                }
                
//===============================================Zellen bef�llen==============================================================
                //Falls die erste Zeile erstellt wird
                if (i == 0) {
                    pnrCell.setCellValue(mitarbeiter.getPnr());
                    nameCell.setCellValue(mitarbeiter.getName());
                    //Falls Urlaub genommen wurde wird der Urlaub eingetragen	
                    if (mitarbeiter.getFehlzeitUrlaub().getLength() > 0) {
                        urlaubCell.setCellValue(mitarbeiter.getFehlzeitUrlaub().getIntervall()[i].getVonBis());
                        urlaubSumCell.setCellValue(mitarbeiter.getFehlzeitUrlaub().getIntervall()[i].getDauer());
                    } else {
                        urlaubCell.setCellValue(" - ");
                        urlaubSumCell.setCellValue("0");
                    }
                    //Falls der Mitarbeiter Krank war wird die Erste "Krankheits Periode" eingetragen	
                    if (mitarbeiter.getFehlzeitKrank().getLength() > 0) {

                        krankheitCell.setCellValue(mitarbeiter.getFehlzeitKrank().getIntervall()[i].getVonBis());
                        krankheitSumCell.setCellValue(mitarbeiter.getFehlzeitKrank().getIntervall()[i].getDauer());
                    } else {
                        krankheitCell.setCellValue(" - ");
                        krankheitSumCell.setCellValue("0");
                    }
                }
                //Alle weiteren Zeilen werden befüllt
                else {
                    //Falls die auszugebende Zeile die letzte ist		
                    if (i == length) {
                        pnrCell.setCellValue(" ");
                        nameCell.setCellValue("Summe");
                        urlaubCell.setCellValue(" ");
                        krankheitCell.setCellValue(" ");
                        urlaubSumCell.setCellValue(mitarbeiter.getUrlaubSum());
                        krankheitSumCell.setCellValue(mitarbeiter.getKrankheitSum());
                    }
                    else{
                        pnrCell.setCellValue(" ");
                        nameCell.setCellValue(" ");
                        if (mitarbeiter.getFehlzeitUrlaub().getVonBis().length > i) {
                            urlaubCell.setCellValue(mitarbeiter.getFehlzeitUrlaub().getIntervall()[i].getVonBis());
                            urlaubSumCell.setCellValue(mitarbeiter.getFehlzeitUrlaub().getIntervall()[i].getDauer());
                        } else{
                            urlaubCell.setCellValue(" ");
                            urlaubSumCell.setCellValue(" ");
                        }
                        if (mitarbeiter.getFehlzeitKrank().getVonBis().length > i) {
                            krankheitCell.setCellValue(mitarbeiter.getFehlzeitKrank().getIntervall()[i].getVonBis());
                            krankheitSumCell.setCellValue(mitarbeiter.getFehlzeitKrank().getIntervall()[i].getDauer());
                        } else{
                            krankheitCell.setCellValue(" ");
                            krankheitSumCell.setCellValue(" ");
                        }
                    }
                }

                try {
                    FileOutputStream output = new FileOutputStream(path + "Fehlzeitenerfassung_" + this.startDate + "-" + this.endDate + ".xls");
                    workbook.write(output);
                    output.close();
                }catch (Exception e){
                    System.out.println("Fehler beim erstellen von Mitarbeiter " + mitarbeiter.getName());
                    System.out.println("Ist das Dokument bereits ge�ffnet?");
                    e.printStackTrace();
                }
            }
            lastRow += length + 1;
        }
    }

    public void fillSum() {

        Footer footer = sheet.getFooter();
        footer.setRight("Seite " + HeaderFooter.page() + "/" + HeaderFooter.numPages());

        CellStyle style = workbook.createCellStyle();

        Font font = workbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        font.setFontName("Calibri");

        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);

        Cell cell3 = sheet.createRow(lastRow + 1).createCell(2);
        Cell cell4 = sheet.createRow(lastRow + 1).createCell(3);
        Cell cell5 = sheet.createRow(lastRow + 1).createCell(4);
        Cell cell6 = sheet.createRow(lastRow + 1).createCell(5);

        cell3.setCellStyle(style);
        cell4.setCellStyle(style);
        cell5.setCellStyle(style);
        cell6.setCellStyle(style);

        double urlaubSum = 0.0;
        double krankheitSum = 0.0;

        for (Mitarbeiter ma : mitarbeiterDB) {
            urlaubSum += ma.getUrlaubSum();
            krankheitSum += ma.getKrankheitSum();
        }
        
        cell3.setCellValue("Summe Urlaub");
        cell4.setCellValue(urlaubSum);
        cell5.setCellValue("Summe Krankheit");
        cell6.setCellValue(krankheitSum);

        try {
            FileOutputStream output = new FileOutputStream(path + "Fehlzeitenerfassung_" + this.startDate + "-" + this.endDate + ".xls");
            workbook.write(output);
            output.close();
        } catch (Exception e) {
            System.out.println("Fehler beim erstellen");
            e.printStackTrace();
        }
    }
}
