package fr.obd2Reader.command;

public class PidCell {

	private int pidNumber;
	private String pidName;
	private Treatment treatment;
	
	public PidCell(int pidNumber, String pidName, Treatment treatment){
		this.pidNumber = pidNumber;
		this.pidName = pidName;
		this.treatment = treatment;
	}
	
	public int getPidNumber(){
		return pidNumber;
	}
	
	public String getPidName(){
		return pidName;
	}
	
	public Treatment getTreatment(){
		return treatment;
	}
	
}
