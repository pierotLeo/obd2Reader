package fr.obd2Reader.command;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class EngineFuelRate extends ObdCommand implements CompatibleCommand{

	private double rate;
	
	public EngineFuelRate(OutputStream out, InputStream in){
		super("01 5E", out, in);
	}
	
	public boolean isCompatible(ArrayList<Byte> vehicleRef){
		return ((vehicleRef.get(8) & 4) == 4);
	}
	
	public void compute(){
		sendCommand();
		read();
		rate = ((getInBuff().get(0)*256) + getInBuff().get(1))*0.05;
	}
	
	public double getRate(){
		return rate;
	}
}
