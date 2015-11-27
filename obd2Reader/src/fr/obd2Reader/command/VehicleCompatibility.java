package fr.obd2Reader.command;

import java.io.InputStream;
import java.io.OutputStream;

public class VehicleCompatibility extends ObdCommand{

	public VehicleCompatibility(OutputStream out, InputStream in){
		super(out, in);
	}
	
	public VehicleCompatibility(){
		super();
	}
	
	public void compute(){
		for(int i = 0; i < 5; i++){
			sendCommand("01" + i*2 + "0");
			read();
		}
		for(int i = 4; i >= 0; i--){
			for(int j = 0; j < 2; j++){
				getInBuff().remove(6*i);
			}
		}
	}
	
}
