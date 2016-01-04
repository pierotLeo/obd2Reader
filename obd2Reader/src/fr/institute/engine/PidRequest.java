package fr.institute.engine;

import java.util.ArrayList;

public class PidRequest {

	private int pidId;
	private ArrayList<Integer> buffer;

	public PidRequest(int pidNumber){
		this.pidId = pidNumber;
		buffer = new ArrayList<Integer>();
	}
	
	public ArrayList<Integer> getBuffer(){
		return buffer;
	}
	
	public int getPidId(){
		return pidId;
	}
	
	public void setPidId(int pidNumber){
		this.pidId = pidNumber;
	}
	
	public void setBuffer(ArrayList<Integer> buffer){
		this.buffer = buffer;
	}
	
}
