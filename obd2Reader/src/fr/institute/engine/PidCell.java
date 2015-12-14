package fr.institute.engine;

public class PidCell {

	private int pidId;
	private String pidName;
	private String pidUnit;
	private Treatment treatment;
	
	public PidCell(int pidId, String pidName, String pidUnit, Treatment treatment){
		this.pidId = pidId;
		this.pidName = pidName;
		this.pidUnit = pidUnit;
		this.treatment = treatment;
	}
	
	public int getPidId(){
		return pidId;
	}
	
	public String getPidName(){
		return pidName;
	}
	
	public String getPidUnit(){
		return pidUnit;
	}
	
	public Treatment getTreatment(){
		return treatment;
	}
	
	public void setPidId(int pidId){
		this.pidId = pidId;
	}
	
	public void setPidName(String pidName){
		this.pidName = pidName;
	}
	
	public void setPidUnit(String pidUnit){
		this.pidUnit = pidUnit;
	}
	
	public void setTreatment(Treatment treatment){
		this.treatment = treatment;
	}
}
