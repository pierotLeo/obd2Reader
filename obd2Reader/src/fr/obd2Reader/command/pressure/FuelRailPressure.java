package fr.obd2Reader.command.pressure;

import java.io.InputStream;
import java.io.OutputStream;

public class FuelRailPressure extends PressureCommand{

	public FuelRailPressure(OutputStream out, InputStream in){
		super("01 22", out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		setPressure(((getInBuff().get(0) * 256) + getInBuff().get(1)) * 0.079);
	}
	
}
