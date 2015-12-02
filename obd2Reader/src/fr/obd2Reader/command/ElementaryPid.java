package fr.obd2Reader.command;

public class ElementaryPid {

	private String pidName;
	private int pidNumber;
	
	public ElementaryPid(String pidName, int pidNumber){
		this.pidName = pidName;
		this.pidNumber = pidNumber;
	}
	
	public String getPidName(){
		return pidName;
	}
	
	public int getPidNumber(){
		return pidNumber;
	}
	
}
