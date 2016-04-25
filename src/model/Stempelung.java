package model;

//import SQLConnector;

import java.util.Calendar;
import java.util.Date;

import controller.DBQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Stempelung {

	private  int beginnStd;
	private  int beginnMin;
	private  int endeStd;
	private  int endeMin;
	private   Date datum;
	private String datumFormatiert_lang; // Datum als String Formatiert mit Jahreszahl
	private String datumFormatiert_kurz; // Datum als String Formatiert ohne Jahreszahl
	private String kennzeichen;
	private String dayOfWeek;
	private boolean feiertag;
	private double fehlzeit;
	
	SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yyyy");
	
	public Stempelung(String pnr, int beginnStd, int beginnMin, int endeStd, int endeMin, Date datum, String kennzeichen) {	
		this.beginnStd = beginnStd;
		this.beginnMin = beginnMin;
		this.endeStd = endeStd;
		this.endeMin = endeMin;
		this.datum = datum;
		this.kennzeichen = kennzeichen;	
		// Ab hier werden die Werte selbst berechnet
		this.dayOfWeek = getDayofWeek();
		this.feiertag = isFeiertag();
		this.datumFormatiert_lang = simplifyDateJahr();
		this.datumFormatiert_kurz = simplifyDate();
		this.fehlzeit = createFehlzeit();
	}
		
    private double createFehlzeit() {
    	double min = this.endeMin - this.beginnMin;
		double hours = this.endeStd-this.beginnStd;
		min = min/60;
		double differenz = hours + min;	
		if( differenz == 0)
		{
			differenz = 8;
		}else if( differenz <= 3.5){
			differenz = 0.5;
		}
		return differenz;
	}

    protected String simplifyDateJahr() {
        try {
        	String value = datum.toString();
            SimpleDateFormat formatZEF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatSQL = new SimpleDateFormat("dd.MM.yyyy");
            Date date = (formatZEF.parse(value));
            return formatSQL.format(date);
        } catch (ParseException ex) {
            System.out.println("Fehler in Absence.simplifyDate.");    
        }
        return null;
    }
	
	public String simplifyDate() {
        try {
        	String value = datum.toString();
            SimpleDateFormat formatZEF = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatSQL = new SimpleDateFormat("dd.MM.");
            Date date = (formatZEF.parse(value));
            return formatSQL.format(date);
        } catch (ParseException ex) {
            System.out.println("Fehler in Absence.simplifyDate.");    
        }
        return null;
    }

	protected int getBeginnStd() {
		return beginnStd;
	}
		
	protected void setBeginnStd(int beginnStd) {
		this.beginnStd = beginnStd;
	}
	
	protected int getBeginnMin() {
		return beginnMin;
	}
	
	protected void setBeginnMin(int beginnMin) {
		this.beginnMin = beginnMin;
	}
	
	protected int getEndeStd() {
		return endeStd;
	}
	
	protected void setEndeStd(int endeStd) {
		this.endeStd = endeStd;
	}
	
	protected int getEndeMin() {
		return endeMin;
	}
	
	protected void setEndeMin(int endeMin) {
		this.endeMin = endeMin;
	}
		
	public Date getDatum() {
		return datum;
	}
	
	protected void setDatum(Date datum) {
		this.datum = datum;
	}
	
	protected String getDatumformatiert_lang() {
		return datumFormatiert_lang;
	}
	
	protected String getDatumformatiert_kurz() {
		return datumFormatiert_kurz;
	}
	
	protected void setDatumFormatiert(String datumFormatiert) {
		this.datumFormatiert_lang = datumFormatiert;
	}
	
	private String getDayofWeek(){
		
		Calendar c = Calendar.getInstance();
		c.setTime(this.datum); 

		switch(c.get(Calendar.DAY_OF_WEEK)){
		case 1: return "Sonntag";
		case 2: return "Montag";
		case 3: return "Dienstag";
		case 4: return "Mittwoch";
		case 5: return "Donnerstag";
		case 6: return "Freitag";
		case 7: return "Samstag";
		default:
			return null;
		}		
	}
	
	protected String getKennzeichen(){
		return this.kennzeichen;
	}
	
	public void print(int i)
	{
		if(beginnStd!=0 & beginnStd != 8){
			System.out.println("Beginn der Fehlzeit am: "+datum+" um: "+beginnStd+":"+beginnMin);
			System.out.println("Ende der Fehlzeit am: "+datum+" um: "+beginnStd+":"+beginnMin);
		}else{
			System.out.println(i+". Am "+this.dayOfWeek+", den: "+datumFormatiert_lang+" Ist ein: "+kennzeichen);
		}			
	}
		
	public boolean isSunday(){
		if(this.dayOfWeek.equals("Sunday"))
		{
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isSaturday(){
		if(this.dayOfWeek.equals("Saturday")){
			return true;
		}
		else{
			return false;
		}
	}
	
	public boolean isHoliday(){
		return this.feiertag;
	}
	
	public boolean isNextDay(){
		return true;
	}
	
	public boolean isFeiertag(){
		for (Date tag : DBQuery.feiertage){
			if(tag.equals(this.datum)){
				return true;
			}	
		}	
		return false;
	}

	public String getWeekDay(){
		return this.dayOfWeek;
	}
	
	public double getFehlzeit()
	{
		return this.fehlzeit;
	}
	
	public void setFehlzeit(double fehlzeit)
	{
		this.fehlzeit = fehlzeit;
	}
	
}
