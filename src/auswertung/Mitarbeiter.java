/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package auswertung;


/**
 *
 * @author Malte Braun
 */
public class Mitarbeiter
{    
    private String vorname;
    private String nachname;
    private String pnr;
    private Fehlzeit[] fehlzeiten;
    private double krankheitSum;
    private double urlaubSum;
    private boolean flag = false;
       
	public Mitarbeiter(String vorname, String nachname, String pnr) throws Exception
	{
		System.out.println(vorname+" "+nachname);
        this.vorname = vorname;
        this.nachname = nachname;
        this.pnr = pnr;
        fehlzeiten = new Fehlzeit[2];
        fehlzeiten[0] = new Fehlzeit("Krankheit",pnr);
        fehlzeiten[1] = new Fehlzeit("Urlaub",pnr);
        this.krankheitSum = CountKrankheit();
        this.urlaubSum = CountUrlaub();
        this.flag = setFlag();
    }
 
	public void print()
	{
		System.out.println();
		System.out.print(vorname+" ");
		System.out.print(nachname+" ");
		System.out.println(pnr+" ");
		System.out.println("Summe Krankheitstage: "+krankheitSum+" ");
		System.out.println("Summe Urlaubstage: "+urlaubSum);
	}

	protected String getName(){
		String name = this.vorname+" "+this.nachname;
		return name;
	}
	
 	protected void setVorname(String vorname)
 	{
		this.vorname = vorname;
	}
	
	protected void setNachname(String nachname)
	{
		this.nachname = nachname;
	}
	
	protected String getPnr()
	{
		return pnr;
	}

	protected void setPnr(String pnr)
	{
		this.pnr = pnr;
	}

	protected double getKrankheitSum()
	{
		return krankheitSum;
	}

	protected void setKrankheitSum(double krankheitSum)
	{
		this.krankheitSum = krankheitSum;
	}

	protected double getUrlaubSum()
	{
		return urlaubSum;
	}

	protected void setUrlaubSum(double urlaubSum)
	{
		this.urlaubSum = urlaubSum;
	}
	
	private double CountKrankheit(){
		return fehlzeiten[0].getKrankheitFehlzeit();
	}
	
	private double CountUrlaub(){
		return fehlzeiten[1].getUrlaubFehlzeit();
	}
	
	private boolean setFlag() {
		return ( fehlzeiten[0].getFlag() || fehlzeiten[1].getFlag());			
	}
	
	public boolean getFlag(){
		return this.flag;
	}
	
	public Fehlzeit getFehlzeitKrank()
	{
		return fehlzeiten[0];
	}
	
	public Fehlzeit getFehlzeitUrlaub()
	{
		return fehlzeiten[1];
	}
}

