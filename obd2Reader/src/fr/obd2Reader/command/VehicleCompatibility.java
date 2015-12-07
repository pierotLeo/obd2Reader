package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class VehicleCompatibility extends ObdCommand implements CompatibilityTestConstants, GlobalConstants{
	
	public VehicleCompatibility(OutputStream out, InputStream in){
		super(out, in);
	}
	
	public VehicleCompatibility(){
		super();
	}
	
	public void compute(){
		for(int i = 0; i < COMPATIBILITY_RESPONSE_LENGTH; i++){
			sendCommand("01" + i*2 + "0");
			read();
		}
		for(int i = COMPATIBILITY_RESPONSE_LENGTH-1; i >= 0; i--){
			for(int j = 0; j < 2; j++){
				getInBuff().remove(6*i);
			}
		}
	}
	
	public String[] availableCommands(){
		ArrayList<String> availableCommandsList = new ArrayList<String>();
		
		compute();
		for(int i = 0; i < pidTable.size(); i++){
			if(CompatibilityTestableCommand.isCompatible(getInBuff(),pidTable.get(i).getPidNumber()))
					availableCommandsList.add(pidTable.get(i).getPidName());
		}
		
		String[] availableCommands = availableCommandsList.toArray(new String[availableCommandsList.size()]);
		
		return availableCommands;
	}
	
}
