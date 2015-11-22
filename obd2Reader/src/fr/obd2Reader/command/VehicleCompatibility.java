package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

public class VehicleCompatibility extends ObdCommand{

	public VehicleCompatibility(OutputStream out, InputStream in){
		super(out, in);
	}
	
	public void compute(){
		for(int i = 0; i < 5; i++){
			sendCommand("01" + i*2 + "0");
			read();
		}
		for(int i=4; i>=0; i--){
			getInBuff().remove(6*i);
			getInBuff().remove(6*i);
		}
	}
	
}
