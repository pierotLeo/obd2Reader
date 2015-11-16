package fr.obd2Reader.command.temperature;

import java.io.InputStream;
import java.io.OutputStream;


public class EngineCoolantTemperatureCommand extends TemperatureCommand{
	
	public EngineCoolantTemperatureCommand(OutputStream out, InputStream in){
		super("01 05", out, in);
	}
	
	public void calculate(){
		sendCommand();
		read();
		setTemperature((float)(getInBuff().get(0)-40));
	}
	
}
