package fr.obd2Reader.command;

import java.io.*;
import java.util.ArrayList;

public abstract class ObdCommand {
	
	private String command;
	private ArrayList<Integer> inBuff;
	
	public ObdCommand(String command){
		this.command = command;
		this.inBuff = new ArrayList<Integer>();
	}
	
	protected abstract void calculate();
	
	//rendre la lecture et l'écriture interne à chaque objet ObdCommand
	public void sendCommand(OutputStream out){
		
	}
	
	public String read(InputStream in){
		String inBuf = "";
		return inBuf;
	}
	
	protected ArrayList<Integer> getInBuff(){
		return inBuff;
	}
}
